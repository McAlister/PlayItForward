package playitforward

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(S3Service)
class S3ServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test nothing"() {
        expect:"no tests"
        true
    }
}
