package networking;

import domain.*;
import enums.*;
import loginwindow.MainFrame;
import org.json.JSONObject;
import gamescreen.GameScreen;
import panel.ElfBootPanel;
import savegames.*;
import utils.GameRuleUtils;

import java.io.Serializable;
import java.util.*;


import java.util.ArrayList;
import java.util.logging.Logger;

public class GameState implements Serializable{

    // this class will contain all of the information we need to send regarding game state
    // created this class so that I can easily serialize and send important info to each computer
    // for now, this will only keep track of boot locations and towns visited because that's all we need for the demo

    /* as of now, we need to keep track of and be able to serialize:
    1. which Elfen towns have been visited (show on the UI by the town pieces)
    2. where each player's boot is
    3. 
     */
    private final Logger LOGGER = Logger.getGlobal();

    // Global variable holding the singleton GameState instance
    private static GameState instance;

    // meta info (possibly separated into another class in the future)
    private int totalRounds;
    private GameVariant gameVariant;
    private List<Player> players = new ArrayList<>();

    // state info
    private int currentRound;
    private RoundPhaseType currentPhase;
    private Player currentPlayer;
    private int passedPlayerCount;

    private TravelCardDeck travelCardDeck;
    private ArrayList<TravelCard> faceUpCards = new ArrayList<>(); // EG
    private int goldCardDeckCount;

    private CounterUnitPile counterPile;
    private ArrayList<TransportationCounter> faceUpCounters = new ArrayList<>(); // EL

    private ArrayList<ElfBoot> elfBoots;

    // for loading games
    private Savegame loadedState;

    private GameState (String sessionID, GameVariant gameVariant)
    {
        this.elfBoots = new ArrayList<>();
        this.currentRound = 1;
        this.passedPlayerCount = 0;
        if (gameVariant == GameVariant.ELFENLAND_LONG) {
            this.totalRounds = 4;
        } else if (GameRuleUtils.isElfengoldVariant(gameVariant)) {
            this.totalRounds = 6;
        } else {
            this.totalRounds = 3;
        }
        this.gameVariant = gameVariant;
        // the below line gives a nullPointerException when it is called from the GameManager constructor because, inside the constructor, the GameManager.instance() is still null
        // String sessionID = GameManager.getInstance().getSessionID();
        // that is why we are passing the sessionID to the GameState constructor instead (there is no reason for it to really be a field)
        this.travelCardDeck = new TravelCardDeck(sessionID, gameVariant);

        this.counterPile = new CounterUnitPile(sessionID, gameVariant);
    }

    // TODO: how to mediate this constructor with a singleton feature

    /**
     * will read in game info from a save
     * @param pLoadedState the savegame, read from a file
     */
    public GameState (Savegame pLoadedState)
    {
        loadedState = pLoadedState;
        // load directly saved fields first
        totalRounds = loadedState.getTotalRounds();
        gameVariant = loadedState.getGameVariant();
        currentRound = loadedState.getCurrentRound();
        currentPhase = loadedState.getCurrentPhase();
        passedPlayerCount = loadedState.getPassedPlayerCount();
        goldCardDeckCount = loadedState.getGoldCardDeckCount();
        // load the counters and cards

        // load all of the players from the savegame
        loadPlayers();

        // load the travel card deck and counter pile
        loadTravelCardDeck();
        loadCounterPile();

        // load face up cards or counters
        loadFaceUpCards();
        loadFaceUpCounters();

        // init elf boots and their locations based on player colors
        loadBoots();

    }

    
   
    /**
     * @return an arrayList containing all Players in this game
     */
    public List<Player> getPlayers(){
    	return new ArrayList<>(players);
    }
    
