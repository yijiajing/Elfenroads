
import org.json.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

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


    /**
     * @pre user already exists in LS
     * @param pUsername
     * @param pPassword
     * @throws IOException
     */
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
        // System.out.println("Response status: " + status);
        // System.out.println(content.toString());

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

    public String getAccessToken() throws IOException
    {
        authenticate();
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

    // TODO: need to implement this method
    public static boolean doesUsernameExist(String username) throws IOException
    {
        User admin = new User("maex", "abc123_ABC123");
        String token = admin.getAccessToken();
        // do a get request to api/users and look at all users, see if our username is there
        //JSONObject allUsers = getAllUsers(token);
        List<JSONObject> allUsers = getAllUsers(token);
        for (JSONObject json : allUsers)
        {
        	
        	if (json.get("name").toString().equals(username))
        	{
        		return true;
        	}
        }

       return false;
    }
    
    // from Max's code
    public static boolean doesPasswordExist(String password)
    {
    	return Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}").matcher(password).find();
    }

    public static List<JSONObject> getAllUsers(String adminToken) throws IOException

    {
        
    	URL url = new URL("http://127.0.0.1:4242/api/users?access_token=" + adminToken);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

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
        //System.out.println(content.toString());
        
        
        // Start here
        String user = "";
        List<JSONObject> allUsers = new ArrayList<>();
        String contentAsString = content.toString();
        
        int flag = 0;
        for (int i = 0; i < content.length(); i++)
        {
        	if (contentAsString.charAt(i) == '}')
        	{
        		user += contentAsString.charAt(i);
        		flag = 0;
        		
        		String str = user.toString();
        		JSONObject json = new JSONObject(str);
        		user = "";
        		allUsers.add(json);
        		continue;
        	}
        	
        	if (flag == 1)
        	{
        		user += contentAsString.charAt(i);
        		continue;
        	}
        	
        	if (contentAsString.charAt(i) == '{')
        	{
        		user += contentAsString.charAt(i);
        		flag = 1;
        		continue;
        	}
        	
        }
        
        for (JSONObject j : allUsers)
        {
        	System.out.println(j.toString());
        }
    	
    	return allUsers;
    }

}
