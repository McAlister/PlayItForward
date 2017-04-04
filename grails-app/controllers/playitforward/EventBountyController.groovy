package playitforward

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventBountyController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        respond EventBounty.list(), model:[eventBountyCount: EventBounty.count()]
    }

    def show(EventBounty eventBounty) {
        respond eventBounty
    }

    @Secured('ROLE_ADMIN')
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

    @Secured('ROLE_ADMIN')
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

    @Secured('ROLE_ADMIN')
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
