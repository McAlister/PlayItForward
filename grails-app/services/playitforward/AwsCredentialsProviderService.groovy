package playitforward

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.transform.CompileStatic

@CompileStatic
class AwsCredentialsProviderService implements AWSCredentialsProvider, GrailsConfigurationAware {

    String accessKey
    String secretKey

    @Override
    AWSCredentials getCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey)
    }

    @Override
    void refresh() {

    }

    @Override
    void setConfiguration(Config co) {

        this.accessKey = co.getProperty('grails.plugin.awssdk.accessKey', String)
        if (!this.accessKey) {
            throw new IllegalStateException('grails.plugin.awssdk.accessKey not set')
        }

        this.secretKey = co.getProperty('grails.plugin.awssdk.secretKey', String)
        if (!this.secretKey) {
            throw new IllegalStateException('grails.plugin.awssdk.secretKey not set')
        }
    }
}
