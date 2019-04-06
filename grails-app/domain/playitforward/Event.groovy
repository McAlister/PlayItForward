package playitforward

import java.sql.Date

class Event {

    EventOrganizer organizer;
    String name;
    EventType type;
    String eventCode;
    Date startDate;
    Date endDate;
    String eventFormat;
    Art art;
    String coordinator;
    String playmatFileName;
    Integer cfbEventNum;

    static constraints = {

        organizer nullable: false;
        name blank: false;
        type nullable: false;
        eventCode nullable: true;
        startDate nullable: false;
        endDate nullable: false;
        eventFormat nullable: true;
        art nullable: true;
        coordinator nullable: true;
        playmatFileName nullable: true;
        cfbEventNum nullable: true;
    }

    static mapping = {

        id generator: 'native', params: [sequence: 'event_seq'];
        organizer index: 'event_organizer_idx';
        eventCode column: 'event_code';
        startDate column: 'start_date', index: 'event_start_idx';
        endDate column: 'end_date';
        eventFormat column: 'format', index: 'event_format_idx';
        playmatFileName column: 'playmat_file_name';
        cfbEventNum column: 'cfb_event_key';
    }

    static belongsTo = [EventOrganizer, EventType, Art];
}
