// import org.apache.hc.client5.http.async.methods.SimpleRequestBuilder;
// import org.apache.hc.client5.http.classic.HttpClient;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Base64;

public class GameSession {

    // this class represents the game session for Elfenroads (or whatever variant)
    // its constructor connects to the LobbyService
    // each GameSession

    private String username;
    private String password;
    private String accessToken;
    private String sessionName;
    private String basicAuthenticationCredentials;
    private String basicAuthEncoded;


    public GameSession(String pUsername, String pPassword, String pSessionName) throws IOException {

        // first, try to authenticate the user
        // check if the username and password belongs to an existing user
        // if not, either prompt the user to make a new account, or do it automatically??

        // record some information from the api to the GameSession (players and such)
        // TODO: authenticate the user so that we can view and add sessions
        // we need to add parameters to the POST request in order to get an OAuth2 token

        username = pUsername;
        password = pPassword;
        sessionName = pSessionName;
        basicAuthenticationCredentials = "bgp-client-name:bgp-client-pw";
        basicAuthEncoded = Base64.getEncoder().encodeToString(basicAuthenticationCredentials.getBytes());

        accessToken = authenticate();





    }

    public String authenticate() throws IOException {
        URL url = new URL("http://127.0.0.1:4242/oauth/token?grant_type=password&username=" + username + "&password=" + password);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        // con.setDoInput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Basic " + basicAuthEncoded);


        // con.connect();

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        System.out.println("Response status: " + status);
        System.out.println(content.toString());

        return content.toString();

    }

    public String createNewSession() throws IOException {
        // make an API call to create a new session
        // to create a new session:
        // request parameters: access_token=...
        // header parameters: Content-Type: application/json
        /* request body: {
            "creator": "maex",
            "game": "DummyGame1",
            "savegame": "funnysavegameid42"
         }
         */

        URL url = new URL("http://127.0.0.1:4242/api/sessions?access_token=" + accessToken);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("    \"creator\": \"" + username + "\",\n");
        out.writeBytes("    \"game\": \"" + sessionName + "\",\n");
        out.writeBytes("    \"savegame\": \"\"\n");
        out.writeBytes("}");
        out.flush();
        out.close();

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        System.out.println("Response status: " + status);
        System.out.println(content.toString());

        String response = content.toString();

        return response;
    }



}
