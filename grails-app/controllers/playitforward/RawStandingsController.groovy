package playitforward

import grails.plugin.springsecurity.annotation.Secured
import grails.transaction.Transactional;
import org.jsoup.Jsoup
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element
import org.springframework.security.access.prepost.PreAuthorize;

import java.sql.Date
import java.time.Month

@Transactional(readOnly = true)
class RawStandingsController {

    static responseFormats = ['json', 'xml'];
    static allowedMethods = [];

    def sessionFactory;

    static scoCoverageUrl = 'http://old.starcitygames.com/content/archive';
    static gpCoverageUrl = 'https://magic.wizards.com/en/events/coverage';
    static cfbCoverageUrl = 'https://coverage.channelfireball.com/';

    def show(RawStandings rawStandings) {
        respond rawStandings;
    }

    def getPlayers(Integer eventId) {

        final session = sessionFactory.currentSession;
        String sql = " Select name from raw_standings where event_id = ${eventId} and round = 1 order by name";
        def query = session.createSQLQuery(sql);
        List<Object[]> rows = query.list();

        respond(['players': rows], view:'players');
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

        if (linkMap.size() == 15 && linkMap.get(15).standingsUrl != null) {
            respond([message: message]);
            return;
        }

        try {

            if (type.equalsIgnoreCase("SCO"))
            {
                loadStarCities(event, linkMap);
            }
            else
            {
                if (event.eventUrl != null) {

                    if (event.eventUrl.toLowerCase().contains("magic.wizards.com")) {
                        loadCFB(event, linkMap);
                    } else {
                        loadCFBNew(event, linkMap);
                    }
                }
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

        String url = event.eventUrl + "/tournament-results";
        if (invalidUrl(url)) {
            url = event.eventUrl + "/tournament-results-and-decklists";
            if (invalidUrl(url)) {
                url = event.eventUrl;
            }
        }

        Document doc = Jsoup.connect(url).userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000).get();

        Element parent = doc.select("#pairings-results-and-standings").first();
        Elements elements = parent.select("div.by-day a");

        List<String> pairingList = new ArrayList<>();
        List<String> resultsList = new ArrayList<>();
        List<String> standingsList = new ArrayList<>();
        for (int i = 0 ; i < elements.size() ; i++) {

            String link = elements[i].attr("href").toLowerCase();
            if (link.contains("pairings")){
                pairingList.push(link);
            } else if (link.contains("results")) {
                resultsList.push(link);
            } else if (link.contains("standings")) {
                standingsList.push(link);
            }
        }

        for (int i = 0 ; i < standingsList.size() ; i++) {

            EventLinks eventLinks = new EventLinks();

            if (linkMap.containsKey(i)) {
                eventLinks = linkMap.get(roundInt);
            }

            eventLinks.setEvent(event);
            eventLinks.setRound(i + 1);
            eventLinks.setPairingsUrl(pairingList.get(i));
            eventLinks.setStandingsUrl(standingsList.get(i));
            eventLinks.setResultsUrl(resultsList.get(i));

            eventLinks.save();
        }
    }

    void loadCFBNew(Event event, Map<Integer, EventLinks> linkMap) {

        Document doc = Jsoup.connect(event.eventUrl).userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000).get();

        Element parent = doc.select("table.table-sm").first();
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
                if (year < 2020) {
                    scrapeCFB(year, type);
                } else {
                    scrapeNewCFB(year, type);
                }
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

        for (Element element : elements) {

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

            if (format.toUpperCase().contains("TEAM")) {
                continue;
            }

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

    void scrapeNewCFB(int year, String type) {

        EventType eventType = EventType.findByType(type);
        EventOrganizer organizer = EventOrganizer.findByName("Channel Fireball");
        Date firstOfYear = new Date(year - 1900, 0, 1);
        Date lastOfYear = new Date(year - 1900, 11, 31);

        List<Event> eventList = Event.findAllByStartDateBetweenAndType(firstOfYear, lastOfYear, eventType);
        Map<String, Event> eventMap = new HashMap<>();
        for(Event event: eventList) {
            eventMap.put(event.getCfbEventNum(), event);
        }

        Document doc = Jsoup.connect(cfbCoverageUrl).userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000).get();

        Elements elements = doc.select("main div.text-center");
        for (Element element : elements) {

            Element link = element.select("h5 a").first();
            Element info = element.select("div.small").first();
            if (link == null || info == null) {
                continue;
            }

            String format = info.textNodes()[1].text().trim();
            String name = link.text().replace("MagicFest", "").trim();
            String startDate = info.textNodes()[0].text().trim();
            String url = link.attr("href");
            Integer eventKey = Integer.valueOf(url.substring(url.lastIndexOf("/") + 1).trim());

            if (format.toUpperCase().contains("TEAM")) {
                continue;
            }

            if (format.toUpperCase().contains("LIMITED")) {
                format = 'Limited';
            }

            Integer startYear = Integer.valueOf(startDate.substring(startDate.indexOf(",") + 1).trim());
            startDate = startDate.substring(0, startDate.indexOf("-"));
            String month = startDate.substring(0, startDate.indexOf(" ")).trim();
            String day = startDate.substring(startDate.indexOf(" ") + 1).trim();

            Date start = new Date(startYear - 1900, Month.valueOf(month.toUpperCase()).value - 1, Integer.valueOf(day));
            if (start.year + 1900 != year) {
                continue;
            }

            Date end = start + 1;

            Event event = new Event();
            if (eventMap.containsKey(eventKey))
            {
                event = eventMap.get(eventKey);
            }

            event.setName(name);
            event.setCfbEventNum(eventKey);
            event.setType(eventType);
            event.setStartDate(start);
            event.setEndDate(end);
            event.setEventUrl(url);
            event.setOrganizer(organizer);
            event.setEventFormat(format);

            event.save();
        }
    }

    void scrapeCFB(int year, String type) {

        EventType eventType = EventType.findByType(type);
        EventOrganizer organizer = EventOrganizer.findByName("Channel Fireball");
        Date firstOfYear = new Date(year - 1900, 0, 1);
        Date lastOfYear = new Date(year - 1900, 11, 31);

        List<Event> eventList = Event.findAllByStartDateBetweenAndType(firstOfYear, lastOfYear, eventType);
        Map<String, Event> eventMap = new HashMap<>();
        for(Event event: eventList) {
            eventMap.put(buildGPKey(event), event);
        }

        int lastYear = year - 1;
        int nextYear = year + 1;
        String id1 = "${lastYear}-${year}-season";
        String id2 = "${year}-${nextYear}-season";

        Document doc = Jsoup.connect(gpCoverageUrl).userAgent("Mozilla")
                .cookie("auth", "token")
                .timeout(5000).get();

        Elements elements = new Elements();
        elements.add(doc.select("#${id1} p").last());
        elements.add(doc.select("#${id2} p").last());

        List<String> formatList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        List<String> startList = new ArrayList<>();
        List<String> urlList = new ArrayList<>();

        for (Element element : elements) {

            if (element == null) {
                continue;
            }

            for (TextNode node : element.textNodes()) {

                String text = node.text();
                if (text.contains("|")) {
                    continue;
                }

                if (text.contains("(")) {

                    String date = text.substring(text.indexOf("(")+1, text.indexOf(")")).trim();
                    startList.add(date);

                    text = text.substring(text.indexOf(")") + 2).trim().replace("-", "");
                    if (! text.trim().isEmpty()) {
                        formatList.add(text.trim());
                    }
                }
                else {
                    if (! text.trim().isEmpty()) {
                        formatList.add(text.trim());
                    }
                }
            }

            for (Element child : element.children()) {

                if (child.tag().name.equalsIgnoreCase("a") && child.classNames().contains("more")) {

                    nameList.add(child.text().trim());

                    String url = child.attr("href");
                    if (url.startsWith("/")) {
                        url = "https://magic.wizards.com/en" + url;
                    }
                    urlList.add(url.trim());
                }
            }
        }

        for (int i = 0 ; i < nameList.size() ; i++) {

            if (formatList.get(i).toUpperCase().contains("TEAM")) {
                continue;
            }

            String testUrl = urlList.get(i);
            String longTest = testUrl + "/tournament-results";
            String longestUrl = testUrl + "/tournament-results-and-decklists";
            if (invalidUrl(testUrl) && invalidUrl(longTest) && invalidUrl(longestUrl)) {
                continue;
            }

            String startString = startList.get(i).trim();
            Integer startYear = Integer.valueOf(startString.substring(startString.length() - 4));
            String month = startString.substring(0, startString.indexOf(" ")).trim();
            String day = startString.substring(startString.indexOf(" ") + 1, startString.indexOf("-")).trim();

            Date start = new Date(startYear - 1900, Month.valueOf(month.toUpperCase()).value - 1, Integer.valueOf(day));
            if (start.year + 1900 != year) {
                continue;
            }

            Date end = start + 1;

            Event event = new Event();
            event.setName(nameList.get(i));
            event.setStartDate(start);

            if (eventMap.containsKey(buildGPKey(event)))
            {
                event = eventMap.get(buildGPKey(event));
            }

            event.setName(nameList.get(i));
            event.setType(eventType);
            event.setStartDate(start);
            event.setEndDate(end);
            event.setEventUrl(urlList.get(i));
            if (event.organizer == null) {
                event.setOrganizer(organizer);
            }
            if (event.getEventUrl().toLowerCase().contains("coverage.channelfireball.com")) {
                String url = event.getEventUrl();
                event.setCfbEventNum(Integer.valueOf(url.substring(url.lastIndexOf("/") + 1).trim()));
            }

            event.setEventFormat(formatList.get(i).replace("Block", "").trim());

            event.save();
        }
    }

    String buildGPKey(Event event) {

        return event.name + "-" + event.startDate.toString();
    }

    Boolean invalidUrl(String url) {

        URL test = new URL(url);
        HttpURLConnection huc = (HttpURLConnection) test.openConnection();
        int responseCode = huc.getResponseCode();
        return (responseCode == HttpURLConnection.HTTP_NOT_FOUND);
    }
}
