import java.io.IOException;

public class TestUser {


    public static void main (String [] args) throws IOException
    {
        String username = "maex";
        String password = "abc123_ABC123";
        String sessionName = "Nick's game";

        User newUser = new User(username, password);

        GameService dumbGame = new GameService(newUser, "Dumbgamemadebynick", "cheese", 5, 10, "Nick's game");
    }








}
