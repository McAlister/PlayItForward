package playitforward

import grails.plugin.springsecurity.annotation.Secured
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ArtistController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        respond Artist.list().sort{it.name}, model:[artistCount: Artist.count()]
    }

    def show(Artist artist) {
        respond artist
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def save(Artist artist) {
        if (artist == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (artist.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond artist.errors, view:'create'
            return
        }

        artist.save flush:true

        respond artist, [status: CREATED, view:"show"]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def update(Artist artist) {
        if (artist == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (artist.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond artist.errors, view:'edit'
            return
        }

        artist.save flush:true

        respond artist, [status: OK, view:"show"]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def delete(Artist artist) {

        if (artist == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        artist.delete flush:true

        render status: NO_CONTENT
    }
}
