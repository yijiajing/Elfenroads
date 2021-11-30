import java.io.IOException;

public class TestUser {


    public static void main (String [] args) throws IOException
    {
        String username = "test";
        String password = "Password1234";
        String sessionName = "Nick's game";

        User newUser = new User(username, password); // authenticates test user
        System.out.println(newUser.getAccessToken());

        // System.out.println(User.doesUserExist("nick2", newUser));
        GameService dumbGame = new GameService(newUser, "ElfenlandsGame", "Elfenlands", "Cheese12345", 5, 10);
    }








}
