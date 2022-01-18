package test;

import networking.User;

import java.io.IOException;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {
        User admin = new User("maex", "abc123_ABC123");
        String token = admin.getAccessToken().toString();
        System.out.println(token);
    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();

        
    }





}
