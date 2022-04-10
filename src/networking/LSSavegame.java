package networking;


// named like this to distinguish between this class and Savegames.Savegame
// used for savegames in the API
// will just be a bunch of static methods because we don't really need to objectify this

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LSSavegame {

    // methods from API:

    // get savegames by gameService

    /**
     * returns a list of all savegame ID for this gameservice
     * @param gameServiceName the name of the game on the LS (not the display name)
     * @return a list of all savegameIDs for that gameService
     */
    // TODO: update to take a game variant as input instead?
    public static ArrayList<String> getAllSavegames(String gameServiceName) throws IOException
    {
        ArrayList<String> out = new ArrayList<>();
        // send the API request
        // we will just use the admin credentials for this. no reason to restrict it
        String adminToken = User.getAccessTokenUsingCreds("maex", "abc123_ABC123");

        URL url = new URL("http://35.182.122.111:4242/api/gameservices/" + gameServiceName + "/savegames?access_token=" + adminToken);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

        JSONArray contentJSON = new JSONArray(content.toString());
        for (Object cur : contentJSON)
        {
            JSONObject curJSON = new JSONObject(cur);
            // get the save game ID from the JSON
            out.add(curJSON.getString("savegameid"));
        }

        return out;

    }

    // delete savegames by gameService

    // register a savegame

    public static void registerSaveGame()
    {

    }

    // delete a specific savegame






}
