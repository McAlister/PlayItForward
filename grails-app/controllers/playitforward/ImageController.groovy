package playitforward

import grails.plugin.springsecurity.annotation.Secured
import org.apache.commons.io.IOUtils

class ImageController {
    CameoService cameoService
    static responseFormats = ['json', 'xml']

    def index() { }

    @Secured(['permitAll'])
    def generateCameo() {
        File input = File.createTempFile("cameo-upload", ".png")
        File output = File.createTempFile("cameo-output", ".png")
        try {
            IOUtils.copy(request.inputStream, new FileOutputStream(input))
            println "Input:${input} Output:${output}"
            cameoService.makeCameo(input, output)
            render file: new FileInputStream(output), contentType: 'image/png'
        }
        finally {
            input.delete()
            output.delete()
        }
    }
}
