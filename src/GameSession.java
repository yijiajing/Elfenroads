import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;

public class GameSession {

    private User creator;
    private JSONObject info;
    private ArrayList<User> players;
    private boolean launched;
    private ArrayList<String> playerLocations;
    private String sessionName;
    private String saveGameName;


    public GameSession(User pCreator, String pSessionName, String pSaveName) throws IOException
    {
        // this constructor will use the provided User to create a new game session
        // the User must have the appropriate (admin) permissions
        creator = pCreator;
        sessionName = pSessionName;
        saveGameName = pSaveName;
        createGame(creator, sessionName, saveGameName);
    }

    // if no save game name is provided to the constructor, we save the game under its session name
    public GameSession (User pCreator, String pSessionName) throws IOException
    {
        creator = pCreator;
        sessionName = pSessionName;
        saveGameName = pSessionName;
        createGame(creator, sessionName, saveGameName);
    }

    private void createGame(User creator, String pSessionName, String pSaveGameName) throws IOException
    {
        if (creator.isAuthenticated()) {
            URL url = new URL("http://127.0.0.1:4242/api/sessions?access_token=" + creator.getAccessToken());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            /* Payload support */
            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("{\n");
            out.writeBytes("    \"creator\": \"" + creator.getUsername() + "\",\n");
            out.writeBytes("    \"game\": \"" +pSessionName + "\",\n");
            out.writeBytes("    \"savegame\": \"" + pSaveGameName + "\"\n");
            out.writeBytes("}");
            out.flush();
            out.close();

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            System.out.println("Response status: " + status);
            System.out.println(content.toString());
        }
    }




}
