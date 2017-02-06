package playitforward

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(PushNotificationController)
class PushNotificationControllerSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test something"() {

        PushNotificationController test = new PushNotificationController();
        def result = test.index("gpsj17", 9);

        expect:"fix me"
            true == false
    }
}
