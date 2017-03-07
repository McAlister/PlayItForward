package playitforward

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SysconfigController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    def index(Integer max) {
        respond Sysconfig.list(params), model:[sysconfigCount: Sysconfig.count()]
    }
}
