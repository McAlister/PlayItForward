package playitforward

class EventBounty {

    Event event;
    String donor;
    String prize;

    static constraints = {
        event nullable: false;
        donor blank: false;
        prize blank: false;
    }

    static mapping = {

        table 'event_bounty';
        id generator: 'native', params: [sequence: 'event_bounty_seq'];
        event index: 'event_idx', fetch: 'join', lazy: false;
    }
}
