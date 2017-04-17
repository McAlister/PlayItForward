package playitforward

import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*
import static org.springframework.http.HttpStatus.*

@TestFor(GuildController)
@Mock(Guild)
@TestMixin(DomainClassUnitTestMixin)
class GuildControllerSpec extends Specification {

    void setupSpec() {

        // Empty Block;
    }

    def populateValidParams(params) {

        assert params != null

        params["name"] = 'La Belles Revel';
        params["description"] = 'Southern Ladies';
        params["url"] = 'http://blessyourheart.com';
        params["twitter"] = '@hello';
        params["address"] = '123 Fake Boulevard';
        params["email"] = '123@fake.org';
        params["phone"] = '555-555-5556';
    }

    void "Test the index action returns the correct response"() {

        mockDomain(Guild, [
                [name: "Ice Furies", description: "Canada Ladies", url: "http://buttkicking.com",
                 twitter: "@nope", address: "123 Fake Street", email: "123@fake.com", phone: "555-555-5555"]
        ]);

        when:"The index action is executed"
            controller.index(null);

        then:"The response is correct"
            response.text == '[{"id":1,"address":"123 Fake Street","description":"Canada Ladies",' +
                    '"email":"123@fake.com","name":"Ice Furies","phone":"555-555-5555",' +
                    '"twitter":"@nope","url":"http://buttkicking.com"}]';
    }


    void "Test the save action correctly persists an instance"() {

        when:"The save action is given a null"
            request.contentType = JSON_CONTENT_TYPE;
            request.method = 'POST';
            controller.save(null);;

        then:"Cannot find endpoint"
            response.status == NOT_FOUND.value();

        when:"The save action is executed with an invalid instance"
            response.reset();
            def group = new Guild();
            group.validate();
            controller.save(group);

        then:"The create view is rendered again with the correct model"
            response.status == UNPROCESSABLE_ENTITY.value();
            response.json.errors.size() == 2;;

        when:"The save action is executed with a valid instance"
            response.reset();
            populateValidParams(params);
            group = new Guild(params);

            controller.save(group);

        then:"A redirect is issued to the show action"
            Guild.count() == 1;
            response.status == CREATED.value();
            response.json.toString() == '{"twitter":"@hello","address":"123 Fake Boulevard",' +
                    '"phone":"555-555-5556","name":"La Belles Revel","description":"Southern Ladies",' +
                    '"id":1,"email":"123@fake.org","url":"http://blessyourheart.com"}';
    }


    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null);

        then:"A 404 error is returned"
            response.status == NOT_FOUND.value();

        when:"A domain instance is passed to the show action"
            populateValidParams(params);
            response.reset();
            def group = new Guild(params).save();
            controller.show(group);

        then:"A model is populated containing the domain instance"
            group!= null;
            response.status == OK.value();
            response.json.toString() == '{"twitter":"@hello","address":"123 Fake Boulevard",' +
                    '"phone":"555-555-5556","name":"La Belles Revel","description":"Southern Ladies",' +
                    '"id":1,"email":"123@fake.org","url":"http://blessyourheart.com"}';
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.status == NOT_FOUND.value()

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def group = new Guild()
            group.validate()
            controller.update(group)

        then:"The edit view is rendered again with the invalid instance"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors.size() == 2;

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            group = new Guild(params).save(flush: true)
            controller.update(group)

        then:"A redirect is issued to the show action"
            group!= null
            response.status == OK.value()
            response.json.id == group.id
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.status == NOT_FOUND.value()

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def group = new Guild(params).save(flush: true)

        then:"It exists"
            Guild.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(group)

        then:"The instance is deleted"
            Guild.count() == 0
            response.status == NO_CONTENT.value()
    }
}