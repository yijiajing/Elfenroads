package test;

import networking.DNSLookup;
import networking.GameService;
import networking.GameSession;
import networking.User;
import utils.NetworkUtils;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;

import static networking.GameSession.*;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {

        createASession();

        ArrayList<String> ids = getAllSessionID();

        for (String id : ids)
        {
            // System.out.println("Showing game details for session with id: " + id);
            // System.out.println(getSessionDetails(id));
            //  System.out.println("The player names are: " + getPlayerNames(id));
            System.out.println("The creator of the game with id " + id + " is " + getSessionDetails(id).get("creator"));
        }



    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();
    }

    public static void createASession()
    {
        try {
            User alex = User.init("dontforget", "abc123_ABC123");
            GameSession sesh = new GameSession(alex, "testGame", "savegame1234");
        }
        catch (Exception e)
        {
            System.out.println("There was a problem setting up the test game session.");
        }


    }





}
