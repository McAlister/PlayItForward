package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventTypeController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EventType.list(params), model:[eventTypeCount: EventType.count()]
    }

    def show(EventType eventType) {
        respond eventType
    }

    @Transactional
    def save(EventType eventType) {
        if (eventType == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventType.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventType.errors, view:'create'
            return
        }

        eventType.save flush:true

        respond eventType, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(EventType eventType) {
        if (eventType == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventType.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventType.errors, view:'edit'
            return
        }

        eventType.save flush:true

        respond eventType, [status: OK, view:"show"]
    }

    @Transactional
    def delete(EventType eventType) {

        if (eventType == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        eventType.delete flush:true

        render status: NO_CONTENT
    }
}
