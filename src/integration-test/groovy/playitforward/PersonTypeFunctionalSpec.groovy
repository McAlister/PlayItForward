package playitforward

import grails.test.mixin.integration.Integration
import grails.transaction.*
import static org.springframework.http.HttpStatus.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder

@Integration
@Rollback
class PersonTypeFunctionalSpec extends GebSpec {

    RestBuilder getRestBuilder() {
        new RestBuilder();
    }

    String getResourcePath() {
        return "${baseUrl}/api/PersonType";
    }

    void "Test the index action"() {
        when:"The index action is requested";
        def response = restBuilder.get(resourcePath);

        then:"The response is correct";
        response.status == OK.value();
        response.json.size() == 5;
        response.json[0].has('description');
        response.json[0].has('type');
        response.json[0].has('id');
    }
}