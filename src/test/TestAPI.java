package test;

import networking.DNSLookup;
import networking.GameService;
import networking.GameSession;
import networking.User;
import utils.NetworkUtils;

import java.io.IOException;
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



    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();

        
    }





}
