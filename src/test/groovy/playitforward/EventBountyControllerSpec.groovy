package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

import java.sql.Date

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY

@TestFor(EventBountyController)
@Mock([EventType, EventOrganizer, Event, EventBounty])
@TestMixin(DomainClassUnitTestMixin)
class EventBountyControllerSpec extends Specification {

    def populateValidParams(params) {

        assert params != null

        Event event = new Event();
        event.setName('GP Test');
        event.setType(new EventType(id: 1, type: "GP", description: "Grand Prix"));
        event.setEventCode('gp_test_17');
        event.setStartDate(new Date(2017, 1, 1));
        event.setEndDate(new Date(2017, 1, 1));
        event.setOrganizer(new EventOrganizer(id: 1, name: "Last Knight Games", url: "http://www.test.com"));

        params["donor"] = "Cassidy Melczak";
        params["prize"] = "One box Aether Revolt";
        params["event"] = event;
    }

    void setupSpec() {

    }

    void "Test the index action returns the correct response"() {

        mockDomain(EventType, [
           [description: "Grand Prix", type: "GP"],
           [description: "Unknown Type", type: "OTHER"],
        ]);

        mockDomain(EventOrganizer, [
           [name: "Channel Fireball", url: "http://www.channelfireball.com"]
        ]);

        mockDomain(Event, [
           [name: "GP Pittsburgh", organizer: 1, eventCode: "gppit17", type: 1,
                    startDate: new Date(2017, 11, 26), endDate: new Date(2017, 11, 27)],
           [name: "GP New Jersey", organizer: 1, eventCode: "gpsj17", type: 1,
                    startDate: new Date(2017, 11, 26), endDate: new Date(2017, 11, 27)]
        ]);

        mockDomain(EventBounty, [
           [donor: "Simone Aiken", prize: "A playmat", event: 1],
           [donor: "Hannah SP", prize: "Mentorship", event: 2]
        ]);

        when:"The index action is executed"
            controller.index(10);

            ObjectMapper mapper = new ObjectMapper();
            List<EventBounty> bounties = Arrays.asList(mapper.readValue(response.text, EventBounty[].class)) as List<EventBounty>;

        then:"The response is correct"
            bounties.size() == 2;
            bounties[0].donor == "Simone Aiken";
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
        def eventBounty = new EventBounty();
        eventBounty.validate();
        controller.save(eventBounty);

        then:"The create view is rendered again with the correct model"
        response.status == UNPROCESSABLE_ENTITY.value();
        response.json.errors.size() == 3;

        when:"The save action is executed with a valid instance"
        response.reset();
        populateValidParams(params);
        eventBounty = new EventBounty(params);
        eventBounty.event.organizer.save();
        eventBounty.event.type.save();
        eventBounty.event.save();

        controller.save(eventBounty);

        then:"A redirect is issued to the show action"
        Event.count() == 1;
        response.status == CREATED.value();
        response.json.toString() == '{"donor":"Cassidy Melczak","id":1,"event":{"id":1},"prize":"One box Aether Revolt"}';
    }

    void "Test that the show action returns the correct model"() {

        when:"The show action is executed with a null domain"
        controller.show(null);

        then:"A 404 error is returned"
        response.status == 404;

        when:"A domain instance is passed to the show action"
        response.reset();
        populateValidParams(params);
        EventBounty eventBounty = new EventBounty(params);
        eventBounty.event.organizer.save();
        eventBounty.event.type.save()
        eventBounty.event.save();
        eventBounty.save(flush: true);
        controller.show(eventBounty);

        then:"A model is populated containing the domain instance"
        eventBounty!= null;
        response.status == OK.value();
        response.json.toString() == '{"donor":"Cassidy Melczak","id":1,"event":{"id":1},"prize":"One box Aether Revolt"}';
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
        EventBounty eventBounty= new EventBounty();
        eventBounty.validate();
        controller.update(eventBounty);

        then:"The edit view is rendered again with the invalid instance"
        response.status == UNPROCESSABLE_ENTITY.value();
        response.json.errors.size() == 3;

        when:"A valid domain instance is passed to the update action"
        response.reset();
        populateValidParams(params);
        eventBounty = new EventBounty(params);
        eventBounty.event.type.save();
        eventBounty.event.organizer.save();
        eventBounty.event.save();
        eventBounty.save(flush: true);

        controller.update(eventBounty);

        then:"A redirect is issued to the show action"
        eventBounty != null;
        response.status == OK.value();
        response.json.id == eventBounty.id;
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
        EventBounty eventBounty = new EventBounty(params);
        eventBounty.event.type.save();
        eventBounty.event.organizer.save();
        eventBounty.event.save();
        eventBounty.save(flush: true);

        then:"It exists"
        EventBounty.count() == 1;

        when:"The domain instance is passed to the delete action"
        controller.delete(eventBounty);

        then:"The instance is deleted"
        EventBounty.count() == 0;
        response.status == NO_CONTENT.value();
    }
}