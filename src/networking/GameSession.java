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
import java.util.logging.Logger;

public class GameSession {

    private User creator;
    private String gameName; // matches up to a networking.GameService object, but we will not use a networking.GameService object because we just need the name
    private String saveGameName;
    private String locationIP; // this should be the system IP address
    private String id; // this is returned by the API whenever we create a session. we need this to reference and get information about the session later on

    // this class will represent a networking.GameSession
    // its constructor will intialize a networking.GameSession

    /**
     *
     * @param pCreator
     * @param pGameName
     * @param pSaveGameName MUST BE EMPTY IF THERE IS NO EXISTING SAVEGAME
     * @throws Exception
     */
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



    // TODO: allow user to enter savegame name
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

    // TODO: change to a method only to delete unlaunched sessions from the creator's computer
    /**
     * deletes the session from the LS
     * should work for both launched and unlaunched sessions
     * will use admin credentials
     * @param sessionID the session to delete
     * @throws IOException
     */
    public static void delete(String sessionID) throws IOException
    {
        String token;
        if (isLaunched(sessionID))
        {
            // if a session has been launched already, it must be deleted by the game service admin
            Logger.getGlobal().info("I see you are calling GameSession.delete(). This method is not well-adapted to be used in the actual game code.");
            token = User.getAccessTokenUsingCreds("Elfenland_Classic", "abc123_ABC123");
        }
        else
        {
            // if a session has not been launched, we can delete it with the creator
            if (isCreator(User.getInstance(), sessionID))
            {
                token = User.getInstance().getAccessToken();
            }
            else // if the User singleton instance is not the creator, we're out of luck
            {
                return;
            }
        }

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + sessionID + "?access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("DELETE");
        con.setRequestProperty("authorization", "Basic YmdwLWNsaWVudC1uYW1lOmJncC1jbGllbnQtcHc=");

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
     * static method to check if a session has been launched
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

    public static String getSessionsReturnString() throws IOException
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
        // System.out.println("Response status: " + status);
        // System.out.println(content.toString());

        return content.toString();
    }

    public static String getSessionDetailsReturnString (String id) throws IOException
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
        return content.toString();
    }

    public static ArrayList<String> getPlayersFromSessionDetails(String sessionDetailsResponse)
    {
        JSONObject details = new JSONObject(sessionDetailsResponse);
        ArrayList<String> players = new ArrayList<String>();
        JSONArray names = details.optJSONArray("players");

        for (int i = 0; i < names.length(); i++)
        {
            String name = names.getString(i);
            players.add(name);
        }

        return players;
    }

    /**
     * does the same thing as getSessionDetails, except using long polling
     * @param id the id of the session we would like information about
     * @param prevPayload the previous payload (i.e. the last update received)--will be hashed and sent as a parameter
     * @return unlike the synchronous method, this will just return the content and cast it to a string (because we need it for the next call.) we will JSON-ize the information outside of this method
     */
    public static String getSessionDetails(String id, String prevPayload) throws IOException
    {
        String hashedPrevPayload = NetworkUtils.md5Hash(prevPayload.toString());

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + id + "?hash=" + hashedPrevPayload);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();

        // if we get a timeout, just resend the request
        if (status == 408)
        {
            Logger.getGlobal().info("Session details request timed out. Resending it.");
            getSessionDetails(id, prevPayload);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        System.out.println("Response status: " + status);

        return content.toString();
    }

    /**
     * does the same thing as getSessions, except using long polling
     * @param prevPayload the previous getSessions response, which we will hash and pass as a parameter
     * @return once there is updated information(the response was different than the previous one,) return the information
     * @throws IOException
     */
    public static String getSessions(String prevPayload) throws IOException
    {
        String prevPayloadHashed = NetworkUtils.md5Hash(prevPayload);

        URL url = new URL("http://35.182.122.111:4242/api/sessions" + "?hash=" + prevPayloadHashed);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        // if we get a timeout, just resend the request
        if (status == 408)
        {
            Logger.getGlobal().info("GetSessions request timed out. Resending it.");
            getSessions(prevPayload);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        System.out.println("Response status: " + status);
        return content.toString();
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

    /**
     * @param resultsOfGetSessions the String results of a getSessions call
     * @return
     */
    public static ArrayList<String> getSessionIDFromSessions(String resultsOfGetSessions)
    {
        JSONObject results = new JSONObject(resultsOfGetSessions);
        Set keys = results.keySet();
        JSONObject sessions = results.getJSONObject("sessions");
        Set idSet = sessions.keySet();
        ArrayList<String> ids = new ArrayList<String>(idSet);
        return ids;

    }

    /**
     * long version of getAllSessionID that uses a long polling request instead
     * @param prevPayload the previous payload from getSessions api call, to hash
     * @return
     */
    public static ArrayList<String> getAllSessionIDLongPolling (String prevPayload) throws IOException
    {
        JSONObject getSessionsResults = new JSONObject(getSessions(prevPayload));
        Set keys = getSessionsResults.keySet();
        JSONObject sessions = getSessionsResults.getJSONObject("sessions");
        Set idSet = sessions.keySet();
        ArrayList<String> ids = new ArrayList <String> (idSet);
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
    public static void leaveSession(User leaver, String sessionID) throws Exception
    {
        String ip = NetworkUtils.getLocalIPAddPort();

        if (!leaver.getRole().equals(User.Role.PLAYER))
        {
            throw new Exception("Only players can leave games.");
        }
        String token = leaver.getAccessToken();

        URL url = new URL("http://35.182.122.111:4242/api/sessions/" + sessionID + "/players/" + leaver.getUsername() + "?location=" + ip + "&access_token=" + token);
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
