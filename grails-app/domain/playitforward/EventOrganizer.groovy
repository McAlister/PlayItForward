package playitforward

class EventOrganizer {

    String name;
    String url;

    static constraints = {

        name blank: false, unique: true;
        url blank: false, url: true;
    }

    static mapping = {

        table 'event_organizer';
    }
}
