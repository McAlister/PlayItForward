package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*
import static org.springframework.http.HttpStatus.*

@TestFor(EventOrganizerController)
@Mock(EventOrganizer)
@TestMixin(DomainClassUnitTestMixin)
class EventOrganizerControllerSpec extends Specification {

    def populateValidParams(params) {

        assert params != null;

        params["name"] = "Channel Fireball";
        params["url"] = "http://store.channelfireball.com/store";
    }

    void setupSpec() {

    }

    void "Test the index action returns the correct response"() {

        mockDomain(EventOrganizer, [
           [name: "Pastimes", url: "http://www.pastimes.net/"],
           [name: "Game Keeper", url: "http://www.gamekeeper.ca/"]
        ]);

        when:"The index action is executed"
            controller.index(10);

        ObjectMapper mapper = new ObjectMapper();
        List<EventOrganizer> organizers = Arrays.asList(mapper.readValue(response.text,
                EventOrganizer[].class)) as List<EventOrganizer>;

        then:"The response is correct"
            organizers.size() == 2;
            organizers[0].name == "Pastimes";
    }


    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = JSON_CONTENT_TYPE;
            request.method = 'POST';
            def eventOrganizer = new EventOrganizer();
            eventOrganizer.validate();
            controller.save(eventOrganizer);

        then:"The create view is rendered again with the correct model"
            response.status == UNPROCESSABLE_ENTITY.value();
            response.json.errors;

        when:"The save action is executed with a valid instance"
            response.reset();
            populateValidParams(params);
            eventOrganizer = new EventOrganizer(params);

            controller.save(eventOrganizer);

        then:"A redirect is issued to the show action"
            EventOrganizer.count() == 1;
            response.status == CREATED.value();
            response.json;
    }

    void "Test that the show action returns the correct model"() {

        when:"The show action is executed with a null domain"
            controller.show(null);

        then:"A 404 error is returned"
            response.status == 404;

        when:"A domain instance is passed to the show action"
            populateValidParams(params);
            response.reset();
            def eventOrganizer = new EventOrganizer(params).save();
            controller.show(eventOrganizer);

        then:"A model is populated containing the domain instance"
            eventOrganizer!= null;
            response.status == OK.value();
            response.json.toString() == '{"name":"Channel Fireball","id":1,' +
                    '"url":"http://store.channelfireball.com/store"}';
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
            def eventOrganizer= new EventOrganizer();
            eventOrganizer.validate();
            controller.update(eventOrganizer);

        then:"The edit view is rendered again with the invalid instance"
            response.status == UNPROCESSABLE_ENTITY.value();
            response.json.errors.size() == 2;

        when:"A valid domain instance is passed to the update action"
            response.reset();
            populateValidParams(params);
            eventOrganizer= new EventOrganizer(params).save(flush: true);
            controller.update(eventOrganizer);

        then:"A redirect is issued to the show action"
            eventOrganizer!= null;
            response.status == OK.value();
            response.json.id == eventOrganizer.id;
    }

    void "Test that the delete action deletes an instance if it exists"() {

        when:"The delete action is called for a null instance"
            request.contentType = JSON_CONTENT_TYPE;
            request.method = 'DELETE';
            controller.delete(null);

        then:"A 404 is returned"
            response.status == NOT_FOUND.value();

        when:"A domain instance is created"
            response.reset();
            populateValidParams(params);
            def eventOrganizer= new EventOrganizer(params).save(flush: true);

        then:"It exists"
            EventOrganizer.count() == 1;

        when:"The domain instance is passed to the delete action"
            controller.delete(eventOrganizer);

        then:"The instance is deleted"
            EventOrganizer.count() == 0;
            response.status == NO_CONTENT.value();
    }
}