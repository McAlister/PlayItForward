package playitforward

class EventArt {

    Event event;
    Artist artist;
    String title;

    static constraints = {

        event nullable: false, unique: true;
        artist nullable: false;
        title nullable: false;
    }

    static mapping = {

        table 'event_art';
        id generator: 'native', params: [sequence: 'event_art_seq'];
        event index: 'event_art_event_idx', fetch: 'join', lazy: false, unique: true;
        artist index: 'event_art_artist_idx', fetch: 'join', lazy: false;
    }
}
