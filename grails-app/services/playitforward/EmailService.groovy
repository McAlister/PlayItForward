package playitforward

import grails.plugin.awssdk.ses.AmazonSESService
import grails.transaction.Transactional
import grails.util.Environment

@Transactional
class EmailService {

    AmazonSESService amazonSESService
    static sender = 'playitforward@playitforward-magic.org'

    def sendPasswordReset(User user) {
        def to = user.username
        def subject = 'Set or change your playitforward password'
        def host = Environment.current == Environment.PRODUCTION ? 'www.playitforward-magic.org' : 'localhost:8080'
        def url = "http://${host}/#/setPass?user=${URLEncoder.encode(to, 'utf-8')}" +
                "&resetKey=${URLEncoder.encode(user.resetKey, 'utf-8')}"
        def body = "<p>Hello playitforward user!\n" +
                "<p>Please click <a href='${url}'>this link</a> to set or change your password.\n"

        int result = amazonSESService.send(to, subject, body, sender)
        if (result != 1) {
            throw new RuntimeException("Unable to send mail to ${to}, result: ${result}")
        }
        System.console().println("Sent password reset email to ${to} with key ${user.resetKey}")
    }
}
