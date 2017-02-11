package playitforward

import org.apache.commons.httpclient.NameValuePair
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.entity.UrlEncodedFormEntity
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.HttpClients
import org.apache.http.message.BasicNameValuePair

import java.util.regex.Matcher
import java.util.regex.Pattern

import org.apache.commons.codec.binary.Base64;

class PushNotificationController {

    private static final String ACCOUNT_SID = "AC535ebaff99248ccc888e872e71a050d0";
    private static final String AUTH_TOKEN = "097c40f3dff014564a9b48765235962a";
    private static final String url = "https://api.twilio.com/2010-04-01/Accounts/" + ACCOUNT_SID + "/Messages.json";

    static String urlBase ='http://magic.wizards.com/en/events/coverage/';
    Pattern seatPattern = Pattern.compile("\\s*</tr>\\s*<tr><td>(\\d*-*)</td>");

    @SuppressWarnings("GroovyUnusedDeclaration")
    static responseFormats = ['json', 'xml']
    static allowedMethods = []

    static mappings = {
    }

    def index(String gpName, Integer roundNum) {

        Map<String, String> rawSeatings = getSeatings(getUrl(gpName, roundNum));
        Map<String, String> parsedSeatings = new HashMap<>();

        Person.list(params).each{ person ->
            String name = person.lastName + ", " + person.firstName;
            if (person.sendPushNotifications && rawSeatings.containsKey(name))
            {
                String seating = rawSeatings.get(name);
                parsedSeatings.put(name, seating);
                sendTextMessage(person.phone, name, seating, roundNum);
            }
        };

        sendControllerMessage(gpName, roundNum, parsedSeatings);
        respond parsedSeatings, model:[PushNotificationCount: parsedSeatings.size()];
    }

    def pushPastTimeGames(String gpName, Integer roundNum) {

        Map<String, String> rawSeatings = getPastTimeSeatings("http://pastimes.mtgel.com/pairings/13");
        Map<String, String> parsedSeatings = new HashMap<>();

        Person.list(params).each{ person ->
            String name = person.lastName + ", " + person.firstName;
            if (person.sendPushNotifications)
            {
                if (rawSeatings.containsKey(name))
                {
                    String seating = rawSeatings.get(name);
                    parsedSeatings.put(name, seating);
                }
                else
                {
                    String seating = "Bye";
                    parsedSeatings.put(name, seating);
                }

                sendTextMessage(person.phone, name, parsedSeatings.get(name), roundNum);
            }
        };

        sendControllerMessage(gpName, roundNum, parsedSeatings);
        respond parsedSeatings, model:[PushNotificationCount: parsedSeatings.size()];
    }

    private static void sendControllerMessage(String gpName, Integer roundNum, Map<String, String> pairings) {

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

        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("To", '13039567188'));
        params.add(new BasicNameValuePair("From", "17207533049"));
        params.add(new BasicNameValuePair("Body", body));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
    }

    private static void sendTextMessage(String phoneNumber, String name, String seat, Integer round) {

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
            seat = "bye";
        }

        String body = name + " pairing for round: " + round + " is at table " + seat;
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("To", phoneNumber));
        params.add(new BasicNameValuePair("From", "17207533049"));
        params.add(new BasicNameValuePair("Body", body));
        httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        HttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
    }

    private static String getUrl(String gp, int round)
    {
        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        int monthOfYear = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);

        String month = String.format("%02d", monthOfYear);
        String day = String.format("%02d", dayOfMonth);
        //month = '01';
        //day = '28';

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

    private Map<String,String> getPastTimeSeatings(String url)
    {
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
