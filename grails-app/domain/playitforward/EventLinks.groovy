package playitforward

class EventLinks {

    Event event;
    int round;
    String pairingsUrl;
    String resultsUrl;
    String standingsUrl;

    static constraints = {

        event nullable: false;
        round nullable: false;
    }

    static mapping = {

        table 'event_links';
        id generator: 'native', params: [sequence: 'event_links_seq'];
        event index: 'event_link_event_idx';
        event unique: ['round'];
    }

    static belongsTo = [Event];
}
