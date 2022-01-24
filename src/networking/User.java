package networking;

import org.json.*;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

public class User {


    // enum used for roles as they appear in the LS

    public enum Role
    {
        PLAYER, SERVICE, ADMIN
    }

    // this class represents a user of the lobby service


    // basic info
    private String username;
    private String password;
    private Role role;
    private String preferredColor; // we probably won't need this, but I set it anyway

    // oauth stuff
    private String accessToken;
    private static final String basicAuthCredentials = "bgp-client-name:bgp-client-pw";
    private static final String adminUsername = "maex";
    private static final String adminPassword = "abc123_ABC123";
    private static final String basicAuthEncoded = Base64.getEncoder().encodeToString(basicAuthCredentials.getBytes());
    private boolean isAuthenticated;
    private String refreshToken;
    private int tokenExpiresIn;
    private Calendar authTokenExpiresAt;
    private Calendar authTokenIssued;
    private String tokenExpiryAsString;

    // for reference
    private JSONObject infoFromAPI;

    /**
     * @pre user already exists in LS
     * @param pUsername
     * @param pPassword
     * @throws IOException
     */

    // TODO: read in role from the API
    public User(String pUsername, String pPassword) throws IOException, Exception
    {
        username = pUsername;
        password = pPassword;
        isAuthenticated = false;
        authenticate();
        retrieveUserInfo();
    }

    /**
     * creates a new User in the LS with ROLE_PLAYER
     * @pre user does not yet exist in LS
     * @pre the password must comply to the password policy, or the request throw an exception
     * @param newUsername the username for the new user
     * @param newPassword the password for the new user
     * @return
     * @throws IOException
     */
    public static User createNewUser(String newUsername, String newPassword, Role newRole) throws Exception
    {

        // check to make sure that password is acceptable by LS
        // throw an exception with method if it is not
        if (!isValidPassword(newPassword)) // calls the method we took from Max's code
        {
            throw new Exception("This password does not fit the LS criteria. Please try a different password.");
        }

        String adminToken = getAccessTokenUsingCreds(adminUsername, adminPassword);

        URL url = new URL("http://35.182.122.111:4242/api/users/" + newUsername + "?access_token=" + adminToken);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("    \"name\": \"" + newUsername + "\",\n");
        out.writeBytes("    \"password\": \"" + newPassword + "\",\n");
        out.writeBytes("    \"preferredColour\": \"01FFFF\",\n");
        out.writeBytes("    \"role\": \"ROLE_PLAYER\"\n");
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

        User created = new User(newUsername, newPassword);
        return created;
    }


    public int authenticate() throws IOException
    {
        URL url = new URL("http://35.182.122.111:4242/oauth/token?grant_type=password&username=" + username + "&password=" + password);

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
        tokenExpiryAsString = json.get("expires_in").toString();
        tokenExpiresIn = Integer.parseInt(tokenExpiryAsString);

        tokenTimeUpdate();
        isAuthenticated = true;

        // System.out.println("The access token is " + accessToken + " and the refresh token is " + refreshToken);
        return status;
    }

    public String getAccessToken() throws IOException
    {
        // if the access token is still valid, then return it and update the value of expires_in
        // else use the api with the refresh token to get a new one

        if (!accessTokenValid())
        {
            accessToken = refreshToken;
            authenticate();
        }

        return accessToken;
    }

    public boolean accessTokenValid() {
        // the access token is valid iff it has not expired
        // we will check if it is still valid by evaluating     tokenIssuedTime + expires_In <= currentTime

        Date currentTime = new Date();
        Calendar currentCal = Calendar.getInstance();
        currentCal.setTime(currentTime);

        if (currentCal.before(authTokenExpiresAt))
        {
            return true;
        }

        return false;
    }

