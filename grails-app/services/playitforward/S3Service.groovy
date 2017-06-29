package playitforward

import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import grails.plugin.awssdk.s3.AmazonS3Service
import grails.transaction.Transactional

@Transactional
class S3Service {

    AmazonS3Service amazonS3Service
    def grailsApplication

    void uploadBase64FormData(String path, String base64String, int maxAge=365*24*60*60) {

        String[] data = base64String.split(",", 2);
        String contentType = data[0].substring(data[0].indexOf("data:") + 5);
        contentType = contentType.substring(0, contentType.indexOf(';'));

        File tmp = File.createTempFile("PiFUpload", "tmp");
        FileOutputStream fos = new FileOutputStream(tmp);

        byte[] imageBytes = Base64.decoder.decode(data[1]);
        fos.write(imageBytes);

        uploadFile(path, tmp, contentType, maxAge);
    }

    void uploadFile(String path, File file, String contentType, int maxAge=365*24*60*60) {

        def bucket = grailsApplication.config.getProperty('bucket') as String;

        try {

            InputStream stream = new FileInputStream(file);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.size());
            metadata.setContentType(contentType);
            metadata.setCacheControl("public, max-age=31536000");

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, path, stream, metadata);
            println "Saving bucket: " + bucket + " path: " + path + " file: " + file.name + " type: " + contentType;
            amazonS3Service.client.putObject(putObjectRequest);

            file.delete();

        } catch (Exception exception) {

            log.warn 'An exception was caught while storing file', exception;
            throw exception;
        }
    }

    File downloadFile(String path, File file) {

        def bucket = grailsApplication.config.getProperty('bucket') as String;
        amazonS3Service.getFile(bucket, path, file.getAbsolutePath());
    }
}
