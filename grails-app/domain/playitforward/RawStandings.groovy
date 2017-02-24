package playitforward

class RawStandings {

    Event event;
    String name;
    int round;
    int rank;
    int points;
    double opponentMatchWin;

    static constraints = {
        event nullable: false;
        name blank: false;
        round nullable: false;
        rank nullable: false;
        points nullable: false;
        opponentMatchWin nullable: false;
    }

    static mapping = {

        table 'raw_standings';
        event index: 'raw_standings_event_idx', fetch: 'join', lazy: false
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