    // this method is designed to be called whenever we (re)authenticate and obtain a token
    // we have to set the values of the fields we use to determine whether an access token is expired
    // we will use Calendar because we want to be able to use its add function
    private void tokenTimeUpdate()
    {
        Date currentTime = new Date(); // current time to add to expiry time
        Calendar current = Calendar.getInstance(); // Calendar is an abstract class. That's why we don't use the constructor
        current.setTime(currentTime);

        // add the expiry time (as seconds) to the current time to see when it will expire
        Calendar expiryTime = Calendar.getInstance();
        expiryTime.setTime(currentTime); // we will add the seconds after
        expiryTime.add(Calendar.SECOND, tokenExpiresIn);

        // now update the field values
        authTokenIssued = current;
        authTokenExpiresAt = expiryTime;

    }

    /**
     * makes an API call to get details about a user on the LS and to set the relevant fields
     * */
    private void retrieveUserInfo() throws IOException, Exception
    {
        // this operation requires admin permissions.
        // we're going to use the workaround method and purposely avoid calling the User constructor inside this method
        // because when we call the constructor in here, it creates an infinite loop of User constructors and hangs forever
        String adminToken = getAccessTokenUsingCreds("maex", "abc123_ABC123");

        // now make the main API call
        URL url = new URL("http://35.182.122.111:4242/api/users/maex?access_token=" + adminToken);
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

        // now, take the output and turn it into a JSON

        JSONObject response = new JSONObject(content.toString());
        infoFromAPI = response;
        // we already have username and password, so we don't need to do anything to those.

        // we are, however, interested in preferred color and role
        preferredColor = response.get("preferredColour").toString();
        String roleString = response.get("role").toString();

        role = interpretRole(roleString); // see line 386


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

    public Role getRole() {return role;}

    private static String escapePlusSign(String token)
    {
        String returnVal = token.replace("+", "%2B");
        return returnVal;
    }

    // TODO: need to implement this method
    public static boolean doesUsernameExist(String username) throws IOException, Exception
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
    public static boolean isValidPassword(String password)
    {
    	return Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}").matcher(password).find();
    }

    public static List<JSONObject> getAllUsers(String adminToken) throws IOException

    {
        
    	URL url = new URL("http://35.182.122.111:4242/api/users?access_token=" + adminToken);
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
        	// System.out.println(j.toString()); I don't think we need to print this
        }
    	
    	return allUsers;
    }

    // used to help us convert the text description of a user's role to a value from our Role enum
    public Role interpretRole(String role) throws Exception
    {
        if (role.equalsIgnoreCase("ROLE_PLAYER"))
        {
            return Role.PLAYER;
        }

        else if (role.equalsIgnoreCase("ROLE_ADMIN"))
        {
            return Role.ADMIN;
        }

        else if (role.equalsIgnoreCase("ROLE_SERVICE"))
        {
            return Role.SERVICE;
        }

        throw new Exception(role + " is not a valid role!");
    }

    // FOR TESTING PURPOSES ONLY
    public void printTokenRelatedFields() throws IOException {
        String token = getAccessToken();
        String refreshToken = getRefreshToken();

        System.out.println("The current access token is " + token);
        System.out.println("The current refresh token is " + refreshToken);

        System.out.println("The token was obtained at " + authTokenIssued);
        System.out.println("The token expires at " + authTokenExpiresAt + ", which should be " + tokenExpiresIn + " seconds from the time it was obtained.");
    }


    // TODO: make sure this method still works once the token is expired
    /**
     * a method to get access token without having to call the User constructor
     * designed to avoid the infinite loop of User constructor called by getUserInfo
     * @param username
     * @param password
     * @return
     */
    public static String getAccessTokenUsingCreds(String username, String password) throws IOException
    {
        URL url = new URL("http://35.182.122.111:4242/oauth/token?grant_type=password&username=" + username + "&password=" + password);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

        // add the credentials, encoded
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

        // now get the token
        String contentString = content.toString();
        JSONObject contentJSON = new JSONObject(contentString);

        String accessToken = escapePlusSign(contentJSON.get("access_token").toString());
        return accessToken;


    }

}