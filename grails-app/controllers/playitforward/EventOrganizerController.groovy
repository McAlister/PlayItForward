package playitforward

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventOrganizerController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"];

    def index(Integer max) {

        respond EventOrganizer.list(), model:[eventOrganizerCount: EventOrganizer.count()];
    }

    def show(EventOrganizer eventOrganizer) {
        respond eventOrganizer;
    }

    @Transactional
    @Secured('ROLE_ADMIN')
    def save(EventOrganizer eventOrganizer) {

        if (eventOrganizer == null) {
            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (eventOrganizer.hasErrors()) {
            transactionStatus.setRollbackOnly();
            respond eventOrganizer.errors, view:'create';
            return;
        }

        eventOrganizer.save flush:true;
        respond eventOrganizer, [status: CREATED, view:"show"];
    }

    @Transactional
    @Secured('ROLE_ADMIN')
    def update(EventOrganizer eventOrganizer) {

        if (eventOrganizer == null) {
            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (eventOrganizer.hasErrors()) {
            transactionStatus.setRollbackOnly();
            respond eventOrganizer.errors, view:'edit';
            return;
        }

        eventOrganizer.save flush:true;
        respond eventOrganizer, [status: OK, view:"show"];
    }

    @Transactional
    @Secured('ROLE_ADMIN')
    def delete(EventOrganizer eventOrganizer) {

        if (eventOrganizer == null) {
            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        eventOrganizer.delete flush:true;
        render status: NO_CONTENT;
    }
}
