package playitforward

import grails.test.mixin.TestFor
import org.grails.plugins.testing.GrailsMockMultipartFile
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.web.ControllerUnitTestMixin} for usage instructions
 */
@TestFor(ImageController)
class ImageControllerSpec extends Specification {

    CameoService mockCameo = Mock();

    def setup() {

        controller.setCameoService(mockCameo);
    }

    def cleanup() {

        // Empty Block
    }

    void "test generateCameo"() {

        given:
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("images/Zyla.png").getFile());
            byte[] input = file.getBytes();

            def multipartFile = new GrailsMockMultipartFile('inputStream',
                    'Zyla.png', 'image/jpeg', input);
            request.addFile(multipartFile);

        when:"calling generateCameo()"
            controller.generateCameo();

        then:"Cameo Made"
            1 * mockCameo.makeCameo(_ as File, _ as File);
            response.status == 200;
    }

    void "noop index"() {

        when:"calling index"
            controller.index();

        then:"noop"
            true;
    }
}
