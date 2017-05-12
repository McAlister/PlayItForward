package playitforward

import grails.test.mixin.integration.Integration
import grails.transaction.*
import groovy.json.JsonSlurper

import static org.springframework.http.HttpStatus.*
import geb.spock.*
import grails.plugins.rest.client.RestBuilder

@Integration
@Rollback
class EventOrganizerFunctionalSpec extends GebSpec {

    static String authToken;

    RestBuilder getRestBuilder() {
        new RestBuilder();
    }

    String getResourcePath() {
        return "${baseUrl}/api/EventOrganizer";
    }

    Closure getValidJson() {{->

        def jsonSlurper = new JsonSlurper();
        return jsonSlurper.parseText('{"name": "Last Knight Games", "url": "http://test.com"}');
    }}

    Closure getInvalidJson() {{->

        def jsonSlurper = new JsonSlurper();
        return jsonSlurper.parseText('{"name": null}');
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
        def response = restBuilder.get(resourcePath);

        then:"The response is correct"
        response.status == OK.value();
        response.json.size() == EventOrganizer.count();
        response.json[0].has('name');
        response.json[0].has('url');
    }

    void "Test the save action correctly persists an instance"() {

        int oldCount = EventOrganizer.count();

        // /////////////// //
        // Save The Record //
        // /////////////// //

        when:"The save action is executed with valid data"
        def response = restBuilder.post(resourcePath) {
            header 'Authorization', "Bearer " + token
            json validJson
        }        

        then:"The response is correct"
        response.status == CREATED.value()
        int id = response.json.id;
        EventOrganizer.count() == oldCount + 1;

        // /////////////// //
        // Show the Record //
        // /////////////// //

        when:"When the show action is called to retrieve a resource"
        response = restBuilder.get("$resourcePath/$id");

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
        response = restBuilder.delete("$resourcePath/$id"){
            header 'Authorization', "Bearer " + token
        };

        then:"The response is correct"
        response.status == NO_CONTENT.value();
        !EventOrganizer.get(id)
    }

    void "Test save action errors"() {

        when:"The save action is executed without login credentials"
        def response = restBuilder.post(resourcePath);

        then:"The response is correct"
        response.status == UNAUTHORIZED.value()

        when:"The save action is executed with no content"
        response = restBuilder.post(resourcePath){
            header 'Authorization', "Bearer " + token
        };

        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value()

        when:"The save action is executed with invalid data"
        response = restBuilder.post(resourcePath) {
            header 'Authorization', "Bearer " + token
            json invalidJson
        }
        then:"The response is correct"
        response.status == UNPROCESSABLE_ENTITY.value()
    }

    void "Test the update action errors"() {

        int id = EventOrganizer.list()[0].getId();

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

    void "Test the delete action errors"() {

        when:"The delete action is executed without login credentials"
        def response = restBuilder.delete("$resourcePath/99999");

        then:"The response is correct"
        response.status == UNAUTHORIZED.value();

        when:"When the delete action is executed on an unknown instance"
        response = restBuilder.delete("$resourcePath/99999") {
            header 'Authorization', "Bearer " + token
        };

        then:"The response is correct"
        response.status == NOT_FOUND.value();
    }
}