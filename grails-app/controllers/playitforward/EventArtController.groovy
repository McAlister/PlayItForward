package playitforward

import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventArtController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        respond EventArt.list().sort{it.title}, model:[eventArtCount: EventArt.count()]
    }

    def show(EventArt eventArt) {
        respond eventArt
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def save(EventArt eventArt) {
        if (eventArt == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventArt.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventArt.errors, view:'create'
            return
        }

        eventArt.save flush:true

        respond eventArt, [status: CREATED, view:"show"]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def update(EventArt eventArt) {
        if (eventArt == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (eventArt.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond eventArt.errors, view:'edit'
            return
        }

        eventArt.save flush:true

        respond eventArt, [status: OK, view:"show"]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def delete(EventArt eventArt) {

        if (eventArt == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        eventArt.delete flush:true

        render status: NO_CONTENT
    }
}
