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
        respond Guild.listOrderByName(), model:[groupCount: Guild.count()]
    }

    @Secured('permitAll')
    def show(Guild group) {
        respond group
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def save(Guild group) {
        if (group == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (group.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond group.errors, view:'create'
            return
        }

        group.save flush:true

        respond group, [status: CREATED, view:"show"]
    }

    @Secured(['ROLE_ADMIN', 'ROLE_GROUP_LEADER'])
    @Transactional
    def update(Guild group) {
        if (group == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        if (group.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond group.errors, view:'edit'
            return
        }

        group.save flush:true

        respond group, [status: OK, view:"show"]
    }

    @Secured('ROLE_ADMIN')
    @Transactional
    def delete(Guild group) {

        if (group == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        group.delete flush:true

        render status: NO_CONTENT
    }
}
