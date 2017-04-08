package playitforward

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional()
class PersonController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT"]

    @Secured('ROLE_ADMIN')
    def index(Integer max) {
        respond Person.list(), model:[personCount: Person.count()]
    }

    def show(Person person) {
        respond person;
    }

    @Secured('permitAll')
    @Transactional
    def save(Person person) {

        if (person == null) {

            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (person.hasErrors()) {

            transactionStatus.setRollbackOnly()
            respond person.errors, view:'create'
            return
        }

        person.save flush:true;
        respond person, [status: CREATED, view:"show"];
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def update(Person person) {

        if (person == null) {

            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (person.hasErrors()) {

            transactionStatus.setRollbackOnly()
            respond person.errors, view:'edit'
            return
        }

        person.save flush:true;
        respond person, [status: OK, view:"show"];
    }
}
