package networking;

import org.json.JSONObject;
import panel.GameScreen;
import java.util.*;
import domain.Player;


public class GameState {

    // this class will contain all of the information we need to send regarding game state
    // created this class so that I can easily serialize and send important info to each computer
    // for now, this will only keep track of boot locations and towns visited because that's all we need for the demo

    // TODO: probably get rid of this class entirely, for now


    /* as of now, we need to keep track of and be able to serialize:
    1. which Elfen towns have been visited (show on the UI by the town pieces)
    2. where each player's boot is
    3. 
     */

    private GameScreen screen;
    private JSONObject serialized;
    private List<Player> aPlayers = new ArrayList<>();

    //Global variable holding the singleton GameState instance
    private static GameState instance = new GameState();
    
    //NEED TO IMPLEMENT
    //a default constructor
    private GameState() {
    	
    }
    
    private GameState (GameScreen input)
    {
        this.screen = input;

    }

    // TODO: implement this second constructor
    // public networking.GameState (JSONObject gameStateJSON)

    public JSONObject serialize()
    {
        // we will call the JSONObject constructor with the networking.GameState object as an argument
        // that class comes from org.json (see documentation for details)
        JSONObject serializedVersion = new JSONObject(this);
        serialized = serializedVersion;
        return serialized;
    }
    
   
    /**
     * @return an arrayList containing all Players in this game
     */
    public List<Player> getPlayers(){
    	return new ArrayList<>(aPlayers);
    }
    
    public static GameState instance() {
    	return instance;
    }

  



}
