package playitforward

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(EventStanding)
class EventStandingSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test X-3 in round 8 can make it"() {

        expect:"Hey!  She can totally make it!"
        EventStanding eventStanding = new EventStanding();
        eventStanding.setRound(8);
        eventStanding.setPoints(15);
        eventStanding.setEvent(new Event(startDate: "2017-01-01"));
        eventStanding.canMakeDayTwo();
    }

    void "test X-4 can't make it"() {

        expect:"Doesn't realize she's lost 4 day 1"
        EventStanding eventStanding = new EventStanding();
        eventStanding.setRound(6);
        eventStanding.setPoints(6);
        eventStanding.setEvent(new Event(startDate: "2017-01-01"));
        !eventStanding.canMakeDayTwo();
    }

    void "test X-9 Already Made it"() {

        expect:"Doesn't realize she is already day 2"
        EventStanding eventStanding = new EventStanding();
        eventStanding.setRound(15);
        eventStanding.setPoints(18);
        eventStanding.setEvent(new Event(startDate: "2017-01-01"));
        eventStanding.canMakeDayTwo();
    }

    void "test X-3-1 didn't make day 2" () {

        expect:"Thinks she made it"
        EventStanding eventStanding = new EventStanding();
        eventStanding.setRound(15);
        eventStanding.setPoints(16);
        eventStanding.setEvent(new Event(startDate: "2017-01-01"));
        !eventStanding.canMakeDayTwo();
    }

    void "test 2019 X-3 in round 8 can't make it"() {

        expect:"Hey!  She can totally make it!"
        EventStanding eventStanding = new EventStanding();
        eventStanding.setRound(8);
        eventStanding.setPoints(15);
        eventStanding.setEvent(new Event(startDate: "2019-01-01"));
        !eventStanding.canMakeDayTwo();
    }

    void "test 2019 X-3 can't make it"() {

        expect:"Doesn't realize she's lost 4 day 1"
        EventStanding eventStanding = new EventStanding();
        eventStanding.setRound(6);
        eventStanding.setPoints(9);
        eventStanding.setEvent(new Event(startDate: "2019-01-01"));
        !eventStanding.canMakeDayTwo();
    }

    void "test 2019 X-9 Already Made it"() {

        expect:"Doesn't realize she is already day 2"
        EventStanding eventStanding = new EventStanding();
        eventStanding.setRound(15);
        eventStanding.setPoints(18);
        eventStanding.setEvent(new Event(startDate: "2019-01-01"));
        eventStanding.canMakeDayTwo();
    }
}
