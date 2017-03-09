package playitforward

import org.apache.commons.logging.LogFactory
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair

import grails.plugin.springsecurity.annotation.Secured

import javax.annotation.PostConstruct
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.commons.codec.binary.Base64;

class PushNotificationController {

    private static final logger = LogFactory.getLog(this);

    private String ACCOUNT_SID = null;
    private String AUTH_TOKEN = null;
    private String url = null;
    private String phoneSource = null;

    Pattern seatPattern = Pattern.compile("\\s*</tr>\\s*<tr><td>(\\d*-*)</td>");

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ['json', 'xml']
    static allowedMethods = []

    @SuppressWarnings("GroovyUnusedDeclaration")
    static mappings = {
    }

    @PostConstruct
    public synchronized void init() {

        if (ACCOUNT_SID == null) {

            ACCOUNT_SID = grailsApplication.config.getProperty('twilio.sid');
            AUTH_TOKEN = grailsApplication.config.getProperty('twilio.token');
            url = "https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/Messages.json";
            phoneSource = grailsApplication.config.getProperty('twilio.phone');
        }
    }

    def ping(String name) {

        Map<String, String> results = new HashMap<>();
        results.put("name", name);
        results.put("sid", ACCOUNT_SID.substring(0, 5) + "...");
        results.put("token", AUTH_TOKEN.substring(0, 5) + "...");
        results.put("phone", phoneSource);
        results.put("url", getWizardsUrl(name, 9));

        Event event = Event.findByEventCode(name);
        results.put("start", event.getStartDate().toString());
        results.put("end", event.getEndDate().toString());
        results.put("title", event.getName());

        respond results, model: [PushNotificationCount: results.size()];
    }

    @Secured('ROLE_ADMIN')
    def index(String gpName, Integer roundNum) {

        logger.info("Called Wizard's Pairings for " + gpName + " round " + roundNum + ".");

        Map<String, String> rawSeatings = getSeatings(getWizardsUrl(gpName, roundNum));
        Map<String, String> parsedSeatings = new HashMap<>();

        Person.findAllBySendPushNotifications(true).each { person ->

            String name = person.lastName + ", " + person.firstName;
            if (rawSeatings.containsKey(name))
            {
                String seating = rawSeatings.get(name);
                parsedSeatings.put(name, seating);
                sendTextMessage(person.phone, name, seating, roundNum);
            }
        };

        sendControllerMessage(gpName, roundNum, parsedSeatings);
        respond parsedSeatings, model:[PushNotificationCount: parsedSeatings.size()];
    }

    @Secured('ROLE_ADMIN')
    def pushPastTimeGames(String gpName, Integer roundNum) {

        logger.info("Called Pasttime's Pairings for " + gpName + " round " + roundNum + ".");

        Map<String, String> rawSeatings = getPastTimeSeatings("http://pastimes.mtgel.com/pairings/13");
        Map<String, String> parsedSeatings = new HashMap<>();

        Person.findAllBySendPushNotifications(true).each { person ->

            String name = person.lastName + ", " + person.firstName;
            if (rawSeatings.containsKey(name))
            {
                parsedSeatings.put(name, rawSeatings.get(name));
                sendTextMessage(person.phone, name, parsedSeatings.get(name), roundNum);
            }
        };

        sendControllerMessage(gpName, roundNum, parsedSeatings);
        respond parsedSeatings, model:[PushNotificationCount: parsedSeatings.size()];
    }

    @Secured('ROLE_ADMIN')
    def pushGameKeeper(String gpName, Integer roundNum) {

        logger.info("Called GameKeeper's Pairings for " + gpName + " round " + roundNum + ".");

        Map<String, String> rawSeatings = getPastTimeSeatings("http://pairings.gamekeeper.ca/pairings/14");
        Map<String, String> parsedSeatings = new HashMap<>();

        Person.findAllBySendPushNotifications(true).each { person ->

            String name = person.lastName + ", " + person.firstName;
            if (rawSeatings.containsKey(name))
            {
                parsedSeatings.put(name, rawSeatings.get(name));
                sendTextMessage(person.phone, name, parsedSeatings.get(name), roundNum);
            }
        };

        sendControllerMessage(gpName, roundNum, parsedSeatings);
        respond parsedSeatings, model:[PushNotificationCount: parsedSeatings.size()];
    }

    private void sendControllerMessage(String gpName, Integer roundNum, Map<String, String> pairings) {

        String authString = ACCOUNT_SID + ":" + AUTH_TOKEN;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Authorization", "Basic " + authStringEnc);
        httpPost.setHeader("charset", "utf-8");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        String body = "Sent pairings for " + gpName + " round " + roundNum + ".";
        for (String name : pairings.keySet())
        {
            body += " " + name + " - " + pairings.get(name).replace("-", "bye") + ".";
        }

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(3);
        params.add(new BasicNameValuePair("To", '13039567188'));
        params.add(new BasicNameValuePair("From", phoneSource));
        params.add(new BasicNameValuePair("Body", body));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        logger.info(httpClient.execute(httpPost).getStatusLine());
    }

    private void sendTextMessage(String phoneNumber, String name, String seat, Integer round) {

        logger.info("Sending text to " + name + " for round " + round + " seat " + seat);

        String authString = ACCOUNT_SID + ":" + AUTH_TOKEN;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);

        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setHeader("Authorization", "Basic " + authStringEnc);
        httpPost.setHeader("charset", "utf-8");
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        if (seat.trim().equals("-"))
        {
            seat = "Bye";
        }

        String body = name + " pairing for round: " + round + " is at table " + seat;
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>(3);
        params.add(new BasicNameValuePair("To", phoneNumber));
        params.add(new BasicNameValuePair("From", phoneSource));
        params.add(new BasicNameValuePair("Body", body));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        logger.info(httpClient.execute(httpPost).getStatusLine());
    }

    private static String getWizardsUrl(String gp, Integer round) {

        Event event = Event.findByEventCode(gp);
        Calendar cal = Calendar.getInstance();

        if (round < 10)
        {
            cal.setTime(event.getStartDate());
        }
        else
        {
            cal.setTime(event.getEndDate());
        }

        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        String month = String.format("%02d", monthOfYear);
        String day = String.format("%02d", dayOfMonth);

        String url ='http://magic.wizards.com/en/events/coverage/' + gp +
            '/round-' + round + '-pairings-' + year + '-' + month + '-' + day;

        return url;
    }

    private Map<String, String> getSeatings(String url) {

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

    private static Map<String,String> getPastTimeSeatings(String url) {

        Map<String, String> results = new HashMap<>();
        URL pairingsURL = new URL(url);
        URLConnection ec = pairingsURL.openConnection();

        def input = new BufferedReader(new InputStreamReader(
                ec.getInputStream(), "UTF-8"));

        String inputLine = input.readLine();
        String seatLine;
        String nameLine;
        while((inputLine != null))
        {
            // Each person has a row with them in column 2.
            //   <tr>
            //           <td>249</td>
            //           <td>Pho, Teresa</td>
            //           <td>3</td>
            //           <td>McIntyre, James</td>
            //   </tr>

            if (inputLine.trim().equals("<tr>"))
            {
                seatLine = input.readLine().trim();
                nameLine = input.readLine().trim();

                String seat = seatLine.substring(4);
                seat = seat.replaceAll("</td>", "");

                String name = nameLine.substring(4);
                name = name.replaceAll("</td>", "");

                results.put(name, seat);
            }

            inputLine = input.readLine ();
        }

        input.close();

        return results;
    }
}
