package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventWinnerController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        respond EventWinner.list(), model:[eventWinnerCount: EventWinner.count()]
    }

    def show(EventWinner eventWinner) {
        respond eventWinner
    }
}
