package test;

import org.json.JSONObject;
import networking.*;

import java.io.IOException;
import java.util.ArrayList;
// created new branch nick

public class TestUser {


    public static void main (String [] args) throws IOException
    {


        //networking.User maex = new networking.User("maex", "abc123_ABC123");
        //networking.GameService elfenlands = new networking.GameService (maex, "Elfenlands", "Elfenlands", "Password1", 2, 2);
        //networking.GameSession newGame = new networking.GameSession(maex, "Elfenlands", "savegame2");





        // JSONObject allSessions = networking.GameSession.getSessions();
        // System.out.println(allSessions);



        // System.out.println("The ID of the first session is: " + networking.GameSession.getFirstSessionID(allSessions));

        String username = "maex";
        String password = "abc123_ABC123";
        // String sessionName = "Nick's game";

        User maex = new User(username, password); // authenticates test user
        User nick = new User("nick", "Nicholas1234");
        // System.out.println(maex.getAccessToken());

        // System.out.println(networking.User.getAllUsers(maex.getAccessToken()));

        // System.out.println(networking.User.doesUserExist("nick2", newUser));
        GameService elfenlands = new GameService(maex, "ElfenlandsGame", "Elfenlands", "Cheese12345", 5, 10);
        GameSession session = new GameSession(maex, "ElfenlandsGame" , "savegame12");
        System.out.println("Nick's access token is: " + nick.getAccessToken());
        // System.out.println(networking.GameSession.getAllSessions("Elfenlands"));

       // test the access token validity thing

        // maex.printTokenRelatedFields();

        String sessionIDWithLocalhost = "7425611162837342731";
        String sessionIDWithOldMacIP = "7904626508747853581";

        String newMacIP = "10.0.0.87";
        String oldMacIP = "10.0.0.244";

        String sessionID = session.getId();
        // JSONObject details = networking.GameSession.getSessionDetails(sessionIDWithOldMacIP);

        // System.out.println(details);

       // System.out.println("The fields in the details are: " + details.names());

        ArrayList<String> addresses = GameSession.getPlayerAddresses(sessionID);
        System.out.println(addresses);

        System.out.println("Joining nick into the new session... with id = " + sessionID);
        GameSession.joinSession(nick, sessionID, newMacIP);







        // networking.User charles = new networking.User(charlesUsername, charlesPassword);

    }








}
