package playitforward

class RawStandings {

    Event event;
    String name;
    int round;
    int rank;
    int points;
    double opponentMatchWin;
    boolean isWoman;

    static constraints = {
        event nullable: false;
        name blank: false;
        round nullable: false;
        rank nullable: false;
        points nullable: false;
        opponentMatchWin nullable: false;
        isWoman nullable: false;
    }

    static mapping = {

        table 'raw_standings';
        id generator: 'native', params: [sequence: 'raw_standings_seq'];
        event index: 'raw_standings_event_idx', fetch: 'join', lazy: false;
        round index: 'raw_standings_event_idx';
        rank index: 'raw_standings_event_idx';
        isWoman index: 'raw_standings_event_idx';
        event unique: ['round', 'name', 'rank'];
    }

    public boolean canMakeDayTwo()
    {
        if (round >= 8) {
            return (points >= 18);
        }

        int wins = points / 3;
        int draws = points % 3;
        int losses = round - wins - draws;

        return (draws + losses < 3);
    }
}
