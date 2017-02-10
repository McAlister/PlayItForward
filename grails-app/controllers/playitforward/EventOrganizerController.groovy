package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventOrganizerController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EventOrganizer.list(params), model:[eventOrganizerCount: EventOrganizer.count()]
    }

    def show(EventOrganizer eventOrganizer) {
        respond eventOrganizer
    }

    @Transactional
    def save(EventOrganizer eventOrganizer) {
        if (eventOrganizer == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventOrganizer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventOrganizer.errors, view:'create'
            return
        }

        eventOrganizer.save flush:true

        respond eventOrganizer, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(EventOrganizer eventOrganizer) {
        if (eventOrganizer == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventOrganizer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventOrganizer.errors, view:'edit'
            return
        }

        eventOrganizer.save flush:true

        respond eventOrganizer, [status: OK, view:"show"]
    }

    @Transactional
    def delete(EventOrganizer eventOrganizer) {

        if (eventOrganizer == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        eventOrganizer.delete flush:true

        render status: NO_CONTENT
    }
}
