package test;

import networking.GameService;
import networking.User;

import java.io.IOException;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {
        User admin = new User("maex", "abc123_ABC123");
        GameService elfenlands = new GameService()
    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        createNew.printTokenRelatedFields();
    }





}
