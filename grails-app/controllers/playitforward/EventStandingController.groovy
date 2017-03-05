package playitforward

import grails.transaction.Transactional

@Transactional(readOnly = true)
class EventStandingController {

    def sessionFactory;

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    def index(Integer eventId, Integer roundNum) {

        System.console().println("EventID: " + eventId + " round: " + roundNum);

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
            def updatedRows = query.executeUpdate()

            System.console().println("Inserted " + updatedRows + " rows");

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
               eventStanding.player.alias = "Nissa";
               eventStanding.player.imgUrl = "Nissa.png";
           }
        }

        respond standingList, model:[eventStandingCount: standingList.size()];
    }

    def show(EventStanding eventStanding) {
        respond eventStanding
    }
}
