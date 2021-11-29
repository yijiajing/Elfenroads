import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class User {

    // this class represents a user of the lobby service

    private String username;
    private String password;
    private String accessToken;
    private String basicAuthCredentials;
    private String basicAuthEncoded;
    private boolean isAuthenticated;


    public User(String pUsername, String pPassword)
    {
        username = pUsername;
        password = pPassword;
        basicAuthCredentials = "bgp-client-name:bgp-client-pw";
        basicAuthEncoded = Base64.getEncoder().encodeToString(basicAuthCredentials.getBytes());
        isAuthenticated = false;

    }

    public String authenticate() throws IOException
    {
        URL url = new URL("http://127.0.0.1:4242/oauth/token?grant_type=password&username=" + username + "&password=" + password);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Basic " + basicAuthEncoded);

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

        // try reading the stuff into a JSON object
        JSONObject tokens = new JSONObject(content);

        // System.out.println("The fields in the file received are: " + JSONObject.getNames(content));
        // accessToken = (String) tokens.get("access_token");
        // System.out.println("The access token is: " + accessToken);

        // returns the JSON object containing the bearer and refresh tokens. See API docs for details
        isAuthenticated = true;
        return content.toString();
    }











}
