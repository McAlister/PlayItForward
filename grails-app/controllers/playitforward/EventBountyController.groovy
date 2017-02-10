package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventBountyController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond EventBounty.list(params), model:[eventBountyCount: EventBounty.count()]
    }

    def show(EventBounty eventBounty) {
        respond eventBounty
    }

    @Transactional
    def save(EventBounty eventBounty) {
        if (eventBounty == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventBounty.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventBounty.errors, view:'create'
            return
        }

        eventBounty.save flush:true

        respond eventBounty, [status: CREATED, view:"show"]
    }

    @Transactional
    def update(EventBounty eventBounty) {
        if (eventBounty == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventBounty.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventBounty.errors, view:'edit'
            return
        }

        eventBounty.save flush:true

        respond eventBounty, [status: OK, view:"show"]
    }

    @Transactional
    def delete(EventBounty eventBounty) {

        if (eventBounty == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        eventBounty.delete flush:true

        render status: NO_CONTENT
    }
}
