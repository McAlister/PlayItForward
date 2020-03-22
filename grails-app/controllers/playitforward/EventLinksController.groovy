package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventLinksController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"];

    def index(Integer max) {

        respond EventLinks.list(params), model:[eventLinksCount: EventLinks.count()];
    }

    def show(EventLinks eventLinks) {

        respond eventLinks;
    }

    def getLinks(long eventId) {

        Event event = Event.findById(eventId);
        def linkList = EventLinks.where {event.id == eventId};

        respond linkList.sort("round", "desc"), model:[eventLinksCount: EventLinks.count()];
    }
}
