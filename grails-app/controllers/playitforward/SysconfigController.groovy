package playitforward

import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Transactional(readOnly = true)
class SysconfigController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    @Secured('ROLE_ADMIN')
    def index(Integer max) {
        respond Sysconfig.list(params), model:[sysconfigCount: Sysconfig.count()]
    }
}
