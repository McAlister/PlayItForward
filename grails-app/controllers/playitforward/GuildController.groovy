package playitforward

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class GuildController
{

    static responseFormats = ['json', 'xml']
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Secured('permitAll')
    def index(Integer max) {
        respond Guild.listOrderByName(), model:[guildCount: Guild.count()]
    }

    @Secured('permitAll')
    def show(Guild guild) {
        respond guild
    }

    @Secured('ROLE_GROUP_LEADER')
    def getMyGuild(String username) {

        User user = User.findByUsername(username);
        GuildLeader guildLeader = GuildLeader.findByUser(user);
        respond guildLeader.guild;
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def save(Guild guild) {
        if (guild == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (guild.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond guild.errors, view:'create'
            return
        }

        guild.save flush:true

        respond guild, [status: CREATED, view:"show"]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_GROUP_LEADER'])
    @Transactional
    def update(Guild guild) {
        if (guild == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (guild.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond guild.errors, view:'edit'
            return
        }

        guild.save flush:true

        respond guild, [status: OK, view:"show"]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def delete(Guild guild) {

        if (guild == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        guild.delete flush:true

        render status: NO_CONTENT
    }
}
