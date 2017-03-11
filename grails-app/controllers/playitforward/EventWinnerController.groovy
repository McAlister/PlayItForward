package playitforward

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventWinnerController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        respond EventWinner.list(), model:[eventWinnerCount: EventWinner.count()]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def save(EventWinner winner) {

        if (winner == null) {

            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (winner.hasErrors()) {

            transactionStatus.setRollbackOnly()
            respond winner.errors, view:'create'
            return
        }

        winner.save flush:true;
        respond winner, [status: CREATED, view:"show"];
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def update(EventWinner winner) {

        if (winner == null) {

            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (winner.hasErrors()) {

            transactionStatus.setRollbackOnly()
            respond winner.errors, view:'edit'
            return
        }

        winner.save flush:true;
        respond winner, [status: OK, view:"show"];
    }
}
