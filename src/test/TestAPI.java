package test;

import commands.GameCommand;
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

        // createASession();
        // User nick = User.init("nick", "abc123_ABC123");
        // joinSession(nick, "4501488078472682875");

        //User creator = User.init("dontforget", "abc123_ABC123");
        // GameSession.launch(creator, "4501488078472682875");

        // System.out.println(GameSession.isLaunched("4501488078472682875"));


        for (String id : GameSession.getAllSessionID())
        {
            System.out.println(GameSession.isLaunched(id));
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
