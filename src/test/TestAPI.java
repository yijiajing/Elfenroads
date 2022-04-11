package test;

import commands.GameCommand;
import networking.DNSLookup;
import networking.GameService;
import networking.GameSession;
import networking.User;
import org.json.JSONObject;
import utils.NetworkUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.logging.Logger;

import static networking.GameSession.*;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {

        System.out.println(User.getAccessTokenUsingCreds("Elfenland_Classic", "abc123_ABC123"));
        deleteAllSessions();

       //  System.out.println(GameSession.getAllSessionID());

        String saveGameID = "Savegameid1";
        JSONObject sessions = getSessions();
        // parse this, and see if anything matches
        // first, use the key set to get sessions by ID:
        for (String id : getAllSessionID())
        {
            // each of these entries is a whole session's info
            // so we can get saveGameID
            // JSONObject thatSessionInfo = sessions.getJSONObject(id);
            JSONObject details = GameSession.getSessionDetails(id);
            String saveGameID2 = details.getString("savegameid");
            System.out.println(saveGameID2);
            // System.out.print(details);
            // System.out.println(thatSessionInfo);
            // System.out.println(thatSessionInfo.keySet());
            // System.out.println(thatSessionInfo.getString(id));
            // String thatSessionSaveGameName = thatSessionInfo.getString("savegameid");
            /*if (thatSessionSaveGameName.equals(saveGameID))
            {
                // System.out.println("The game has id " + id);
            }

             */

        }

        // JSONObject details = GameSession.getSessionDetails()
    }

        // System.out.println(getSessions());

       //  System.out.println(User.getAccessTokenUsingCreds("maex", "abc123_ABC123"));
        // deleteAllSessions();
        // testCreateUser("user_test", "abc123_ABC123");

        /*
        initializeGameServiceWithStandardSettings("Elfenland_Classic", "Elfenland (Classic)" );
        initializeGameServiceWithStandardSettings("Elfenland_Long", "Elfenland (Long)");
        initializeGameServiceWithStandardSettings("Elfenland_Destination", "Elfenland (Destination)");
        initializeGameServiceWithStandardSettings("Elfengold_Classic", "Elfengold (Classic)");
        initializeGameServiceWithStandardSettings("Elfengold_Destination", "Elfengold (Destination)");
        initializeGameServiceWithStandardSettings("Elfengold_RandomGold", "Elfengold (Random Gold)");
        initializeGameServiceWithStandardSettings("Elfengold_Witch", "Elfengold (Witch)");

         */


    public static void testCreateUser(String username, String password) throws IOException, Exception {

        User.registerNewPlayer(username, password);
        User.logout();
        User createNew = User.init(username, password);
        createNew.printTokenRelatedFields();
    }

    public static void createASession(String variant)
    {
        try {
            User alex = User.init("alex", "abc123_ABC123");
            GameSession sesh1 = new GameSession(alex, variant, "savegame1234");
        }
        catch (Exception e)
        {
            System.out.println("There was a problem setting up the test game session.");
        }
    }

    public static void deleteAllSessions() throws IOException
    {
        ArrayList<String> ids = getAllSessionID();
        for (String id : ids)
        {
            try {delete(id);}
            catch (Exception e) {continue;}
        }

    }

    public static void testLongPolling() throws IOException
    {
        String gameID = getFirstSessionID();
        String prevPayload = "";

        while (true)
        {
            prevPayload = GameSession.getSessionDetails(gameID, prevPayload);
            System.out.println("Received new information!");
            System.out.println(prevPayload);
        }
    }

    /**
     * designed to be called whenever we have to reset the LS. repopulates Users and the GameServices we are using
     * @pre none of the users already exist in the LS
     */
    public static void reInitializeAPI() throws Exception
    {
        // add users
        ArrayList<String> groupMembers = new ArrayList<String>();
        groupMembers.add("nick");
        groupMembers.add("charles");
        groupMembers.add("philippe");
        groupMembers.add("chloe");
        groupMembers.add("huasen");
        groupMembers.add("yijia");

        for (String person : groupMembers)
        {
            // User.registerNewUser(person, "abc123_ABC123", User.Role.PLAYER);
        }

        // initialize game services
        GameService destination = new GameService("Destination", "Destination", "abc123_ABC123", 2, 6);
        User.logout();
        GameService classic = new GameService("elfenlands", "elfenlands", "abc123_ABC123", 2,6);
        User.logout();
        GameService elfLong = new GameService("Elfenland(Long)", "Elfenland(Long)", "abc123_ABC123", 2,6);
        User.logout();

        // GameService
    }

    // will initialize all the game services we need
    public static void initializeGameServiceWithStandardSettings (String name, String displayName) {
        User.logout();
        try {
            GameService created = new GameService(name, displayName, "abc123_ABC123", 2, 6);
        } catch (Exception e) {
            Logger.getGlobal().info("There was a problem creating a GameService for " + name);
        }
    }

    public static String getNickAccessToken() throws Exception
    {
        return User.getAccessTokenUsingCreds("nick", "abc123_ABC123");
    }

    public static String createGameWithOtherUserReturnID() throws Exception
    {
        User.logout();
        User.init("dontforget", "abc123_ABC123");
        User other = User.getInstance();
        GameSession sesh = new GameSession(User.getInstance(), "Elfenland_Classic", "My Save Game Name");
        return sesh.getId();
    }

    @Deprecated
    public static void joinGameWithNick(String id) throws Exception
    {
        User.logout();
        User.init("nick", "abc123_ABC123");
        User nick = User.getInstance();
        // GameSession.joinSession(nick, id);
    }

    public static void leaveGameWithNick(String id) throws Exception
    {
        User.logout();
        User.init("nick", "abc123_ABC123");
        User nick = User.getInstance();
        GameSession.leaveSession(nick, id);
    }






}
