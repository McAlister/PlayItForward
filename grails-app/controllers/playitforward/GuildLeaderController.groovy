package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class GuildLeaderController
{

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"];

    def index(Integer max) {

        respond GuildLeader.listOrderByName(), model:[groupLeaderCount: GuildLeader.count()];
    }

    def show(GuildLeader groupLeader) {

        respond groupLeader;
    }

    @Transactional
    def save(GuildLeader groupLeader) {

        if (groupLeader == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (groupLeader.hasErrors()) {

            transactionStatus.setRollbackOnly();
            respond groupLeader.errors, view:'create';
            return;
        }

        groupLeader.save flush:true;
        respond groupLeader, [status: CREATED, view:"show"];
    }

    @Transactional
    def update(GuildLeader groupLeader) {

        if (groupLeader == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (groupLeader.hasErrors()) {

            transactionStatus.setRollbackOnly();
            respond groupLeader.errors, view:'edit';
            return;
        }

        groupLeader.save flush:true;
        respond groupLeader, [status: OK, view:"show"];
    }

    @Transactional
    def delete(GuildLeader groupLeader) {

        if (groupLeader == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        groupLeader.delete flush:true;
        render status: NO_CONTENT;
    }
}
