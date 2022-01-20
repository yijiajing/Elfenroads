package networking;

import domain.*;
import org.json.JSONObject;
import panel.GameScreen;
import utils.GameRuleUtils;

import java.util.*;


import java.util.ArrayList;

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

    //Global variable holding the singleton GameState instance
    private static GameState instance = new GameState();

    // meta info (possibly separated into another class in the future)
    private GameMap gameMap;
    private int numPlayers;
    private int totalRounds;
    private List<Player> players = new ArrayList<>();

    // state info
    private int currentRound;
    private RoundPhaseType currentPhase;
    private Player currentPlayer;
    private Road selectedRoad;
    private TransportationCounter selectedCounter;
    private List<TravelCard> selectedCards = new ArrayList<>();
    private Town selectedTown;


    
    //NEED TO IMPLEMENT
    //a default constructor
    private GameState() {
    	
    }

    private ArrayList<ElfBoot> elfBoots;

    public GameState (GameScreen input)

    {
        this.screen = input;
        this.elfBoots = new ArrayList<>();
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
    	return new ArrayList<>(players);
    }
    
    public static GameState instance() {
    	return instance;
    }


    public void addElfBoot(ElfBoot pElfBoot) {
        elfBoots.add(pElfBoot);
    }

    public ArrayList<ElfBoot> getElfBoots() {
        return elfBoots;
    }

    /*
    Selection handling:
    - counter unit + road + PLANROUTES phase -> place a counter unit on the road
    - card + town + MOVE phase -> requested player move to a town
     */
    public Road getSelectedRoad() {
        return selectedRoad;
    }

    public void setSelectedRoad(Road selectedRoad) {
        this.selectedRoad = selectedRoad;

        if (currentPhase == RoundPhaseType.PLANROUTES) {
            //TODO: handle route planning of a player given counter/obstacle selected and road selected
        }
    }

    public TransportationCounter getSelectedCounter() {
        return selectedCounter;
    }

    public void setSelectedCounter(TransportationCounter selectedCounter) {
        this.selectedCounter = selectedCounter;
    }

    public List<TravelCard> getSelectedCards() {
        return selectedCards;
    }

    public void setSelectedCard(List<TravelCard> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public Town getSelectedTown() {
        return selectedTown;
    }

    public void setSelectedTown(Town selectedTown) {
        this.selectedTown = selectedTown;

        if (currentPhase == RoundPhaseType.MOVE) {
            //TODO: handle the move of the current player (and their elfboot)
            // if the cards can enable the player to follow one of the roads to the town
            if (!GameRuleUtils.validateMove(
                    gameMap, gameMap.getTownByName(currentPlayer.getCurrentTownName()), selectedTown, selectedCards
            )) {
                currentPlayer.setCurrentTown(selectedTown.getName());
            } else {
                //TODO: handle invalid move
            }
        }
    }
}
