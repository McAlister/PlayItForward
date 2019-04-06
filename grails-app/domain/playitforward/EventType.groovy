package playitforward

class EventType {

    String type;
    String description;

    static constraints = {

        type blank: false, unique: true;
        description blank: false;
    }

    static mapping = {

        table 'event_type';
        id generator: 'native', params: [sequence: 'event_type_seq'];
    }
}
