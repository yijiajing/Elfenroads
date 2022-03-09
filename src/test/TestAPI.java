package test;

import commands.GameCommand;
import networking.DNSLookup;
import networking.GameService;
import networking.GameSession;
import networking.User;
import utils.NetworkUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

import static networking.GameSession.*;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {
       // GameService elfenroads = new GameService("elfenroads", "elfenroads", "abc123_ABC123", 1, 6);
       // GameService elfenlands = new GameService("elfenlands", "elfenlands", "abc123_ABC123", 2, 6 );
       // GameService longGame = new GameService("longGame", "long game", "abc123_ABC123", 2, 6 );
       // GameService destinationTown = new GameService("destinationTown", "destination town", "abc123_ABC123", 2, 6);
       createASession("Destination");
        // deleteAllSessions();
    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();
    }

    public static void createASession(String variant)
    {
        try {
            User alex = User.init("dontforget", "abc123_ABC123");
            GameSession sesh = new GameSession(alex, variant, "savegame1234");
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

    public static void createGameServices() throws IOException
    {}

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
            User.registerNewUser(person, "abc123_ABC123", User.Role.PLAYER);
        }

        // initialize Elfenroads game service
        GameService elfenroads = new GameService("Elfenroads", "Elfenroads", "abc123_ABC123", 2, 6);

    }







}
