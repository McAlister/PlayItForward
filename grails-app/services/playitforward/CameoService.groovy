package playitforward

import grails.transaction.Transactional
import playitforward.cameo.Cameo

@Transactional
class CameoService {

    def makeCameo(File input, File output) {
        new Cameo(input).makeCameo(output)
        return output
    }
}
