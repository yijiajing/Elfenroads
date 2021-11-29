import com.google.gson.Gson;
import org.json.*;

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
    private String refreshToken;
    private int tokenExpiresIn;


    public User(String pUsername, String pPassword) throws IOException
    {
        username = pUsername;
        password = pPassword;
        basicAuthCredentials = "bgp-client-name:bgp-client-pw";
        basicAuthEncoded = Base64.getEncoder().encodeToString(basicAuthCredentials.getBytes());
        isAuthenticated = false;
        authenticate();

    }

    public int authenticate() throws IOException
    {
        URL url = new URL("http://127.0.0.1:4242/oauth/token?grant_type=password&username=" + username + "&password=" + password);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Basic " + basicAuthEncoded);
        con.setRequestProperty("Content-Type", "application/json");

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

        // trying to read the stuff into a JSON file using GSON
        String contentAsString = content.toString();
        JSONObject json = new JSONObject(contentAsString);
        // now we have the json as an object



        // System.out.println("The fields in the file received are: " + JSONObject.getNames(content));
        // accessToken = (String) tokens.get("access_token");
        // System.out.println("The access token is: " + accessToken);

        // returns the JSON object containing the bearer and refresh tokens. See API docs for details
        // set the fields using the JSON info

        accessToken = escapePlusSign(json.get("access_token").toString());
        refreshToken = escapePlusSign(json.get("refresh_token").toString());
        isAuthenticated = true;

        // System.out.println("The access token is " + accessToken + " and the refresh token is " + refreshToken);
        return status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public int getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public String getUsername() {
        return username;
    }

    private String escapePlusSign(String token)
    {
        String returnVal = token.replace("+", "%2B");
        return returnVal;
    }

}
