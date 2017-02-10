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
        event index: 'event_idx';
    }
}
