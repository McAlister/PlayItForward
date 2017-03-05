package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*

@TestFor(SysconfigController)
@Mock(Sysconfig)
@TestMixin(DomainClassUnitTestMixin)
class SysconfigControllerSpec extends Specification {

    void "Test the ping action returns the correct response"() {

        mockDomain(Sysconfig, [
                [key: "DB Version", value: "1.0.0"],
                [key: "SW Version", value: "2.0.0"],
        ]);

        when:"The ping action is executed"
        controller.index(10);

        ObjectMapper mapper = new ObjectMapper();
        List<Sysconfig> sysconfigList = Arrays.asList(mapper.readValue(response.text, Sysconfig[].class)) as List<Sysconfig>;

        then:"The response is correct"
        sysconfigList.size() == 2;
        sysconfigList[0].key == "DB Version";
        sysconfigList[1].key == "SW Version";
    }
}