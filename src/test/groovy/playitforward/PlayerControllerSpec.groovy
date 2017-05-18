package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

@TestFor(PlayerController)
@Mock(Player)
@TestMixin(DomainClassUnitTestMixin)
class PlayerControllerSpec extends Specification {

    def populateValidParams(params) {

        assert params != null

        params["name"] = 'Simone Aiken';
        params["isWoman"] = true;
        params["alias"] = 'GrassCatt';
        params["imgUrl"] = 'Mirri.jpg';
    }

    void "Test the index action returns the correct response"() {

        mockDomain(Player, [
                [name: "Aiken, Simone [US]", alias: "Simone Aiken", isWoman: true],
                [name: "Driscoll, Michael [US]", alias: "Michael Driscoll", isWoman: false],
        ]);

        when:"The index action is executed"
        controller.index(null);

        ObjectMapper mapper = new ObjectMapper();
        List<Player> players = Arrays.asList(mapper.readValue(response.text, Player[].class)) as List<Player>;

        then:"The response is correct"
        players.size() == 2;
        players[0].name == "Simone Aiken";
    }

    void "Test the trueList action returns the correct response"() {

        mockDomain(Player, [
                [name: "Aiken, Simone [US]", alias: "Simone Aiken", isWoman: true],
                [name: "Driscoll, Michael [US]", isWoman: false],
        ]);

        when:"The TrueList action is executed"
        controller.trueList()

        ObjectMapper mapper = new ObjectMapper();
        List<Player> players = Arrays.asList(mapper.readValue(response.text, Player[].class)) as List<Player>;

        then:"The response is correct"
        players.size() == 2;
        players[0].name == "Aiken, Simone [US]";
    }

    void "Test that the show action returns the correct model"() {

        when:"The show action is executed with a null domain"
        controller.show(null);

        then:"A 404 error is returned"
        response.status == 404;

        when:"A domain instance is passed to the show action"
        response.reset();
        populateValidParams(params);
        Player player = new Player(params);
        player.save(flush: true);
        controller.show(player);

        then:"A model is populated containing the domain instance"
        player!= null;
        response.status == OK.value();
        response.json.toString() == '{"imgUrl":"Mirri.jpg","isWoman":true,"name":"Simone Aiken",' +
                '"alias":"GrassCatt","id":1}';
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is given a null"
        request.contentType = JSON_CONTENT_TYPE;
        request.method = 'POST';
        controller.save(null);

        then:"Cannot find endpoint"
        response.status == NOT_FOUND.value();

        when:"The save action is executed with an invalid instance"
        response.reset();
        request.contentType = JSON_CONTENT_TYPE;
        request.method = 'POST';
        Player player = new Player();
        player.validate();
        controller.save(player);

        then:"The create view is rendered again with the correct model"
        response.status == UNPROCESSABLE_ENTITY.value();
        response.json.errors.size() == 1;

        when:"The save action is executed with a valid instance"
        response.reset();
        populateValidParams(params);
        player = new Player(params);

        controller.save(player);

        then:"A redirect is issued to the show action"
        Player.count() == 1;
        response.status == CREATED.value();
        response.json.toString() == '{"imgUrl":"Mirri.jpg","isWoman":true,"name":"Simone Aiken",' +
                '"alias":"GrassCatt","id":1}';
    }

    void "Test the update action performs an update on a valid domain instance"() {

        when:"Update is called for a domain instance that doesn't exist"
        request.contentType = JSON_CONTENT_TYPE;
        request.method = 'PUT';
        controller.update(null);

        then:"A 404 error is returned"
        response.status == NOT_FOUND.value();

        when:"An invalid domain instance is passed to the update action"
        response.reset();
        Player player = new Player();
        player.validate();
        controller.update(player);

        then:"The edit view is rendered again with the invalid instance"
        response.status == UNPROCESSABLE_ENTITY.value();
        response.json.errors.size() == 1;

        when:"A valid domain instance is passed to the update action"
        response.reset();
        populateValidParams(params);
        player = new Player(params);
        player.save(flush: true);

        controller.update(player);

        then:"A redirect is issued to the show action"
        player != null;
        response.status == OK.value();
        response.json.id == player.id;
    }
}