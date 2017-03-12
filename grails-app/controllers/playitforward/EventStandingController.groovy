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

        Map<String, Integer> args = new HashMap<>();

        System.console().println("Args: " + rows.toString());

        args.put("latestRound", Integer.parseInt(rows.get(0).toString()));

        System.console().println("Args: " + args.toString());
        respond args, view:'latestRound';
    }

    def index(Integer eventId, Integer roundNum) {

        List<EventStanding> standingList = EventStanding.findAll(sort: 'player', order: 'asc') {
            event.id == eventId && round == roundNum
        };

        if (standingList.size() == 0) {

            // populate event standings for this round;

            final session = sessionFactory.currentSession;
            final String sql = "Insert Into event_standing (id, version, event_id, player_id, points, round )\n" +
                    "Select nextval('hibernate_sequence'), 0, r.event_id, p.id, r.points, r.round\n" +
                    "From Player p inner join raw_standings r On p.name = r.name\n" +
                    "Where r.event_id = " + eventId + "\n" +
                    "And r.round = " + roundNum;

            def query = session.createSQLQuery(sql);
            query.executeUpdate();

            standingList = EventStanding.findAll(sort: 'player', order: 'asc') {
                event.id == eventId && round == roundNum
            };
        }

        standingList.sort { a, b ->
            b.points <=> a.points;
        }

        if(roundNum > 9) {

            // Day 2. Remove people who did not make it
            Iterator<EventStanding> iter = standingList.iterator();
            while (iter.hasNext()) {
                EventStanding standing = iter.next();
                if(standing.points < 18) {
                    iter.remove();
                }
            }
        }
        else {

            // Day 1.  Remove people who cannot make day 2.
            Iterator<EventStanding> iter = standingList.iterator();
            while (iter.hasNext()) {
                EventStanding standing = iter.next();
                if(!standing.canMakeDayTwo()) {
                    iter.remove();
                }
            }
        }

        while (standingList.size() > 20) {
            standingList.pop();
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
