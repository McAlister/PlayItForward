package playitforward

import com.fasterxml.jackson.databind.ObjectMapper
import grails.test.mixin.*
import spock.lang.*
import static org.springframework.http.HttpStatus.*
import grails.test.mixin.domain.DomainClassUnitTestMixin

@TestFor(PersonController)
@Mock([Person, PersonType])
@TestMixin(DomainClassUnitTestMixin)
class PersonControllerSpec extends Specification {

    def populateValidParams(params) {

        assert params != null

        params["firstName"] = 'Jane Q.';
        params["lastName"] = 'Tester';
        params["email"] = 'test@unitTest.com';
        params["personType"] = new PersonType(id: 1, description: 'A future woman pro', type: 'WOMAN');
        params["phone"] = '555-555-5555';
        params["sendPushNotifications"] = false;
    }

    void setupSpec() {

    }

    void "Test the index action returns the correct response"() {

        mockDomain(PersonType, [
           [description: "A future woman pro", type: "WOMAN"]
        ]);

        mockDomain(Person, [
            [personType: 1, firstName: "Simone", lastName: "Aiken", email: "test@unitTest.com",
                phone: "555-555-5555", sendPushNotifications: true]
        ]);

        when:"The index action is executed"
            controller.index(10);

        ObjectMapper mapper = new ObjectMapper();
        List<Person> people = Arrays.asList(mapper.readValue(response.text,
            Person[].class)) as List<Person>;

        then:"The response is correct"
            people.size() == 1;
            people[0].firstName == "Simone";
            people[0].personType.id == 1;
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is given a null"
            request.contentType = JSON_CONTENT_TYPE;
            request.method = 'POST';
            controller.save(null);

        then:"Cannot find endpoint"
            response.status == NOT_FOUND.value();

        when:"The save action is executed with an invalid instance"
            response.reset();
            request.contentType = JSON_CONTENT_TYPE;
            request.method = 'POST';
            def person = new Person();
            person.validate();
            controller.save(person);

        then:"The create view is rendered again with the correct model"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors.size() == 4;

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            person = new Person(params)
            person.personType.save();

            controller.save(person);

        then:"A redirect is issued to the show action"
            Person.count() == 1;
            response.status == CREATED.value();
            response.json.toString() == '{"firstName":"Jane Q.","lastName":"Tester","sendPushNotifications":false,' +
                    '"phone":"555-555-5555","id":1,"personType":{"id":1},"email":"test@unitTest.com"}';
    }

    void "Test that the show action returns the correct model"() {

        when:"The show action is executed with a null domain"
            controller.show(null);

        then:"A 404 error is returned"
            response.status == 404;

        when:"A domain instance is passed to the show action"
            populateValidParams(params);
            response.reset();
            def person = new Person(params).save();
            person.personType.save();
            controller.show(person);

        then:"A model is populated containing the domain instance"
            person != null;
            response.status == OK.value();
            response.json.toString() == '{"firstName":"Jane Q.","lastName":"Tester","sendPushNotifications":false,' +
                    '"phone":"555-555-5555","id":1,"personType":{"id":1},"email":"test@unitTest.com"}';
    }

    void "Test the update action performs an update on a valid domain instance"() {

        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = JSON_CONTENT_TYPE;
            request.method = 'PUT';
            controller.update(null);

        then:"A 404 error is returned"
            response.status == NOT_FOUND.value();

        when:"An invalid domain instance is passed to the update action"
            response.reset();
            def person = new Person();
            person.validate();
            controller.update(person);

        then:"The edit view is rendered again with the invalid instance"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors.size() == 4;

        when:"A valid domain instance is passed to the update action"
            response.reset();
            populateValidParams(params);

            person = new Person(params);
            person.personType.save();
            person.save(flush: true);

            controller.update(person);

        then:"A redirect is issued to the show action"
            person != null;
            response.status == OK.value();
            response.json.id == person.id;
    }
}