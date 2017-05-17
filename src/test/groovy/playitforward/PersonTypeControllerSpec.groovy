package playitforward

import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

@TestFor(PersonTypeController)
@Mock(PersonType)
@TestMixin(DomainClassUnitTestMixin)
class PersonTypeControllerSpec extends Specification {

    void setupSpec() {

        // Empty Block
    }

    void "Test the index action returns the correct response"() {

        mockDomain(PersonType, [
                [description: "A future Woman Pro", type: "WOMAN"],
                [description: "Supporter", type: "OTHER"],
        ]);

        when:"The index action is executed"
            controller.index(null);

        then:"The response is correct"
            response.text == '[{"id":1,"description":"A future Woman Pro","type":"WOMAN"},' +
                    '{"id":2,"description":"Supporter","type":"OTHER"}]'
    }
}