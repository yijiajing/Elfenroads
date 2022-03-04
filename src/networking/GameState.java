package networking;

import domain.*;
import enums.Colour;
import enums.RoundPhaseType;
import enums.TravelCardType;
import org.json.JSONObject;
import panel.ElfBootPanel;
import panel.GameScreen;

import java.util.*;


import java.util.ArrayList;

public class GameState {

    // this class will contain all of the information we need to send regarding game state
    // created this class so that I can easily serialize and send important info to each computer
    // for now, this will only keep track of boot locations and towns visited because that's all we need for the demo

    /* as of now, we need to keep track of and be able to serialize:
    1. which Elfen towns have been visited (show on the UI by the town pieces)
    2. where each player's boot is
    3. 
     */

    private JSONObject serialized;

    // Global variable holding the singleton GameState instance
    private static GameState instance;

    // meta info (possibly separated into another class in the future)
    private int totalRounds;
    private List<Player> players = new ArrayList<>();

    // state info
    private int currentRound;
    private RoundPhaseType currentPhase;
    private Player currentPlayer;

    private TravelCardDeck travelCardDeck;
    private CounterPile counterPile;
    private ArrayList<TransportationCounter> faceUpCounters = new ArrayList<>();

    private ArrayList<ElfBoot> elfBoots;

    private GameState (int numRounds)
    {
        this.elfBoots = new ArrayList<>();
        this.currentRound = 1;
        this.totalRounds = numRounds;
        this.travelCardDeck = new TravelCardDeck();
        this.counterPile = new CounterPile();
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
        players.add(new Player(Colour.BLACK));
        players.add(new Player(Colour.BLUE));
        players.add(new Player(Colour.GREEN));
        players.add(new Player(Colour.PURPLE));
        players.add(new Player(Colour.RED));
        players.add(new Player(Colour.YELLOW));
    }
    
    public static GameState init(int numRounds) {
        if (instance == null) {
            instance = new GameState(numRounds);
        }
    	return instance;
    }

    public static GameState instance() {
        return instance;
    }

    public ArrayList<ElfBoot> getElfBoots() {
        return elfBoots;
    }

    public Player getPlayerByColour(Colour colour) {
        for ( Player p : players ) {
            if (p.getColour() == colour) {
                return p;
            }
        }

        return null;
    }

    public ElfBoot getBootByColour(Colour colour) {
        for ( ElfBoot e : elfBoots ) {
            if (e.getColour() == colour) {
                return e;
            }
        }

        return null;
    }

    public int getTotalRounds() {
        return totalRounds;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public void incrementCurrentRound() {
        currentRound++;
    }

    public RoundPhaseType getCurrentPhase() {
        return currentPhase;
    }

    public void setCurrentPhase(RoundPhaseType currentPhase) {
        this.currentPhase = currentPhase;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentPlayerIdx() {
        return players.indexOf(currentPlayer);
    }

    public int getNumOfPlayers() {
        return players.size();
    }

    public void setToNextPlayer() {
        assert getCurrentPlayerIdx() + 1 < players.size();
        currentPlayer = players.get(getCurrentPlayerIdx() + 1);
    }

    public void setToFirstPlayer() {
        currentPlayer = players.get(0);
    }

    public void addElfBoot(ElfBoot boot) {
        this.elfBoots.add(boot);
    }

    public TravelCardDeck getTravelCardDeck() {
        return travelCardDeck;
    }

    public ArrayList<TransportationCounter> getFaceUpCounters() {
        return this.faceUpCounters;
    }

    public CounterPile getCounterPile() {
        return this.counterPile;
    }

    public void addFaceUpCounterFromPile() {
        if (counterPile.getSize() > 0) {
            TransportationCounter counter = counterPile.draw();
            faceUpCounters.add(counter);
            counter.setOwned(false);
        } else {
            // TODO what to do if the counter pile is empty?? reshuffle?
        }
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

}
