package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class UserController {
	static responseFormats = ['json', 'xml']
    static allowedMethods = []
    def emailService

    def index() {
    }

    def sendResetEmail(String username) {
        def user = User.findByUsername(username)
        if (user != null) {
            user.makeResetKey()
            emailService.sendPasswordReset(user)
            user.save(flush: true)
        }
        respond true
    }
}
