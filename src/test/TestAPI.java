package test;

import networking.User;

import java.io.IOException;

public class TestAPI {

    public static void main (String [] args) throws IOException

    {
        String username = "fuck_this";
        String password = "abc123_ABC123";

        testCreateUser(username, password);




    }

    public static void testCreateUser(String username, String password) throws IOException {

        User createNew = User.createNewUser(username, password);
        createNew.printTokenRelatedFields();
    }





}
