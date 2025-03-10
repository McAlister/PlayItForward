package playitforward

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class UserController {

	static responseFormats = ['json', 'xml'];
    static allowedMethods = [];
    def emailService;

    def index() {
        // Empty Block
    }

    @Secured(['permitAll'])
    def sendResetEmail(String username) {

        username = username.toLowerCase();
        def user = User.findByUsername(username);

        if (user != null) {

            user.makeResetKey();
            emailService.sendPasswordReset(user);
            user.save(flush: true);
        }

        respond([message: 'email sent if user exists']);
    }

    @Secured(['permitAll'])
    def setNewPassword(String username) {

        username = username.toLowerCase();
        def password = params.password ?: request.JSON?.password;
        def resetKey = params.key ?: request.JSON?.key;
        def user = User.findByUsername(username);

        if (user.resetKey != resetKey) {
            return respond([status: 403], [message: 'Incorrect key, be sure to use the latest reset email']);
        }

        user.password = password;
        user.resetKey = null;
        user.save(flush: true);
        respond([message: 'success']);
    }
}
