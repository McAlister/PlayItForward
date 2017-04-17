package playitforward

import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin
import spock.lang.*
import static org.springframework.http.HttpStatus.*

@TestFor(GuildLeaderController)
@Mock([GuildLeader, Guild, User, GuildLeaderType])
@TestMixin(DomainClassUnitTestMixin)
class GuildLeaderControllerSpec extends Specification {

    void setupSpec() {

        // Empty Block;
    }

    def populateValidParams(params) {

        assert params != null;

        params["name"] = 'Simone Aiken';
        params["imageName"] = 'Avacyn.png';

        params["type"] = new GuildLeaderType(id: 1, type: 'ORGANIZER', description: 'Organizer');
        params["user"] = new User(id: 1, username: 'saiken@fake.com', password: 'test', enabled: true);
        params["guild"] = new Guild(id: 1, name: 'foo', description: 'bar', email: "123@fake.com",
                                    phone: "555-555-5555", url: "http://www.google.com");
    }

    void "Test the index action returns the correct response"() {

        mockDomain(GuildLeaderType, [
                [description: "Organizer", type: "ORGANIZER"],
                [description: "Mentor", type: "MENTOR"],
                [description: "Rules Advisor", type: "RULES_ADVISOR"]
        ]);

        mockDomain(Guild, [
                [name: "Ice Furies", description: "Canada Ladies", url: "http://buttkicking.com",
                 twitter: "@nope", address: "123 Fake Street", email: "123@fake.com", phone: "555-555-5555"]
        ]);

        mockDomain(User, [
                [username: "saiken@ulfheim.net", password: "test123", enabled: true]
        ]);

        mockDomain(GuildLeader, [
                [name: "Simone Aiken", type: 1, guild: 1, user: 1, imageName: "Avacyn.png"]
        ]);

        when:"The index action is executed"
            controller.index(null);

        then:"The response is correct"
            response.text == '[{"id":1,"guild":{"id":1},"imageName":"Avacyn.png","name":"Simone Aiken",' +
                    '"type":{"id":1},"user":{"id":1}}]';
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
            request.contentType = JSON_CONTENT_TYPE
            request.method = 'POST'
            def guildLeader = new GuildLeader()
            guildLeader.validate()
            controller.save(guildLeader)

        then:"The create view is rendered again with the correct model"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors.size() == 4;

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            guildLeader = new GuildLeader(params);
            guildLeader.type.save();
            guildLeader.guild.save();
            guildLeader.user.save();
            guildLeader = new GuildLeader(params);

            controller.save(guildLeader);

        then:"A redirect is issued to the show action"
            GuildLeader.count() == 1;
            response.status == CREATED.value();
            response.json.toString() == '{"guild":{"id":1},"imageName":"Avacyn.png","name":"Simone Aiken",' +
                    '"id":1,"type":{"id":1},"user":{"id":1}}';
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null);

        then:"A 404 error is returned"
            response.status == 404;

        when:"A domain instance is passed to the show action"
            populateValidParams(params);
            response.reset();
            def guildLeader = new GuildLeader(params);
            guildLeader.type.save();
            guildLeader.guild.save();
            guildLeader.user.save();
            guildLeader.save();
            controller.show(guildLeader);

        then:"A model is populated containing the domain instance"
            guildLeader!= null;
            response.status == OK.value();
            response.json.toString() == '{"guild":{"id":1},"imageName":"Avacyn.png","name":"Simone Aiken",' +
                    '"id":1,"type":{"id":1},"user":{"id":1}}';
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
            def guildLeader = new GuildLeader()
            guildLeader.validate()
            controller.update(guildLeader)

        then:"The edit view is rendered again with the invalid instance"
            response.status == UNPROCESSABLE_ENTITY.value()
            response.json.errors.size() == 4;

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)

            guildLeader = new GuildLeader(params);
            guildLeader.type.save();
            guildLeader.guild.save();
            guildLeader.user.save();
            guildLeader.save(flush:true);

            controller.update(guildLeader)

        then:"A redirect is issued to the show action"
            guildLeader != null
            response.status == OK.value()
            response.json.id == guildLeader.id
    }

    void "Test that the delete action deletes an instance if it exists"() {

        when:"The delete action is called for a null instance"
            request.contentType = JSON_CONTENT_TYPE;
            request.method = 'DELETE';
            controller.delete(null);

        then:"A 404 is returned"
            response.status == NOT_FOUND.value();

        when:"A domain instance is created"
            response.reset();
            populateValidParams(params);

            def guildLeader = new GuildLeader(params);
            guildLeader.type.save();
            guildLeader.guild.save();
            guildLeader.user.save();
            guildLeader.save(flush:true);

        then:"It exists"
            GuildLeader.count() == 1;

        when:"The domain instance is passed to the delete action"
            controller.delete(guildLeader);

        then:"The instance is deleted"
            GuildLeader.count() == 0;
            response.status == NO_CONTENT.value();
    }
}