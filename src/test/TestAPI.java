package test;

import networking.GameService;
import networking.GameSession;
import networking.User;
import utils.NetworkUtils;

import java.io.IOException;

public class TestAPI {

    public static void main (String [] args) throws IOException, Exception

    {

        System.out.println(User.doesUsernameExist("maex"));

        System.out.println(NetworkUtils.ngrokAddrToPassToLS());

        // System.out.println(GameSession.getFirstSessionID());
        // System.out.println(NetworkUtils.getServerInfo());
        // System.out.println(GameSession.getAllSessionID());

        User alex = User.init("alex", "abc123_ABC123");
        GameSession sesh = new GameSession(alex, "testGame", "saveGameName1");
        GameSession sesh2 = new GameSession(alex, "testGame", "saveGame2");




        /* User nick = User.init("nick", "abc123_ABC123");

        for (String id : GameSession.getAllSessionID())
        {
            GameSession.joinSession(nick, id);
        }

        */

        System.out.println(GameSession.getSessions());


    }

    public static void testCreateUser(String username, String password) throws IOException, Exception {

        //User createNew = User.createNewUser(username, password, User.Role.PLAYER);
        //createNew.printTokenRelatedFields();

        
    }





}
