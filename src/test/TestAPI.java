package Test;

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

        //GameService testGame2 = new GameService("testGame2", "testGame2", "abc123_ABC123",2, 6);
        //GameService destination = new GameService("Destination", "Destination", "abc123_ABC123",2, 6);
        //GameService longvar = new GameService("Elfenland(Long)", "Elfenland(Long)", "abc123_ABC123",2, 6);
        //GameService longvar = new GameService("Elfenland", "Elfenland", "abc123_ABC123",2, 6);

       //  User.init()
        //GameService destination = new GameService("Destination", "Destination", "abc123_ABC123", 2, 6);
        // GameService testGame2 = new GameService("testGame2", "testGame2", "abc123_ABC123",2, 6);

        // GameService elfenlands = new GameService("Elfenroads", "Elfenroads", "abc123_ABC123", 2, 6);
        //User.init("dontforget", "abc123_ABC123");
       /* ArrayList<String> sessions = getAllSessionID();

        for (String id : sessions)
        {
            System.out.println(getPlayersWithLocations(id));
        }

        */
    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();
    }

    public static void createASession()
    {
        try {
            User alex = User.init("dontforget", "abc123_ABC123");
            GameSession sesh = new GameSession(alex, "elfenlands", "savegame1234");
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







}
