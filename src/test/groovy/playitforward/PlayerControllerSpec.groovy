package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

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
                [name: "Simone Aiken", isWoman: true],
                [name: "Michael Driscoll", isWoman: false],
        ]);

        when:"The index action is executed"
        controller.index(10);

        ObjectMapper mapper = new ObjectMapper();
        List<Player> players = Arrays.asList(mapper.readValue(response.text, Player[].class)) as List<Player>;

        then:"The response is correct"
        players.size() == 2;
        players[0].name == "Simone Aiken";
    }
}