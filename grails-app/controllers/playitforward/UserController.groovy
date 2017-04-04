package playitforward

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ADMIN'])
class UserController {
	static responseFormats = ['json', 'xml']
    static allowedMethods = []
    def emailService

    def index() {
    }

    @Secured(['ROLE_ANONYMOUS', 'ROLE_ADMIN'])
    def sendResetEmail(String username) {
        def user = User.findByUsername(username)
        if (user != null) {
            user.makeResetKey()
            emailService.sendPasswordReset(user)
            user.save(flush: true)
        }
        respond([message: 'email sent if user exists'])
    }

    @Secured(['ROLE_ANONYMOUS', 'ROLE_ADMIN'])
    def setNewPassword(String username) {
        def password = params.password
        def resetKey = params.key
        def user = User.findByUsername(username)
        if (user.resetKey != resetKey) {
            return respond([status: 403], [message: 'Incorrect key'])
        }
        user.password = password
        user.resetKey = null
        user.save(flush: true)
        respond([message: 'success'])
    }
}
