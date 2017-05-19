package playitforward

import grails.test.mixin.TestFor
import spock.lang.Specification
import grails.test.mixin.*
import grails.test.mixin.domain.DomainClassUnitTestMixin

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(UserController)
@Mock(User)
@TestMixin(DomainClassUnitTestMixin)
class UserControllerSpec extends Specification {

    EmailService mockEmailService = Mock();

    def setup() {

        controller.setEmailService(mockEmailService);
    }

    def cleanup() {

        // Empty Block
    }

    void "test set new password"() {

        mockDomain(User, [
                [username: "saiken@ulfheim.net", password: "testing", resetKey: "test key"]
        ]);

        when:"Call set new password with no reset key"
            controller.setNewPassword("saiken@ulfheim.net");

        then:"wrong reset key error"
            response.status == 403;

        when:"correct reset key sent"
            response.reset();
            controller.params.key = "test key";
            controller.params.password = "explorerReporting";
            controller.setNewPassword("saiken@ulfheim.net");
            controller.params.clear();

        then:"Success"
            response.status == 200;
            response.text == "{\"message\":\"success\"}";
            User user = User.findByUsername("saiken@ulfheim.net");
            user.password.equals("explorerReporting");
    }

    void "test user does not exist"() {

        when:"Call send reset with imaginary user"
            controller.sendResetEmail("saiken@ulfheim.net");

        then:"no op"
            response.status == 200;
            response.text == "{\"message\":\"email sent if user exists\"}";
    }

    void "test user exists"() {

        mockDomain(User, [
                [username: "saiken@ulfheim.net", password: "testing"]
        ]);

        when:"Call reset with real user"
            controller.sendResetEmail("saiken@ulfheim.net");
            User user = User.findByUsername("saiken@ulfheim.net");

        then:"no op"
            1 * mockEmailService.sendPasswordReset(_ as User);
            response.status == 200;
            user.resetKey != null;
            response.text == "{\"message\":\"email sent if user exists\"}";
    }
}
