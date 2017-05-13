package playitforward

import grails.test.mixin.integration.Integration
import grails.transaction.*
import groovy.json.JsonSlurper

import static org.springframework.http.HttpStatus.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder

@Integration
@Transactional()
class PersonFunctionalSpec extends GebSpec {

    def sessionFactory;
    static String authToken;

    RestBuilder getRestBuilder() {
        new RestBuilder()
    }

    String getResourcePath() {
        return "${baseUrl}/api/Person";
    }

    Closure getValidJson() {{->

        List<PersonType> typeList = PersonType.list();
        int id = typeList.get(0).id;
        //  email | first_name | last_name | person_type | phone | send_push_notifications
        def jsonSlurper = new JsonSlurper();
        return jsonSlurper.parseText('{"firstName": "Jane Q.", "lastName": "Tester", "email": "test@tester.com", ' +
                '"phone": "15555555555", "send_push_notifications": "false", "personType": "' + id + '"}');
    }}

    Closure getInvalidJson() {{->

        def jsonSlurper = new JsonSlurper();
        return jsonSlurper.parseText('{"firstName": "Jane Q.", "lastName": "Tester", "email": null, ' +
                '"phone": "15555555555", "send_push_notifications": "false"}');
    }}

    String getToken() {

        if (authToken != null) {
            return authToken;
        }

        def response = restBuilder.post("${baseUrl}/api/login") {
            json {
                username = 'play_it_forward@outlook.com'
                password = 'Bring1tLadies!'
            }
        }

        authToken = response.json.access_token;
        return authToken;
    }

    void "Test the index action"() {

        when:"The index action is requested"
        def response = restBuilder.get(resourcePath){
            header 'Authorization', "Bearer " + token
        };

        then:"The response is correct"
        response.status == OK.value();
        response.json.size() == Person.count();
        response.json[0].has('firstName');
        response.json[0].has('lastName');
    }

    void "Test CRUD Happy Path"() {

        int oldCount = Person.count();

        // /////////////// //
        // Save The Record //
        // /////////////// //

        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            json validJson
        }

        then:"The response is correct"
        response.status == CREATED.value()
        int id = response.json.id;
        Person.count() == oldCount + 1;

        // /////////////// //
        // Show the Record //
        // /////////////// //

        when:"When the show action is called to retrieve a resource"
        response = restBuilder.get("$resourcePath/$id"){
            header 'Authorization', "Bearer " + token
        };

        then:"The response is correct"
        response.status == OK.value();
        response.json.id == id;

        // ///////////////// //
        // Update the Record //
        // ///////////////// //

        when:"The update action is called with valid data"
        response = restBuilder.put("$resourcePath/$id") {
            json response.json
            header 'Authorization', "Bearer " + token
        };

        then:"The response is correct"
        response.status == OK.value();
        response.json.id == id;

        // ///////////////// //
        // Delete The Record //
        // ///////////////// //

        when:"When the delete action is executed on an existing instance"
        final session = sessionFactory.currentSession;
        def update = session.createSQLQuery("Delete From Person Where id = " + id);
        update.executeUpdate();
        session.flush();

        then:"The response is correct"
        Person.count() == oldCount;
    }

    void "Test save action errors"() {

        when:"The save action is executed with no content"
        def response = restBuilder.post(resourcePath);

        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value();

        when:"The save action is executed with invalid data"
        response = restBuilder.post(resourcePath) {
            json invalidJson
        };

        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value();
    }

    void "Test the update action errors"() {

        int id = Person.list()[0].getId();

        when:"The update action is executed without login credentials"
        def response = restBuilder.put("$resourcePath/$id");

        then:"The response is correct"
        response.status == UNAUTHORIZED.value();

        when:"The update action is called with invalid data"
        response = restBuilder.put("$resourcePath/$id") {
            header 'Authorization', "Bearer " + token
            json invalidJson
        };

        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value();
    }
}