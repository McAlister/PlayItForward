package playitforward

class RawStandings {

    Event event;
    String name;
    Integer round;
    Integer rank;
    Integer points;
    Double opponentMatchWin;
    Boolean isWoman;

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
        name index: 'raw_standings_name_idx';
        event index: 'raw_standings_event_idx,raw_standings_name_idx', fetch: 'join', lazy: false;
        round index: 'raw_standings_event_idx';
        rank index: 'raw_standings_event_idx';
        isWoman index: 'raw_standings_event_idx';
        event unique: ['round', 'name', 'rank'];
    }

    boolean canMakeDayTwo()
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
