package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class PersonTypeController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = []

    def index(Integer max) {
        respond PersonType.list(), model:[personTypeCount: PersonType.count()]
    }

    def show(PersonType personType) {
        respond personType
    }
}
