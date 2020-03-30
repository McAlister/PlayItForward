package playitforward

import java.sql.Date

class Event {

    EventOrganizer organizer;
    String name;
    EventType type;
    Date startDate;
    Date endDate;
    String eventFormat;
    String eventUrl;

    static hasMany = [linkSet: EventLinks];

    static constraints = {

        organizer nullable: false;
        name blank: false;
        type nullable: false;
        startDate nullable: false;
        endDate nullable: false;
        eventFormat nullable: true;
        eventUrl nullable: true;
    }

    static mapping = {

        id generator: 'native', params: [sequence: 'event_seq'];
        organizer index: 'event_organizer_idx';
        startDate column: 'start_date', index: 'event_start_idx';
        endDate column: 'end_date';
        eventFormat column: 'format', index: 'event_format_idx';
        eventUrl column: 'event_url';
    }

    static belongsTo = [EventOrganizer, EventType];
}
