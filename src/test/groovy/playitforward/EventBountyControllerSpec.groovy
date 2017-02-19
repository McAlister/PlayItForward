package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

import java.sql.Date

@TestFor(EventBountyController)
@Mock([EventType, EventOrganizer, Event, EventBounty])
@TestMixin(DomainClassUnitTestMixin)
class EventBountyControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        params["donor"] = "Cassidy Melczak";
        params["prize"] = "One box Aether Revolt";
        params["event"] = 1;
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
}