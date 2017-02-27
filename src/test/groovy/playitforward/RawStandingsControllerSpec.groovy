package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

import java.sql.Date

@TestFor(RawStandingsController)
@Mock([EventType, EventOrganizer, Event, RawStandings])
@TestMixin(DomainClassUnitTestMixin)
class RawStandingsControllerSpec extends Specification {

    def populateValidParams(params) {

        assert params != null

        params["name"] = "Simone Aiken";
        params["round"] = 1;
        params["points"] = 3;
        params["rank"] = 400;
        params["opponentMatchWin"] = 0.33;
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

        mockDomain(RawStandings, [
                [event: 1, name: "Simone Aiken", round: 1, points: 3, rank: 400, opponentMatchWin: 0.33],
                [event: 2, name: "Michael Driscoll", round: 2, points: 6, rank: 200, opponentMatchWin: 0.33]
        ]);

        when:"The index action is executed"
            controller.index(10);

            ObjectMapper mapper = new ObjectMapper();
            List<RawStandings> rawList = Arrays.asList(mapper.readValue(response.text, RawStandings[].class)) as List<RawStandings>;

        then:"The response is correct"
            rawList.size() == 2;
            rawList[0].name == 'Simone Aiken';
            rawList[1].name == 'Michael Driscoll';
    }
}