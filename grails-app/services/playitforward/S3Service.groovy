package playitforward

import com.amazonaws.services.s3.model.PutObjectRequest
import grails.plugin.awssdk.s3.AmazonS3Service
import grails.transaction.Transactional

@Transactional
class S3Service {

    AmazonS3Service amazonS3Service
    def grailsApplication

    void uploadFile(String path, File file, int maxAge=365*24*60*60) {
        def bucket = grailsApplication.config.getProperty('bucket') as String
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, file)
            putObjectRequest.putCustomRequestHeader(
                    'Cache-Control', "max-age=${maxAge}")
            amazonS3Service.client.putObject(putObjectRequest)
        } catch (Exception exception) {
            log.warn 'An exception was caught while storing file', exception
            throw exception
        }
    }

    File downloadFile(String path, File file) {
        def bucket = grailsApplication.config.getProperty('bucket') as String
        amazonS3Service.getFile(bucket, path, file.getAbsolutePath())
    }
}
