package playitforward

class EventStanding {

    Event event;
    Player player;
    int round;
    int points;
    int rank;

    static constraints = {

        event nullable: false;
        player nullable: false;
        round nullable: false;
        points nullable: false;
        // Change to false after deployment and population.
        rank nullable: true;
    }

    static mapping = {

        table 'event_standing';
        player index: 'event_standing_player_idx', fetch: 'join', lazy: false;
        event index: 'event_standing_event_idx', fetch: 'join', lazy: false;
        round index: 'event_standing_event_idx';
        event unique: ['round', 'player'];
    }

    public boolean canMakeDayTwo()
    {
        if (round > 9) {
            return (points >= 18);
        }

        int wins = points / 3;
        int draws = points % 3;
        int losses = round - wins - draws;

        return (draws + losses < 4);
    }
}
