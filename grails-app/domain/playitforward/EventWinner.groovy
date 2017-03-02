package playitforward

class EventWinner {

    Event event;
    String name;
    Integer ranking;
    String record;
    String blurb;

    static constraints = {

        event nullable: false;
        name blank: false;
        ranking nullable: false;
        record nullable: false;
        blurb nullable: false;
    }

    static mapping = {

        table 'event_winner';
        blurb type: 'text';
        event index: 'event_winner_event_idx', fetch: 'join', lazy: false, unique: true;
    }
}
