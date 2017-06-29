package playitforward

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional
import org.apache.commons.io.IOUtils

class ImageController {

    CameoService cameoService;
    S3Service s3Service;
    static responseFormats = ['json', 'xml'];

    def index() {

        // Empty Block
    }

    @Secured(['permitAll'])
    def generateCameo() {

        File input = File.createTempFile("cameo-upload", ".png");
        File output = File.createTempFile("cameo-output", ".png");

        try {

            IOUtils.copy(request.inputStream, new FileOutputStream(input));
            println "Input:${input} Output:${output}";
            cameoService.makeCameo(input, output);

            render file: new FileInputStream(output), contentType: 'image/png';
        }
        finally {

            input.delete();
            output.delete();
        }
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def save() {

        String imgStr = request.getParameter("file");
        String name = request.getParameter("name");
        String path = request.getParameter("path");
        path = path + File.separator + name;

        s3Service.uploadBase64FormData(path, imgStr);
        println("Uploaded: " + path);

        respond ''
    }
}
