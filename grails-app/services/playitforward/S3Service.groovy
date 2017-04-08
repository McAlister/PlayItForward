package playitforward

import grails.plugin.awssdk.s3.AmazonS3Service
import grails.transaction.Transactional

@Transactional
class S3Service {

    AmazonS3Service amazonS3Service
    def grailsApplication

    def uploadFile(String path, File file) {
        def bucket = grailsApplication.config.getProperty('bucket') as String
        amazonS3Service.storeFile(bucket, path, file)
    }

    def downloadFile(String path, File file) {
        def bucket = grailsApplication.config.getProperty('bucket') as String
        amazonS3Service.getFile(bucket, path, file.getAbsolutePath())
    }
}
