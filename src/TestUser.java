import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
// created new branch nick

public class TestUser {


    public static void main (String [] args) throws IOException
    {

        JSONObject allSessions = GameSession.getSessions();
        String firstSessionID = GameSession.getFirstSessionID(allSessions);
        JSONObject sessionDetails = GameSession.getSessionDetails(firstSessionID);
        JSONObject sessionParameters = GameSession.getGameParameters(firstSessionID);
        System.out.println("The creator of the session is: " + sessionDetails.get("creator"));
        System.out.println("The minimum number of players in the session is " + sessionParameters.get("minSessionPlayers"));





        // JSONObject allSessions = GameSession.getSessions();
        // System.out.println(allSessions);



        System.out.println("The ID of the first session is: " + GameSession.getFirstSessionID(allSessions));

        String username = "maex";
        String password = "abc123_ABC123";
        String sessionName = "Nick's game";

        User maex = new User(username, password); // authenticates test user
        User nick = new User("nick", "Nicholas1234");
        // System.out.println(maex.getAccessToken());

        // System.out.println(User.getAllUsers(maex.getAccessToken()));

        // System.out.println(User.doesUserExist("nick2", newUser));
        // GameService elfenlands = new GameService(maex, "ElfenlandsGame", "Elfenlands", "Cheese12345", 5, 10);
        GameSession session = new GameSession(maex, "ElfenlandsGame" , "savegame12");
        System.out.println("Nick's access token is: " + nick.getAccessToken());
        // System.out.println(GameSession.getAllSessions("Elfenlands"));

       // test the access token validity thing

        // maex.printTokenRelatedFields();

        String sessionIDWithLocalhost = "7425611162837342731";
        String sessionIDWithOldMacIP = "7904626508747853581";

        String newMacIP = "10.0.0.87";
        String oldMacIP = "10.0.0.244";

        String sessionID = session.getId();
        // JSONObject details = GameSession.getSessionDetails(sessionIDWithOldMacIP);

        // System.out.println(details);

       // System.out.println("The fields in the details are: " + details.names());

        ArrayList<String> addresses = GameSession.getPlayerAddresses(sessionID);
        System.out.println(addresses);

        System.out.println("Joining nick into the new session... with id = " + sessionID);
        GameSession.joinSession(nick, sessionID, newMacIP);



        // User charles = new User(charlesUsername, charlesPassword);
    }








}
