package playitforward

import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder
import grails.core.support.GrailsConfigurationAware
import grails.plugin.awssdk.ses.AmazonSESService
import grails.config.Config
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.Body
import com.amazonaws.services.simpleemail.model.Content
import com.amazonaws.services.simpleemail.model.Destination
import com.amazonaws.services.simpleemail.model.Message
import com.amazonaws.services.simpleemail.model.SendEmailRequest
import com.amazonaws.services.simpleemail.model.SendEmailResult

import grails.transaction.Transactional
import grails.util.Environment

@Transactional
class EmailService implements GrailsConfigurationAware {

    AmazonSESService amazonSESService;
    AwsCredentialsProviderService awsCredentialsProviderService;
    static sender = 'playitforward@playitforward-magic.org';
    def awsRegion;
    def sourceEmail;

    @Override
    void setConfiguration(Config co) {

        this.awsRegion = co.getProperty('grails.plugin.awssdk.region')
        if (!this.awsRegion) {
            throw new IllegalStateException('\'grails.plugin.awssdk.region not set')
        }

        this.sourceEmail = co.getProperty('grails.plugin.awssdk.ses.sender')
        if (!this.sourceEmail) {
            throw new IllegalStateException('grails.plugin.awssdk.ses.sender not set')
        }
    }

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

    def sendClaimEmail(String name, String email, String contest, String content) {

        if ( !awsCredentialsProviderService ) {
            log.warn("AWS Credentials provider not configured");
            return;
        }

        String msg = content.replaceAll("(\r\n|\n)", "<br />");
        msg = "<div>" + msg;
        msg += "<br /><br /><p>From: " + name + "</p>";
        msg += "<p>Email: " + email + "</p></div>";

        Destination destination = new Destination().withToAddresses("play_it_forward@outlook.com");
        Content subject = new Content().withData(contest);
        Body body = new Body().withHtml(new Content().withData(msg));
        Message message = new Message().withSubject(subject).withBody(body);

        SendEmailRequest request = new SendEmailRequest()
                .withSource(sender)
                .withDestination(destination)
                .withMessage(message);

        try {
            log.info("Attempting to send an email for ${email} about ${contest} with ${content} ...");

            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                    .withCredentials(awsCredentialsProviderService)
                    .withRegion(awsRegion)
                    .build();

            SendEmailResult sendEmailResult = client.sendEmail(request)
            log.info("Email sent! {}", sendEmailResult.toString())

        } catch (Exception ex) {
            log.warn("The email was not sent.")
            log.warn("Error message: {}", ex.message)
        }
    }
}
