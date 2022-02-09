package test;

import networking.GameService;
import networking.GameSession;
import networking.User;

import java.io.IOException;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {

        System.out.println(User.doesUsernameExist("maex"));

        System.out.println(GameSession.getSessions());


        /* User admin = new User("maex", "abc123_ABC123");
        String token = admin.getAccessToken().toString();
        System.out.println(token);

         */


    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();

        
    }





}
