import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.net.InetAddress;
import java.util.Set;

public class GameSession {

    private User creator;
    private String gameName; // matches up to a GameService object, but we will not use a GameService object because we just need the name
    private String saveGameName;
    private String locationIP; // this should be the system IP address
    private String id; // this is returned by the API whenever we create a session. we need this to reference and get information about the session later on

    // this class will represent a GameSession
    // its constructor will intialize a GameSession


    public GameSession(User pCreator, String pGameName, String pSaveGameName) throws IOException
    {
        creator = pCreator;
        gameName = pGameName;
        saveGameName = pSaveGameName;
        InetAddress ip = InetAddress.getLocalHost();
        locationIP = ip.getHostAddress();
        createNewSession();
    }



    private void createNewSession() throws IOException
    {
        String token = creator.getAccessToken();


        // TODO: change that line back after testing
        // URL url = new URL("http://127.0.0.1:4242/api/sessions?access_token=" + token + "&location=" + locationIP);
        URL url = new URL("http://127.0.0.1:4242/api/sessions?access_token=" + token + "&location=10.0.0.244");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("    \"creator\": \"" + creator.getUsername() + "\",\n");
        out.writeBytes("    \"game\": \"" + gameName + "\",\n");
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

        id = content.toString();
        System.out.println("The session ID is " + id);

    }

    public void launch() throws IOException
    {
        String creatorToken = creator.getAccessToken();

        URL url = new URL("http://127.0.0.1:4242/api/sessions/" + id + "?access_token=" + creatorToken);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");

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


    }


    public User getCreator() {
        return creator;
    }

    public String getGameName() {
        return gameName;
    }

    public String getSaveGameName() {
        return saveGameName;
    }

    // TODO: implement
    public static ArrayList<String> getPlayerAddresses(String id) throws IOException
    {
        // parse the session info to get enough info about the players
        // we only really need their IPs

        ArrayList<String> addresses = new ArrayList<String>();

        JSONObject details = getSessionDetails(id);
        // JSONObject

        JSONObject playersAndIPS = new JSONObject(details.get("playerLocations").toString());

        for (String player : playersAndIPS.keySet())
        {
            String playerAddress = playersAndIPS.get(player).toString();
            addresses.add(playerAddress);
        }

        return addresses;
    }


    public static JSONObject getSessionDetails(String id) throws IOException
    {
        URL url = new URL("http://127.0.0.1:4242/api/sessions/" + id);
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
        // System.out.println(content.toString());

        JSONObject details = new JSONObject(content.toString());

        return details;

    }

    public static JSONObject getGameParameters(String id) throws IOException
    {
        JSONObject gameDetails = getSessionDetails(id);
        JSONObject parameters = new JSONObject(gameDetails.get("gameParameters").toString());
        return parameters;
    }

    public static String getFirstSessionID(JSONObject resultsOfGetSessions)
    {
        Set keys = resultsOfGetSessions.keySet();

        int counter = 0;

        for(Object key : keys)
        {
            String keyString = key.toString();
           JSONObject nested = new JSONObject (resultsOfGetSessions.get(keyString).toString());

           Set nestedKeys = nested.keySet();

           for (Object nestedKey : nestedKeys)
           {
               return nestedKey.toString();
           }

        }
        return "000000000000";
    }

    public static JSONObject getSessions() throws IOException
    {
        URL url = new URL("http://127.0.0.1:4242/api/sessions");
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
        System.out.println(content.toString());

        JSONObject response = new JSONObject(content.toString());

        return response;
    }

    /*
    public static List<JSONObject> getSessions() throws IOException
    {
    	URL url = new URL("http://127.0.0.1:4242/api/gameservices");
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
        String session = "";
        List<JSONObject> allSessions = new ArrayList<>();
        String sessionAsString = content.toString();
        
        int flag = 0;
        for (int i = 0; i < content.length(); i++)
        {
        	if (sessionAsString.charAt(i) == '}')
        	{
        		session += sessionAsString.charAt(i);
        		flag = 0;
        		
        		String str = session.toString();
        		JSONObject json = new JSONObject(str);
        		session = "";
        		allSessions.add(json);
        		continue;
        	}
        	
        	if (flag == 1)
        	{
        		session += sessionAsString.charAt(i);
        		continue;
        	}
        	
        	if (sessionAsString.charAt(i) == '{')
        	{
        		session += sessionAsString.charAt(i);
        		flag = 1;
        		continue;
        	}
        	
        }
        
        for (JSONObject j : allSessions)
        {
        	System.out.println(j.toString());
        }
    	
    	return allSessions;
  
    }

     */

    public String getLocationIP() {
        return locationIP;
    }

    public String getId() {
        return id;
    }

    /**
     * @pre joiner must have role player
     *
     * @param joiner
     */
    public static void joinSession(User joiner, String sessionID, String joinerIP) throws IOException
    {
        // we need to join this session with the chosen user, so we will
        String token = joiner.getAccessToken();

        URL url = new URL("http://127.0.0.1:4242/api/sessions" + sessionID +"/players/" + joiner.getUsername() + "?location=" + joinerIP + "&access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");

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



    }
}
