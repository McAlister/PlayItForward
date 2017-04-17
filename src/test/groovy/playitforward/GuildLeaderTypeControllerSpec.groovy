package playitforward

import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

@TestFor(GuildLeaderTypeController)
@Mock(GuildLeaderType)
@TestMixin(DomainClassUnitTestMixin)
class GuildLeaderTypeControllerSpec extends Specification {

    void setupSpec() {

        // Empty Block;
    }

    void "Test the index action returns the correct response"() {

        mockDomain(GuildLeaderType, [
                [description: "Organizer", type: "ORGANIZER"],
                [description: "Mentor", type: "MENTOR"],
                [description: "Rules Advisor", type: "RULES_ADVISOR"]
        ]);

        when:"The index action is executed"
            controller.index(null);

        then:"The response is correct"
            response.text == '[{"id":2,"description":"Mentor","type":"MENTOR"},' +
                    '{"id":1,"description":"Organizer","type":"ORGANIZER"},' +
                    '{"id":3,"description":"Rules Advisor","type":"RULES_ADVISOR"}]'
    }

}