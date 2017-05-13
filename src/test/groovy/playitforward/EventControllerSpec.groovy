package playitforward

import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*
import static org.springframework.http.HttpStatus.*
import java.sql.Date

@TestFor(EventController)
@Mock([Event, EventType, EventOrganizer])
@TestMixin(DomainClassUnitTestMixin)
class EventControllerSpec extends Specification {

    void setupSpec() {

        // Empty Block;
    }

    def populateValidParams(params) {

        assert params != null

        params["name"] = 'GP Test';
        params["type"] = new EventType(id: 1, type: "GP", description: "Grand Prix");
        params["eventCode"] = 'gp_test_17';
        params["startDate"] = new Date(2017, 1, 1);
        params["endDate"] = new Date(2017, 1, 1);
        params["organizer"] = new EventOrganizer(id: 1, name: "Last Knight Games", url: "hhtp://www.test.com");
    }

    void "Test the index action returns the correct response"() {

        mockDomain(EventType, [
                [type: "GP", description: "Grand Prix"]
        ]);

        mockDomain(EventOrganizer, [
                [name: "Last Knight Games", url: "http://www.test.com"]
        ]);

        mockDomain(Event, [
                [name: "GP Test", type: 1, organizer: 1, eventCode: "gp_test_17",
                 startDate: new Date(2017, 1, 1),
                 endDate: new Date(2017, 1, 1)]
        ]);

        when:"The index action is executed"
            controller.index(null);

        then:"The response is correct"
            response.text == '[{"id":1,"endDate":"3917-02-01T07:00:00Z","eventCode":"gp_test_17",' +
                    '"name":"GP Test","organizer":{"id":1},"startDate":"3917-02-01T07:00:00Z","type":{"id":1}}]';
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
            def event = new Event();
            event.validate();
            controller.save(event);

        then:"The create view is rendered again with the correct model"
            response.status == UNPROCESSABLE_ENTITY.value();
            response.json.errors.size() == 5;

        when:"The save action is executed with a valid instance"
            response.reset();
            populateValidParams(params);
            event = new Event(params);
            event.type.save();
            event.organizer.save();

            controller.save(event);

        then:"A redirect is issued to the show action"
            Event.count() == 1;
            response.status == CREATED.value();
            response.json.toString() == '{"eventCode":"gp_test_17","endDate":"3917-02-01T07:00:00Z",' +
                    '"organizer":{"id":1},"name":"GP Test","id":1,"type":{"id":1},"startDate":"3917-02-01T07:00:00Z"}';
    }

    void "Test that the show action returns the correct model"() {

        when:"The show action is executed with a null domain"
            controller.show(null);

        then:"A 404 error is returned"
            response.status == 404;

        when:"A domain instance is passed to the show action"
            response.reset();
            populateValidParams(params);
            Event event = new Event(params);
            event.type.save();
            event.organizer.save();
            event.save(flush: true);
            controller.show(event);

        then:"A model is populated containing the domain instance"
            event!= null;
            response.status == OK.value();
            response.json.toString() == '{"eventCode":"gp_test_17","endDate":"3917-02-01T07:00:00Z",' +
                    '"organizer":{"id":1},"name":"GP Test","id":1,"type":{"id":1},"startDate":"3917-02-01T07:00:00Z"}';
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
            def event= new Event();
            event.validate();
            controller.update(event);

        then:"The edit view is rendered again with the invalid instance"
            response.status == UNPROCESSABLE_ENTITY.value();
            response.json.errors.size() == 5;

        when:"A valid domain instance is passed to the update action"
            response.reset();
            populateValidParams(params);
            event = new Event(params);
            event.type.save();
            event.organizer.save();
            event.save(flush: true);

            controller.update(event);

        then:"A redirect is issued to the show action"
            event != null;
            response.status == OK.value();
            response.json.id == event.id;
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
            Event event = new Event(params);
            event.type.save();
            event.organizer.save();
            event.save(flush: true);

        then:"It exists"
            Event.count() == 1;

        when:"The domain instance is passed to the delete action"
            controller.delete(event);

        then:"The instance is deleted"
            Event.count() == 0;
            response.status == NO_CONTENT.value();
    }
}