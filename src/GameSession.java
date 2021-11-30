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
    private User gameServiceUser;
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
        createGameSession(creator, sessionName, saveGameName);
    }

    // if no save game name is provided to the constructor, we save the game under its session name
    public GameSession (User pCreator, String pSessionName) throws IOException
    {
        creator = pCreator;
        sessionName = pSessionName;
        saveGameName = pSessionName;
        createGameSession(creator, sessionName, saveGameName);
    }

    private void createGameSession(User admin, String pSessionName, String pSaveGameName) throws IOException
    {
        // create a new game session with the specifications

    }










}
