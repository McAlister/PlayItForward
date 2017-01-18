package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class PersonTypeController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = []

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond PersonType.list(params), model:[personTypeCount: PersonType.count()]
    }

    def show(PersonType personType) {
        respond personType
    }
}
