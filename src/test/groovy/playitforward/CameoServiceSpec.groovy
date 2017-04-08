package playitforward

import grails.test.mixin.TestFor
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CameoService)
class CameoServiceSpec extends Specification {

    def random = new Random()
    Path input = Paths.get('grails-app/assets/images/race/Wort.png')
    Path output = Paths.get(System.getProperty('java.io.tmpdir') + '/out-' + random.nextInt() + '.png')

    def setup() {
    }

    def cleanup() {
        Files.delete(output)
    }

    void "test cameo"() {

        when:"running the service"
        service.makeCameo(input.toFile(), output.toFile())
        println "Created ${input} -> ${output}"

        then:"file is created"
        Files.exists(output)
    }
}
