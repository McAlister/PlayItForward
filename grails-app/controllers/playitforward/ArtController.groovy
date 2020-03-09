package playitforward

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class ArtController {

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {

        respond Art.listOrderByTitle(), model:[artCount: Art.count()];
    }

    def show(Art art) {
        respond art;
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def save(Art art) {

        if (art == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (art.hasErrors()) {

            transactionStatus.setRollbackOnly();
            respond art.errors, view:'create';
            return;
        }

        art.save flush:true;
        respond art, [status: CREATED, view:"show"];
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def update(Art art) {

        if (art == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (art.hasErrors()) {

            transactionStatus.setRollbackOnly();
            respond art.errors, view:'edit';
            return;
        }

        art.save flush:true;
        respond art, [status: OK, view:"show"];
    }
}
