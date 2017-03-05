package playitforward

class EventStanding {

    Event event;
    Player player;
    int round;
    int points;

    static constraints = {

        event nullable: false;
        player nullable: false;
        round nullable: false;
        points nullable: false;
    }

    static mapping = {

        table 'event_standing';
        player index: 'event_standing_player_idx', fetch: 'join', lazy: false;
        event index: 'event_standing_event_idx', fetch: 'join', lazy: false;
        round index: 'event_standing_event_idx';
        event unique: ['round', 'player'];
    }
}
