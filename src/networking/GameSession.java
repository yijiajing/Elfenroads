package networking;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.NetworkUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Set;

public class GameSession {

    private User creator;
    private String gameName; // matches up to a networking.GameService object, but we will not use a networking.GameService object because we just need the name
    private String saveGameName;
    private String locationIP; // this should be the system IP address
    private String id; // this is returned by the API whenever we create a session. we need this to reference and get information about the session later on

    // this class will represent a networking.GameSession
    // its constructor will intialize a networking.GameSession


    public GameSession(User pCreator, String pGameName, String pSaveGameName) throws Exception
    {
        creator = pCreator;
        gameName = pGameName;
        saveGameName = pSaveGameName;
        // locationIP = NetworkUtils.ngrokAddrToPassToLS();
        // changed this 03/02 because ngrok addresses are changing. local multiplayer only for now
        locationIP = NetworkUtils.getLocalIPAddPort();
        createNewSession();
    }



    private void createNewSession() throws IOException
    {
        String token = creator.getAccessToken();


        URL url = new URL("http://35.182.122.111:4242/api/sessions?access_token=" + token + "&location=" + locationIP);

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

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + id + "?access_token=" + creatorToken);
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

    /**
     * alternative static version of launch() that takes the required information as arguments instead
     * called from HostWaitingWindow where we can assume the User instance is the creator of the game (since we have already checked)
     * @throws IOException
     */
    public static void launch(User creator, String sessionID) throws IOException
    {
        String creatorToken = creator.getAccessToken();

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + sessionID + "?access_token=" + creatorToken);
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

    /**
     * deletes the session from the LS
     * should work for both launched and unlaunched sessions
     * will use admin credentials
     * @param sessionID the session to delete
     * @throws IOException
     */
    public static void delete(String sessionID) throws IOException
    {
        String adminToken = User.getAccessTokenUsingCreds("maex", "abc123_ABC123");

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + sessionID + "?access_token=" + adminToken);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");

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

    /**
     * static method to check if a session has been lost
     * @param sessionID the id of the session to check
     * @return
     */
    public static boolean isLaunched(String sessionID) throws IOException
    {
        JSONObject details = getSessionDetails(sessionID);
        String isLaunched = details.get("launched").toString();
        boolean launched = Boolean.parseBoolean(isLaunched);
        return launched;
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


    /**
     *
     * @param id the id of the game session we want information for
     * @return all IPs of the players in the session
     * @throws IOException
     */
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
    /**
     *
     * @param id the id of the session we want to access
     * @return a list of all of the players (by name) in the sessions
     */
    public static ArrayList<String> getPlayerNames(String id) throws IOException
    {
        ArrayList<String> players = new ArrayList<String>();
        JSONObject details = getSessionDetails(id);
        //JSONObject playersAndIPS = new JSONObject(details.get("playerLocations").toString());
        //JSONObject playersAndIPS = new JSONObject(details.get("players").toString());
        JSONArray names = details.optJSONArray("players");
        
        for (int i = 0; i < names.length(); i++)
        {
            String name = names.getString(i);
            players.add(name);
        }

        // for some reason, the players array in the server response doesn't show all the players. so, we are getting the information from playerLocations instead because
        // that one seems to populate fine

        /*for (String player : names)
        {
            players.add(player);
        }*/

        return players;
    }

    public static JSONObject getSessionDetails(String id) throws IOException
    {
        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + id);
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

    public static String getFirstSessionID() throws IOException
    {
        JSONObject resultsOfGetSessions = getSessions();
        Set keys = resultsOfGetSessions.keySet();

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

    /**
     * very similar to the above, but gets a list of session IDs instead of just one
     * @return
     */
    public static ArrayList<String> getAllSessionID() throws IOException
    {
        JSONObject resultsOfGetSessions = getSessions();

        Set keys = resultsOfGetSessions.keySet();

        // the top-level key in the response is just "sessions"
        // the second-level is the IDs

        JSONObject sessions = resultsOfGetSessions.getJSONObject("sessions");
        Set idSet = sessions.keySet();
        ArrayList<String> ids = new ArrayList<String>(idSet);
        return ids;


    }

    public static JSONObject getSessions() throws IOException
    {
        URL url = new URL("http://35.182.122.111:4242/api/sessions");
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

        JSONObject response = new JSONObject(content.toString());

        return response;
    }


    public String getLocationIP() {
        return locationIP;
    }

    public String getId() {
        return id;
    }

    /**
     * @pre joiner must have role player
     * @pre ngrok has been initialized and verified
     *
     * @param joiner
     */
    public static void joinSession(User joiner, String sessionID) throws Exception
    {

        if (!joiner.getRole().equals(User.Role.PLAYER))
        {
            throw new Exception ("Only players can join games.");
        }

        String token = joiner.getAccessToken();

        // get ip to pass
        //String ip = NetworkUtils.ngrokAddrToPassToLS();
        // 03/02 changing to pass local address to ls instead
        String ip = NetworkUtils.getLocalIPAddPort();

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + sessionID + "/players/" + joiner.getUsername() + "?location=" + ip + "&access_token=" + token);
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

    /**
     * @pre the player is in the session
     * @pre the session has not been launched yet
     * remove the player from a session
     */
    public static void leaveSession(User leaver, String sessionID) throws IOException
    {
        String token = leaver.getAccessToken();

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + sessionID + "/players/" + leaver.getUsername() + "&access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");

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

    /**
     * @param id the sessionID of the game
     * @return a HashMap with players' names as the keys and their respective ips as the values
     */
    public static HashMap<String, String> getPlayersWithLocations(String id) throws Exception
    {
        // parse the session info to get enough info about the players
        // we only really need their IPs

        HashMap<String, String> playerNamesAndAddresses = new HashMap<String, String>();

        JSONObject details = getSessionDetails(id);

        JSONObject playersAndIPS = new JSONObject(details.get("playerLocations").toString());

        for (String playerName : playersAndIPS.keySet())
        {
            String playerAddress = playersAndIPS.get(playerName).toString();
            playerNamesAndAddresses.put(playerName, playerAddress);
        }

        return playerNamesAndAddresses;
    }

    public static boolean isCreator(User u, String sessionID) {
        try {
            JSONObject deets = getSessionDetails(sessionID);
            String creatorName = deets.getString("creator");
            return creatorName.equals(u.getUsername());
        } catch (IOException prob) {
            prob.printStackTrace();
        }
        return false;
    }
}
