package playitforward

class EventWinner {

    Event event;
    String name;
    Integer ranking;
    String record;
    String blurb;
    String imageName;

    static constraints = {

        event nullable: false;
        name blank: false;
        ranking nullable: false;
        record nullable: false;
        blurb nullable: false;
        imageName nullable: true;
    }

    static mapping = {

        table 'event_winner';
        id generator: 'native', params: [sequence: 'event_winner_seq'];
        imageName column: 'image_name';
        blurb type: 'text';
        event index: 'event_winner_event_idx', fetch: 'join', lazy: false, unique: true;
    }
}
