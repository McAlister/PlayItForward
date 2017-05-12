package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventTypeController {

    static responseFormats = ['json', 'xml'];

    static allowedMethods = [];

    def index(Integer max) {
        respond EventType.listOrderById(), model:[eventTypeCount: EventType.count()]
    }

    def show(EventType eventType) {
        respond eventType
    }
}
