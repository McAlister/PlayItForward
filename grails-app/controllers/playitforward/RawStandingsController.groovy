package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class RawStandingsController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = []

    def index(Integer max) {
        respond RawStandings.list(), model:[rawStandingsCount: RawStandings.count()]
    }

    def show(RawStandings rawStandings) {
        respond rawStandings
    }
}
