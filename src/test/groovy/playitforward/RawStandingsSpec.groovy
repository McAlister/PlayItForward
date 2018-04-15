package playitforward

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(RawStandings)
class RawStandingsSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test X-3 in round 8 can make it"() {

        expect:"Hey!  She can totally make it!"
            RawStandings raw = new RawStandings();
            raw.setRound(7);
            raw.setPoints(15);
            raw.canMakeDayTwo();
    }

    void "test X-4 can't make it"() {

        expect:"Doesn't realize she's lost 4 day 1"
            RawStandings raw = new RawStandings();
            raw.setRound(6);
            raw.setPoints(6);
            !raw.canMakeDayTwo();
    }

    void "test X-9 Already Made it"() {

        expect:"Doesn't realize she is already day 2"
            RawStandings raw = new RawStandings();
            raw.setRound(15);
            raw.setPoints(18);
            raw.canMakeDayTwo();
    }

    void "test X-2-1 didn't make day 2" () {

        expect:"Thinks she made it"
        RawStandings raw = new RawStandings();
        raw.setRound(8);
        raw.setPoints(16);
        !raw.canMakeDayTwo();
    }
}
