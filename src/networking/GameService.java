package networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GameService {

    // this class represents a networking.GameService on the lobby system
    // this is different from a networking.GameSession.
    // we need a networking.GameService in order to create a networking.GameSession with that game
    // but a networking.GameService can only be created by a user with the service role

    private User gameServiceUser;
    private User adminUser;
    private String gameServiceAccountPassword;
    private String gameServiceAccountColor = "01FFFF";
    private String gameServiceName;
    private int minSessionPlayers;
    private int maxSessionPlayers;
    private String gameDisplayName;


    public GameService (User pAdminUser, String pGameServiceName, String pGameDisplayName, String pGameServiceAccountPassword, int pMinSessionPlayers, int pMaxSessionPlayers) throws IOException, Exception
    {
        // first, we need to create a user to manage the networking.GameService
        adminUser = pAdminUser;
        gameServiceName = pGameServiceName;
        gameServiceAccountPassword = pGameServiceAccountPassword;
        minSessionPlayers = 2;
        maxSessionPlayers = 2;
        gameDisplayName = pGameDisplayName;
        createGameServiceUser();
        // now, we have created the game service user and we can go on to create the actual game service (using that user)
        createGameService();

    }

    public void createGameServiceUser() throws IOException, Exception
    {
        // first, check if the service user already exists. if we try to create a user that already exists, we will get an exception
        boolean usernameTaken = User.doesUsernameExist(gameServiceName);
        if (usernameTaken) // TODO: handle the case in which the username is taken already, which probably won't happen in our implementation
        {}
        // if a user does not already exist, we will just create one

        else
        {
            // this method will make a call to Users and create a user with the service role
            URL url = new URL("http://35.182.122.111:4242/api/users/" + gameServiceName + "?access_token=" + adminUser.getAccessToken());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");

            /* Payload support */
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("{\n");
            out.writeBytes("    \"name\": \"" + gameServiceName + "\",\n");
            out.writeBytes("    \"password\": \"" + gameServiceAccountPassword + "\",\n");
            out.writeBytes("    \"preferredColour\": \"01FFFF\",\n");
            out.writeBytes("    \"role\": \"ROLE_SERVICE\"\n");
            out.writeBytes("}");
            out.flush();
            out.close();

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println("Response status: " + status);
            System.out.println(content.toString());

           //  User gameServiceUser = new User(gameServiceName, gameServiceAccountPassword);
            // this.gameServiceUser = gameServiceUser;
            // System.out.println("The token for the gameServiceUser is: " + this.gameServiceUser.getAccessToken());
        }


        // TODO: COME BACK AND FIX THIS!!!
        // User gameServiceUser = new User(gameServiceName, gameServiceAccountPassword);
        //is.gameServiceUser = gameServiceUser;
        // System.out.println("The token for the gameServiceUser is: " + this.gameServiceUser.getAccessToken());

    }

    public void createGameService() throws IOException
    {
        String token = gameServiceUser.getAccessToken();
        URL url = new URL("http://35.182.122.111:4242/api/gameservices/" + gameServiceName + "?access_token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("PUT");
        con.setRequestProperty("Content-Type", "application/json");

        /* Payload support */
        con.setDoOutput(true);
        DataOutputStream out = new DataOutputStream(con.getOutputStream());
        out.writeBytes("{\n");
        out.writeBytes("    \"location\": \"\",\n");
        out.writeBytes("    \"maxSessionPlayers\": \"" + maxSessionPlayers + "\",\n");
        out.writeBytes("    \"minSessionPlayers\": \"" + minSessionPlayers + "\",\n");
        out.writeBytes("    \"name\": \"" + gameServiceName + "\",\n");
        out.writeBytes("    \"displayName\": \"" + gameDisplayName + "\",\n");
        out.writeBytes("    \"webSupport\": \"true\"\n");
        out.writeBytes("}");
        out.flush();
        out.close();

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();
        System.out.println("Response status: " + status);
        System.out.println(content.toString());

    }









}
