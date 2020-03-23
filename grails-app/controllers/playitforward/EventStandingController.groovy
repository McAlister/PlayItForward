package playitforward

import grails.transaction.Transactional
import groovy.transform.Synchronized
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

@Transactional(readOnly = true)
class EventStandingController {

    private final myLock = new Object();

    def sessionFactory;

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    static defaultNames = [ "Arlinn", "Avacyn", "Chandra", "Elspeth", "Kaya",
                                     "Kemba", "Kiora", "Liliana", "Mirri", "Nahiri",
                                     "Narset", "Nissa", "Olivia", "Oona", "Pia", "Saheeli",
                                     "SliverQueen", "Tamiyo", "Vraska", "Wort"
    ];

    @Transactional
    def latestRound(Integer eventId) {

        final session = sessionFactory.currentSession;
        final String sql = "Select max(round) from raw_standings where event_id = " + eventId;
        def query = session.createSQLQuery(sql);
        List<Object[]> rows = query.list();

        Integer lastRound = rows[0] == null ? 0 : rows[0].toString() as Integer;
        respond(['latestRound': lastRound], view:'latestRound');
    }

    @Transactional
    @Synchronized("myLock")
    def loadEvent(Integer eventId, Integer round) {

        Boolean loaded = false;
        Event event = Event.findById(eventId);

        Integer rawCount = RawStandings.countByEventAndRound(event, round);
        if (rawCount > 0) {
            loaded = true;
        } else {

            EventLinks links = EventLinks.findByEventAndRound(event, round);
            if (links != null) {

                String url = links.getStandingsUrl();
                if (url != null && !url.isEmpty()) {

                    if (url.contains("star")) {
                        loaded = scrapeStarCitiesRound(url, event, round);
                    } else if (url.contains("channelfireball")) {
                        loaded = scrapeCFBRound(url, event, round);
                    } else if (url.contains("wizard")) {
                        loaded = scrapeWizardRound(url, event, round);
                    } else {
                        loaded = false;
                    }
                }
            }
        }

        respond(['loaded': loaded], view: 'loaded');
    }

    @Transactional
    def index(Integer eventId, Integer roundNum) {

        getRawIfNeeded(eventId, roundNum);
        if (roundNum < 15) {
            getRawIfNeeded(eventId, roundNum + 1);
        }

        final session = sessionFactory.currentSession;
        final String sql = "Insert Into event_standing (id, version, event_id, player_id, points, round, rank )\n" +
                "Select nextval('event_standing_seq'), 0, r.event_id, p.id, r.points, r.round, r.rank\n" +
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

    // //////////////// //
    // Helper Functions //
    // //////////////// //

    def scrapeWizardRound(String url, Event event, Integer round) {

        return scrapeCFBRound(url, event, round);
    }

    def scrapeCFBRound(String url, Event event, Integer round) {

        try {

            Document doc = Jsoup.connect(url).userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(5000).get();

            if (doc == null) {
                return false;
            }

            Elements rows = doc.select("tbody tr");
            RawStandings raw;

            for (Element row : rows) {

                try {

                    raw = new RawStandings();
                    raw.setEvent(event);
                    raw.setRound(round);
                    raw.setIsWoman(false);

                    Elements tds = row.select("td");
                    if (tds.size() < 4) {
                        continue;
                    }

                    raw.setRank(Integer.valueOf(tds[0].text().trim()));
                    raw.setName(tds[1].text().trim());
                    raw.setPoints(Integer.valueOf(tds[2].text().trim()));
                    raw.setOpponentMatchWin(Double.valueOf(tds[3].text().trim()));

                    raw.save();

                } catch (Exception ex) {

                    System.console().println("Unable To Insert Raw Standing: " + ex.getMessage());
                }
            }

        } catch (Exception ex) {

            System.console().println("Unable to scrape Channel Fireball: " + ex.getMessage());
        }

        Integer rawCount = RawStandings.countByEventAndRound(event, round);
        return rawCount > 0;
    }

    def scrapeStarCitiesRound(String url, Event event, Integer round) {

        try {

            Document doc = findValidStarCityStandings(url, event, round);
            if (doc == null) {
                return false;
            }

            Elements rows = doc.select("tr");
            RawStandings raw;

            for (Element row : rows) {

                try {

                    raw = new RawStandings();
                    raw.setEvent(event);
                    raw.setRound(round);
                    raw.setIsWoman(false);

                    Elements tds = row.select("td");
                    if (tds.size() < 4) {
                        continue;
                    }

                    raw.setRank(Integer.valueOf(tds[0].text().trim()));
                    raw.setName(tds[1].text().trim());
                    raw.setPoints(Integer.valueOf(tds[2].text().trim()));
                    raw.setOpponentMatchWin(Double.valueOf(tds[3].text().trim()));

                    raw.save();

                } catch (Exception ex) {

                    System.console().println("Unable To Insert Raw Standing: " + ex.getMessage());
                }
            }

        } catch (Exception ex) {

            System.console().println("Unable to scrape Star Cities" + ex.getMessage());
        }

        Integer rawCount = RawStandings.countByEventAndRound(event, round);
        return rawCount > 0;
    }

    def findValidStarCityStandings(String url, Event event, Integer round) {

        Document doc = Jsoup.connect(url).userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000).get();

        try {

            String header = doc.select("th").first().text().trim();
            int nextRound = round;
            while (! header.equalsIgnoreCase("rank")) {

                nextRound++;
                EventLinks links = EventLinks.findByEventAndRound(event, nextRound);
                if (links != null && links.standingsUrl != null) {

                    doc = Jsoup.connect(links.standingsUrl).userAgent("Mozilla")
                            .cookie("auth", "token")
                            .timeout(5000).get();
                    header = doc.select("th").first().text().trim();

                } else {
                    doc = null;
                    break;
                }
            }

        } catch (Exception ex) {

            doc = null;
            System.console().println("Unable to find valid SCO standings" + ex.getMessage());
        }

        return doc;
    }

    def getRawIfNeeded(Integer eventId, Integer roundNum) {

        Event event = Event.findById(eventId);

        Integer rawCount = RawStandings.countByEventAndRound(event, roundNum);
        if (rawCount > 0) {
            return;
        }

        EventLinks links = EventLinks.findByEventAndRound(event, roundNum);
        String url = links.getStandingsUrl();

        if (url != null && !url.isEmpty()) {

            if (url.contains("star")) {
                scrapeStarCitiesRound(url, event, roundNum);
            }
        }
    }

}
