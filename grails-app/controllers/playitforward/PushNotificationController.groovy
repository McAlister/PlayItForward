package playitforward

import java.util.regex.Matcher
import java.util.regex.Pattern

class PushNotificationController {

    static String urlBase ='http://magic.wizards.com/en/events/coverage/';
    Pattern seatPattern = Pattern.compile("\\s*</tr>\\s*<tr><td>(\\d*-*)</td>");

    static responseFormats = ['json', 'xml']
    static allowedMethods = []

    static mappings = {
    }

    def index(String gpName, Integer roundNum) {
        //params.max = Math.min(max ?: 10, 100)
        //respond Person.list(params), model:[personTypeCount: Person.count()]

        Map<String, String> rawSeatings = getSeatings(getUrl(gpName, roundNum));
        Map<String, String> parsedSeatings = new HashMap<>();

        Person.list(params).each{ person ->
            String name = person.lastName + ", " + person.firstName;
            if (person.sendPushNotifications && rawSeatings.containsKey(name))
            {
                parsedSeatings.put(name, rawSeatings.get(name));
            }
        };

        respond parsedSeatings, model:[PushNotificationCount: parsedSeatings.size()];
    }

    def show(Person personType) {
        respond personType
    }

    private String getUrl(String gp, int round)
    {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        String month = String.format("%02d", monthOfYear);
        //String day = String.format("%02d", dayOfMonth);
        String day = '28';

        String url = urlBase + gp + '/round-' + round + '-pairings-' +
                year + '-' + month + '-' + day;

        return url;
    }

    private Map<String, String> getSeatings(String url)
    {
        Map<String, String> results = new HashMap<>();
        URL pairingsURL = new URL(url);
        URLConnection ec = pairingsURL.openConnection();

        def input = new BufferedReader(new InputStreamReader(
                ec.getInputStream(), "UTF-8"));

        String inputLine = input.readLine();
        while((inputLine != null))
        {
            //<tr><td>277</td>
            //<td>Burnett, Sean</td>

            Matcher seatMatcher = seatPattern.matcher(inputLine);
            if (seatMatcher.find())
            {
                String seat = seatMatcher.group(1);
                inputLine = input.readLine().trim();
                String name = inputLine.substring(4);
                name = name.replaceAll("</td>", "");

                results.put(name, seat);

                input.readLine().trim();
                input.readLine().trim();
                inputLine = input.readLine().trim();
                name = inputLine.substring(4);
                name = name.replaceAll("</td>", "");

                results.put(name, seat);
            }

            inputLine = input.readLine ();
        }

        input.close();

        return results;
    }

    private void pushNotifications(Map<String, String> seatings)
    {
        seatings.each { person, seat ->
            System.out.println("Person/Seat" + person + seat);
        }
    }
}