    public static GameState init(String sessionID, GameVariant gameVariant) {
        if (instance == null) {
            instance = new GameState(sessionID, gameVariant);
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

    public int getPassedPlayerCount() {
        return passedPlayerCount;
    }

    public void incrementPassedPlayerCount() {
        passedPlayerCount++;
    }

    public void clearPassedPlayerCount() {
        passedPlayerCount = 0;
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

    public CounterUnitPile getCounterPile() {
        return this.counterPile;
    }

    public ArrayList<TravelCard> getFaceUpCards() { return this.faceUpCards; }

    public GameVariant getGameVariant() {
        return gameVariant;
    }

    public void setGameVariant(GameVariant gameVariant) {
        this.gameVariant = gameVariant;
    }

    /**
     * Flips over a counter from the pile so that it is face-up (EL)
     */
    public void addFaceUpCounterFromPile() {
        if (counterPile.getSize() > 0) {
            LOGGER.info("Adding face-up counter from pile");
            CounterUnit counter = counterPile.draw();
            assert counter instanceof TransportationCounter; // only used for Elfenland
            counter.setOwned(false);
            faceUpCounters.add((TransportationCounter)counter);
        }
    }

    /**
     * Flips over a card from the deck so that it is face-up (EG)
     */
    public void addFaceUpCardFromDeck() {
        if (travelCardDeck.getSize() > 0) {
            LOGGER.info("Adding face-up card from travel card deck");
            CardUnit card = travelCardDeck.draw();
            if (card instanceof GoldCard) {
                // if it is a gold card, we just add it to the gold card deck
                incrementGoldCardDeckCount();
                GameScreen.getInstance().updateAll();
            } else if (card instanceof TravelCard){
                ((TravelCard) card).setOwned(false);
                faceUpCards.add((TravelCard) card);
            }
        }
    }


    public void removeFaceUpCounter(CounterUnitType type) {
        CounterUnit toRemove = null;

        for (CounterUnit c : faceUpCounters) {
            if (c.getType() == type) {
                toRemove = c;
            }
        }

        if (toRemove != null) {
            LOGGER.info("There are " + faceUpCounters.size() + " face up counters.");
            LOGGER.info("Removing face-up counter of type " + toRemove.getType());
            faceUpCounters.remove(toRemove);
            LOGGER.info("There are now " + faceUpCounters.size() + " face up counters.");
            addFaceUpCounterFromPile();
            LOGGER.info("There are now " + faceUpCounters.size() + " face up counters.");
            GameScreen.getInstance().updateAll();
        } else {
            LOGGER.info("Error: Counter drawn by another player is not present in the face-up counters on this device.");
        }
    }

    public void removeFaceUpCard(TravelCardType type) {
        TravelCard toRemove = null;

        for (TravelCard c: faceUpCards) {
            if (c.getType() == type) {
                toRemove = c;
            }
        }

        if (toRemove != null) {
            LOGGER.info("There are " + faceUpCounters.size() + " face up cards.");
            LOGGER.info("Removing face-up card of type " + toRemove.getType());
            faceUpCards.remove(toRemove);
            addFaceUpCardFromDeck();
            GameScreen.getInstance().updateAll();
        } else {
            LOGGER.info("Error: Card drawn by another player is not present in the face-up cards on this device.");
        }
    }

    public void addPlayer(Player p) {
        players.add(p);
    }

    /**
     * @pre GameManager.launch() has already been called and the players have been intialized
     * MUST BE CAREFUL WITH USAGES! RETURNS NULL IF NO PLAYER FOUND
     * @param name the name of the player we want
     * @return the player whose name is name
     */
    public Player getPlayerByName(String name)
    {
        for (Player candidate : getPlayers())
        {
            if (candidate.getName().equals(name))
            {
                return candidate;
            }
        }
        System.out.println("Could not find a player named " + name);
        return null; // TODO: if we want, we could throw an Exception instead of returning null.
    }

    /**
     * sorts the list of players so that everyone has the same ordering
     * called in GameManager.launch() after initializing all the Players
     */
    public void sortPlayers()
    {
        Collections.sort(players);
    }

    public void incrementGoldCardDeckCount() {
        goldCardDeckCount++;
    }

    public void clearGoldCardDeck() {
        goldCardDeckCount = 0;
    }

    // METHODS USED TO READ IN A LOADED GAME
    // TODO: implement these and implement constructors
    private void loadPlayers()
    {
        for (SerializablePlayer toLoad : loadedState.getPlayers())
        {
            addPlayer(new Player(toLoad));
        }
    }

    /**
     * @pre the gamescreen has been initialized
     */
    private void loadBoots()
    {
        elfBoots = new ArrayList<ElfBoot>();
        int bootWidth = MainFrame.instance.getWidth() * 15 / 1440;
        int bootHeight = MainFrame.instance.getHeight() * 15/900;

        for (Player cur : players)
        {
            // add each player's elf boot to his current location
            // TODO: the gamescreen needs to already exist when we set up the boots, somehow...
            Town playerCurrentTown = cur.getCurrentTown();
            ElfBootPanel curPanel = playerCurrentTown.getElfBootPanel();
            Colour playerColor = cur.getColour();
            ElfBoot thatPlayer = new ElfBoot(playerColor, bootWidth, bootHeight, curPanel, GameScreen.getInstance());
            elfBoots.add(thatPlayer);
            // TODO: do we need to call a method in Player class to assign the boot?
        }
    }

    /**
     * loads the travel card deck from a savegame
     */
    private void loadTravelCardDeck()
    {
       for (SerializableTravelCard crd : loadedState.getTravelCardDeck())
       {
           travelCardDeck.addDrawable(new TravelCard(crd));
       }
    }

    /**
     * loads the counter pile from a savegame
     */
    private void loadCounterPile()
    {
        for (SerializableCounterUnit ctr : loadedState.getCounterPile())
        {
            // load each counter from the savegame
            if (ctr instanceof SerializableTransportationCounter)
            {
                SerializableTransportationCounter ctrDowncasted = (SerializableTransportationCounter) ctr;
                counterPile.addDrawable(new TransportationCounter(ctrDowncasted));
            }
            else // if ctr is an obstacle
            {
                SerializableObstacle ctrDowncasted = (SerializableObstacle) ctr;
                counterPile.addDrawable(new Obstacle(ctrDowncasted));
            }
        }
    }

    /**
     * loads the face up travel cards from a saved Elfengold game
     */
    private void loadFaceUpCards()
    {
        for (SerializableTravelCard crd : loadedState.getFaceUpCards())
        {
            faceUpCards.add(new TravelCard(crd));
        }
    }

    /**
     * loads the face up transportation counters from a saved Elfenland game
     */
    private void loadFaceUpCounters()
    {
        for (SerializableTransportationCounter ctr : loadedState.getFaceUpCounters())
        {
            faceUpCounters.add(new TransportationCounter(ctr));
        }
    }

}
