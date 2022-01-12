package test;

import networking.User;

import java.io.IOException;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {

    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        createNew.printTokenRelatedFields();
    }





}
