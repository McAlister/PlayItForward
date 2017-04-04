package playitforward

import grails.plugin.awssdk.ses.AmazonSESService
import grails.transaction.Transactional

@Transactional
class EmailService {

    AmazonSESService amazonSESService
    static sender = 'playitforward@playitforward-magic.org'

    def sendPasswordReset(User user) {
        def to = user.username
        def subject = 'Change your playitforward password'
        def body = '<p>Hello ${to.encodeAsHTML()}!' +
                '<p>Please click <a href="http://www.playitforward-magic.org/#/setPass' +
                '?user=${to.encodeAsURL()}&resetKey=${user.resetKey.encodeAsUrl()}">' +
                'this link</a> to set or change your password.'

        int result = amazonSESService.send(to, subject, body, sender)
        System.console().println("Sent password reset email to ${to} with key ${user.resetKey} and result ${result}")
        return result
    }
}
