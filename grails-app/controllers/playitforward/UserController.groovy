package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class UserController {
	static responseFormats = ['json', 'xml']
    static allowedMethods = []

    def index() {
    }

    def sendResetEmail(String username) {
        def user = User.findByUsername(username)
        if (user != null) {
            System.console().println("resetting")
            user.makeResetKey()
            user.save(flush: true)
        }
        respond true
    }
}
