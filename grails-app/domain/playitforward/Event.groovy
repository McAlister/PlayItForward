package playitforward

import java.sql.Date

class Event {

    EventOrganizer organizer;
    String name;
    EventType type;
    String eventCode;
    Date startDate;
    Date endDate;

    static constraints = {

        organizer nullable: false;
        name blank: false;
        type nullable: false;
        eventCode nullable: true;
        startDate nullable: false;
        endDate nullable: false;
    }

    static mapping = {

        organizer index: 'event_organizer_idx';
        eventCode column: 'event_code';
        startDate column: 'start_date', index: 'event_start_idx';
        endDate column: 'end_date';
    }

    static belongsTo = [EventOrganizer, EventType];
}
