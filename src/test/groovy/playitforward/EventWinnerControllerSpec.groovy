package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

import java.sql.Date

import static org.springframework.http.HttpStatus.*

@TestFor(EventWinnerController)
@Mock([EventType, EventOrganizer, Event, EventWinner])
@TestMixin(DomainClassUnitTestMixin)
class EventWinnerControllerSpec extends Specification {

    def populateValidParams(params) {

        assert params != null

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
                [name     : "GP Pittsburgh", organizer: 1, eventCode: "gppit17", type: 1,
                 startDate: new Date(2017, 11, 26), endDate: new Date(2017, 11, 27)],
                [name: "GP New Jersey", organizer: 1, eventCode: "gpsj17", type: 1,
                 startDate: new Date(2017, 11, 26), endDate: new Date(2017, 11, 27)]
        ]);

        mockDomain(EventWinner, [
                [event: 1, name: 'Simone Aiken', ranking: 42, record: '11-3-1', blurb: 'Everything is awesome!']
        ]);

        when:"The index action is executed"
        controller.index(10);

        ObjectMapper mapper = new ObjectMapper();
        List<EventWinner> winners = Arrays.asList(mapper.readValue(response.text, EventWinner[].class)) as List<EventBounty>;

        then:"The response is correct"
        winners.size() == 1;
        winners[0].name == "Simone Aiken";
    }
}