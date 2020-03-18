package playitforward

import grails.transaction.Transactional;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.sql.Date

@Transactional(readOnly = true)
class RawStandingsController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    static scoCoverageUrl = 'http://old.starcitygames.com/content/archive';

    def index(Integer max) {
        respond RawStandings.list(), model:[rawStandingsCount: RawStandings.count()];
    }

    def show(RawStandings rawStandings) {
        respond rawStandings;
    }

    @Transactional
    def loadEvent(long eventId, String type) {

        String message = 'success';
        println("event: " + eventId + " type: " + type);

        Event event = Event.findById(eventId);
        Map<Integer, EventLinks> linkMap = new HashMap<>();
        for(EventLinks eventLinks : event.linkSet) {
            linkMap.put(eventLinks.round, eventLinks);
        }

        try {

            if (type.equalsIgnoreCase("SCO"))
            {
                loadStarCities(event, linkMap);
            }
            else
            {
                loadCFB(event, linkMap);
            }

        } catch (Exception ex) {
            message = ex.getMessage();
        }

        respond([message: message]);
    }

    void loadStarCities(Event event, Map<Integer, EventLinks> linkMap) {

        Document doc = Jsoup.connect(event.eventUrl).userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000).get();

        Element parent = doc.select("table.standings_table").first();
        Elements elements = parent.select("tr");

        for (int i = 1 ; i < elements.size() ; i++) {

            // Pairings, Results, Standings
            Element element = elements[i];
            Elements links = element.select("a");

            String round = links[0].text();
            Integer roundInt = Integer.valueOf(round);
            String pairingsUrl = links[0].attr("href");
            String resultsUrl = links[1].attr("href");
            String standingsUrl = links[2].attr("href");

            EventLinks eventLinks = new EventLinks();

            if (linkMap.containsKey(roundInt)) {
                eventLinks = linkMap.get(roundInt);
            }

            eventLinks.setEvent(event);
            eventLinks.setRound(roundInt);
            eventLinks.setPairingsUrl(pairingsUrl);
            eventLinks.setStandingsUrl(standingsUrl);
            eventLinks.setResultsUrl(resultsUrl);

            eventLinks.save();
        }
    }

    void loadCFB(Event event, Map<Integer, EventLinks> linkMap) {

    }

    @Transactional
    def scrapeEvents(int year, String type) {

        String message = 'success';
        println("Year: " + year + " type: " + type);

        try {

            if (type.equalsIgnoreCase("SCO"))
            {
                scrapeStarCities(year, type);
            }
            else
            {
                scrapeCFB(year);
            }

        } catch (Exception ex) {
            message = ex.getMessage();
        }

        respond([message: message]);
    }

    void scrapeStarCities(int year, String type) {

        EventType eventType = EventType.findByType(type);
        EventOrganizer organizer = EventOrganizer.findByName("Star City Games");
        Date firstOfYear = new Date(year - 1900, 0, 1);
        Date lastOfYear = new Date(year - 1900, 11, 31);

        List<Event> eventList = Event.findAllByStartDateBetweenAndType(firstOfYear, lastOfYear, eventType);
        Map<Date, Event> eventMap = new HashMap<>();
        for(Event event: eventList) {
            eventMap.put(event.getStartDate(), event);
        }

        String id = "div#scgop-" + year;

        Document doc = Jsoup.connect(scoCoverageUrl).userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000).get();

        Element parent = doc.select(id).first();
        Elements elements = parent.select("div.os-event");

        elements.each { Element element ->

            String location = element.select("div.event-city").text();
            if (location.isEmpty()) {
                location = element.select("div.event-city img").attr("alt");
                location = location.replace("SCG ", "");
            }

            String startDate = element.id();
            String eventUrl = element.select("div.event-title a").first().attr("href");
            String format = element.select("div.winner-box a span.format-link").first().text();

            format = format.replace(" Open)", "");
            format = format.replace("(", "");

            Date start = Date.valueOf(startDate);
            Date end = start + 1;

            Event event = new Event();
            if (eventMap.containsKey(start))
            {
                event = eventMap.get(start);
            }

            event.setName(location);
            event.setType(eventType);
            event.setStartDate(start);
            event.setEndDate(end);
            event.setEventUrl(eventUrl);
            event.setOrganizer(organizer);
            event.setEventFormat(format);

            event.save();
        }
    }

    void scrapeCFB(int year) {

    }

}
