package playitforward

import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

@TestFor(EventTypeController)
@Mock(EventType)
@TestMixin(DomainClassUnitTestMixin)
class EventTypeControllerSpec extends Specification {

    void setupSpec() {
    }

    void "Test the index action returns the correct response"() {

        mockDomain(EventType, [
                [description: "Grand Prix", type: "GP"],
                [description: "Unknown Type", type: "OTHER"],
        ])

        when:"The index action is executed"
            controller.index(null)

        then:"The response is correct"
            response.text == '[{"id":1,"description":"Grand Prix","type":"GP"},' +
                    '{"id":2,"description":"Unknown Type","type":"OTHER"}]'
    }
}
