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

    // Global variable holding the singleton GameState instance
    private static GameState instance;

    // meta info (possibly separated into another class in the future)
    private GameMap gameMap;
    private int totalRounds;
    private List<Player> players = new ArrayList<>();

    // state info
    private int currentRound;
    private RoundPhaseType currentPhase;
    private Player currentPlayer;
    private int currentPlayerIdx;

    private Road selectedRoad;
    private TransportationCounter selectedCounter;
    private List<TravelCard> selectedCards = new ArrayList<>();
    private Town selectedTown;
    boolean obstacleSelected;

    
    //NEED TO IMPLEMENT
    //a default constructor
    /*private GameState() {
    	
    }*/

    private ArrayList<ElfBoot> elfBoots;

    public GameState (GameScreen pScreen)
    {
        this.screen = pScreen;
        this.elfBoots = new ArrayList<>();
        this.obstacleSelected = false;

        setDummyPlayers(); // TODO remove
        this.currentPlayerIdx = 0;
        this.currentPlayer = players.get(currentPlayerIdx);
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

    // TODO: remove this method, only using it for testing of UI before networking stuff is set up
    public void setDummyPlayers() {
        players.add(new Player("black", screen));
        players.add(new Player("blue", screen));
        players.add(new Player("green", screen));
        players.add(new Player("purple", screen));
        players.add(new Player("red", screen));
        players.add(new Player("yellow", screen));
    }
    
    public static GameState instance(GameScreen pScreen) {
        if (instance == null) {
            instance = new GameState(pScreen);
        }
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
            // Player intends to place an obstacle
            if (obstacleSelected) {
                if (!selectedRoad.placeObstacle()) {
                    //TODO: display failure message and deselect obstacle in UI
                }
            }
            // Player intends to place a transportation counter
            if (!selectedRoad.setTransportationCounter(selectedCounter)) {
                //TODO: display failure message and deselect counter in UI
            }
        }
    }

    public void obstacleSelected() {
        obstacleSelected = true;
        selectedCounter = null;
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

    public void addSelectedCard(TravelCard selectedCard) {
        this.selectedCards.add(selectedCard);
    }

    public void setSelectedCard(List<TravelCard> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public Town getSelectedTown() {
        return selectedTown;
    }

    public void setSelectedTown(Town selectedTown) {
        this.selectedTown = selectedTown;

        if (currentPhase == RoundPhaseType.MOVE && !selectedCards.isEmpty()) {
            if (!GameRuleUtils.validateMove(gameMap, currentPlayer.getCurrentTown(), selectedTown, selectedCards)) {
                currentPlayer.setCurrentTown(selectedTown);
            } else {
                //TODO: in UI
                // - deselect all currently selected cards in UI
                // - display message that prompts Player for re-selection
                this.selectedTown = null;
                this.selectedCards.clear();
            }
        }
    }

    public Player getPlayerByColour(String colour) {
        for ( Player p : players ) {
            if (p.getColour().equalsIgnoreCase(colour)) {
                return p;
            }
        }

        return null;
    }

    // totalRounds Round <--in-- numOfRoundPhaseType Phases <--in-- numOfPlayer Turns
    public void endTurn() {
        currentPlayerIdx++;
        clearSelection();

        // all players have passed their turn in the current phase
        if (currentPlayerIdx == players.size()) {
            int nextOrdinal = currentPhase.ordinal() + 1;
            if (nextOrdinal == RoundPhaseType.values().length) {
                // all phases are done, go to the next round
                endRound();
            } else {
                // go to the next phase within the same round
                currentPhase = RoundPhaseType.values()[nextOrdinal];
            }
            return;
        }

        // within the same phase, next player will take action
        currentPlayer = players.get(currentPlayerIdx);
    }

    private void endRound() {
        currentPlayerIdx = 0;
        currentPlayer = players.get(currentPlayerIdx);
        currentRound++;
        if (currentRound == totalRounds) {
            endGame();
            return;
        }
        //TODO: update round card in UI
    }

    private void endGame() {
        //TODO: finishes game ending
    }

    /**
     * Clears all selection states.
     * Whenever a new selection state is added to GameState, remember to clear it here.
     */
    private void clearSelection() {
        selectedRoad = null;
        selectedCounter = null;
        selectedCards.clear();
        selectedTown = null;
        obstacleSelected = false;
    }
}
