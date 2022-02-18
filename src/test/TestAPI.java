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

        ArrayList<String> ids = getAllSessionID();

        for (String id : ids)
        {
            System.out.println("Showing game details for session with id: " + id);
            System.out.println(getSessionDetails(id));
            System.out.println("The player names are: " + getPlayerNames(id));
        }

        MessageDigest md5 = MessageDigest.getInstance("MD5");

        byte [] messageDigest = md5.digest("{}".getBytes(StandardCharsets.UTF_8));

        BigInteger no = new BigInteger(1, messageDigest);

        String hashtext = no.toString(16);

        while (hashtext.length() < 32)
        {
            hashtext = "0" + hashtext;
        }

        System.out.println("The hash is: " + hashtext);



    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();
    }





}
