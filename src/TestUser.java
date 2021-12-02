import java.io.IOException;
// created new branch nick

public class TestUser {


    public static void main (String [] args) throws IOException
    {
        String username = "maex";
        String password = "abc123_ABC123";
        String sessionName = "Nick's game";

        User maex = new User(username, password); // authenticates test user
        // System.out.println(maex.getAccessToken());

        System.out.println(User.getAllUsers(maex.getAccessToken()));

        // System.out.println(User.doesUserExist("nick2", newUser));
        // GameService elfenlands = new GameService(maex, "ElfenlandsGame", "Elfenlands", "Cheese12345", 5, 10);
        // GameSession session = new GameSession(maex, "ElfenlandsGame" , "savegame12");
        // System.out.println(GameSession.getAllSessions("Elfenlands"));

        String charlesUsername = "charles";
        String charlesPassword = "Password1234";

        // User charles = new User(charlesUsername, charlesPassword);
    }








}
