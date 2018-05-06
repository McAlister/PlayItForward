package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventStandingController {

    def sessionFactory;

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    static defaultNames = [ "Arlinn", "Avacyn", "Chandra", "Elspeth", "Kaya",
                                     "Kemba", "Kiora", "Liliana", "Mirri", "Nahiri",
                                     "Narset", "Nissa", "Olivia", "Oona", "Pia", "Saheeli",
                                     "SliverQueen", "Tamiyo", "Vraska", "Wort"
    ];

    def latestRound(Integer eventId) {

        final session = sessionFactory.currentSession;
        final String sql = "Select max(round) from raw_standings where event_id = " + eventId;
        def query = session.createSQLQuery(sql);
        List<Object[]> rows = query.list();

        def lastRound = rows[0] == null ? 0 : rows[0].toString() as Integer
        respond(['latestRound': lastRound], view:'latestRound')
    }

    def index(Integer eventId, Integer roundNum) {

        final session = sessionFactory.currentSession;
        final String sql = "Insert Into event_standing (id, version, event_id, player_id, points, round, rank )\n" +
                "Select nextval('hibernate_sequence'), 0, r.event_id, p.id, r.points, r.round, r.rank\n" +
                "From Player p inner join raw_standings r On (p.name = r.name OR (substring(p.name, 0, length(p.name) - 4 )  = r.name))\n" +
                "Where r.event_id = " + eventId + "\n" +
                "And r.round = " + roundNum + "\n" +
                "And not exists (Select * from event_standing " +
                "                Where event_id = r.event_id " +
                "                   And round = r.round " +
                "                   And player_id = p.id)";

        def query = session.createSQLQuery(sql);
        query.executeUpdate();

        List<EventStanding> standingList = EventStanding.findAll(sort: 'player', order: 'asc') {
            event.id == eventId && round == roundNum && player.isWoman == true
        };

        standingList.sort { a, b ->
            b.points <=> a.points;
        }

        Iterator<EventStanding> iter = standingList.iterator();
        while (iter.hasNext()) {
            EventStanding standing = iter.next();
            if(!standing.canMakeDayTwo()) {
                iter.remove();
            }
        }

        standingList.sort { a, b ->
            a.player.id <=> b.player.id;
        }

        for(EventStanding eventStanding : standingList) {
           if (eventStanding.player.alias == null) {
               int index = eventStanding.player.id % 20;
               eventStanding.player.alias = defaultNames[index];
               eventStanding.player.imgUrl = defaultNames[index] + ".png";
           }
        }

        respond standingList, model:[eventStandingCount: standingList.size()];
    }

    def show(EventStanding eventStanding) {
        respond eventStanding
    }
}
