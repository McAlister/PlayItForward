package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class GuildLeaderController
{

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"];

    def index(Integer max) {

        respond GuildLeader.listOrderByName(), model:[guildLeaderCount: GuildLeader.count()];
    }

    def show(GuildLeader guildLeader) {

        respond guildLeader;
    }

    @Transactional
    def save(GuildLeader guildLeader) {

        if (guildLeader == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (guildLeader.hasErrors()) {

            transactionStatus.setRollbackOnly();
            respond guildLeader.errors, view:'create';
            return;
        }

        guildLeader.save flush:true;
        respond guildLeader, [status: CREATED, view:"show"];
    }

    @Transactional
    def update(GuildLeader guildLeader) {

        if (guildLeader == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        if (guildLeader.hasErrors()) {

            transactionStatus.setRollbackOnly();
            respond guildLeader.errors, view:'edit';
            return;
        }

        guildLeader.save flush:true;
        respond guildLeader, [status: OK, view:"show"];
    }

    @Transactional
    def delete(GuildLeader guildLeader) {

        if (guildLeader == null) {

            transactionStatus.setRollbackOnly();
            render status: NOT_FOUND;
            return;
        }

        guildLeader.delete flush:true;
        render status: NO_CONTENT;
    }
}
