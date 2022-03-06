package domain;

import commands.DrawCardCommand;
import commands.NotifyTurnCommand;
import commands.GetBootColourCommand;
import enums.Colour;
import enums.RoundPhaseType;
import loginwindow.*;
import networking.*;
import panel.ElfBootPanel;
import panel.GameScreen;
import utils.NetworkUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * A Singleton class that controls the main game loop
 */
public class GameManager {

    private static GameManager INSTANCE; // Singleton instance
    private final static Logger LOGGER = Logger.getLogger("Game Manager");

    private GameState gameState;
    private ActionManager actionManager;
    private Player thisPlayer = null; // represents the Player who is using this GUI, set by ChooseBootWindow
    private boolean loaded;

    // stuff for managing networking operations
    private String sessionID;
    private CommunicationsManager coms;

    // manages choosing a boot colour
    private ChooseBootWindow bootWindow;
    private ArrayList<Colour> availableColours = new ArrayList<>();
    private HashMap<Colour, String> bootColours = new HashMap<>(); // <boot colour, player IP> TODO change to Player

    /**
     * Constructor is called when "join" is clicked
     * If the User is starting a new game, then loadedState == null
     * If the User is loading a previous game, then loadedState != null
     */
    private GameManager(Optional<GameState> loadedState, String pSessionID) {

        MainFrame.mainPanel.add(GameScreen.init(MainFrame.getInstance()), "gameScreen");
        sessionID = pSessionID;

        // start a new game if there is no state to be loaded
        if (!loadedState.isPresent()) {
            gameState = GameState.init(3, pSessionID);
            actionManager = ActionManager.init(gameState, this);

            loaded = false;

            availableColours.addAll(Arrays.asList(Colour.values())); // all colours are available
        }

        // load state
        else {
            gameState = loadedState.get();
            loaded = true;
            //TODO implement
        }
        coms = new CommunicationsManager(this, sessionID);

    }

    /**
     * Called when "start" is clicked
     */
    public void launch() {
        if (!loaded) setUpNewGame();

        GameScreen.getInstance().draw();
        MainFrame.cardLayout.show(MainFrame.mainPanel,"gameScreen");

        setUpRound(); // includes dealing travel cards (PHASE 1) and drawing 1 face down counter for each player (PHASE 2)

        // TODO : should not immediately start phase 3, need some indication that it is my turn
        drawCounters(); // PHASE 3
    }

    public static GameManager init(Optional<GameState> loadedState, String sessionID) {
        if (INSTANCE == null) {
            INSTANCE = new GameManager(loadedState, sessionID);
        }
        return INSTANCE;
    }

    /**
     * @return the Singleton instance of the GameManager
     */
    public static GameManager getInstance() {
        return INSTANCE;
    }

    /**
     * Called by ChooseBootWindow after the user chooses a boot colour
     * Sets the Player of "this" GUI
     */
    public void setThisPlayer(Player p) {
        thisPlayer = p;
        gameState.addPlayer(p);
    }

    private void setUpNewGame() {
        // put 5 counters face up
        for (int i=0; i<5; i++) {
            this.gameState.addFaceUpCounterFromPile();
        }

        // give all players an obstacle
        for (Player p : gameState.getPlayers()) {
            p.getHand().addUnit(new Obstacle(MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900));
        }

        initializeElfBoots();
    }

    /**
     * PHASE 1 & 2
     */
    private void setUpRound() {
        gameState.setCurrentPhase(RoundPhaseType.DEAL_CARDS);
        gameState.setToFirstPlayer();
        gameState.getTravelCardDeck().shuffle(); // only shuffle once at the beginning of each round

        // Phase 1 and 2 can be done at the same time
        distributeTravelCards(); // distribute cards to each player (PHASE 1)
        distributeHiddenCounter(); // distribute 1 face down counter to each player (PHASE 2)

        GameScreen.getInstance().updateAll();
    }

    /**
     * PHASE 1
     * Distribute travel cards to each Player by popping from the TravelCardDeck
     * Fills the Player's hand to have 8 travel cards
     */
    public void distributeTravelCards() {
        if (!(isLocalPlayerTurn() && gameState.getCurrentPhase() == RoundPhaseType.DEAL_CARDS)) return;

        int numCards = thisPlayer.getHand().getNumTravelCards();
        for (int i = numCards; i < 8; i++) {
            thisPlayer.getHand().addUnit(gameState.getTravelCardDeck().draw());
        }
        endTurn();

        int numDrawn = 8 - numCards;
        DrawCardCommand drawCardCommand = new DrawCardCommand(numDrawn);
        NotifyTurnCommand notifyTurnCommand = new NotifyTurnCommand(RoundPhaseType.DEAL_CARDS);
        try {
            coms.sendGameCommandToAllPlayers(drawCardCommand);
            //TODO: send notifying command to the current player
        } catch (IOException e) {
            LOGGER.info("There was a problem sending the command to draw cards!");
            e.printStackTrace();
        }
    }


    /**
     * PHASE 2
     * Distribute 1 face-down transportation counter to each Player by popping from the CounterPile
     */
    public void distributeHiddenCounter() {
        //TODO: modify this
        for (Player p : gameState.getPlayers()) {
            p.getHand().addUnit(gameState.getCounterPile().draw());
        }
    }

    /**
     * PHASE 3
     */
    public void drawCounters() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase() == RoundPhaseType.DRAW_COUNTERS
                && gameState.getCurrentPlayer().equals(thisPlayer)) {

            updateGameState();
            System.out.println("Current phase: DRAW COUNTERS");

            // display message to let the user know that they need to select a counter
            GameScreen.displayMessage("Please select a transportation counter to add to your hand. You may choose one of " +
                    "the face-up counters or a counter from the deck, shown on the right side of the screen.", false, false);

            // all logic is implemented in the mouse listeners of the counters
        }
    }

    /**
     * PHASE 4
     */
    public void planTravelRoutes() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase().equals(RoundPhaseType.PLAN_ROUTES)
                && gameState.getCurrentPlayer().equals(thisPlayer)) {

            updateGameState();
            System.out.println("Current phase: PLAN TRAVEL ROUTES");

            // display message
            GameScreen.displayMessage("""
                    It is time to plan your travel routes! Begin by clicking the transportation counter in your hand that you want to use, then click on the road that you want to travel.
                    The chart in the bottom right corner indicates which transportation counters may be used on which road.
                    Alternatively, you may choose to place your Obstacle on a road that already has a counter. But be warned... you can only do this once!
                    Alternatively, you can pass your turn.
                    """, true, false);

            // TODO implement all logic in listeners and action manager
        }
    }

    /**
     * PHASE 5
     */
    public void moveOnMap() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase().equals(RoundPhaseType.MOVE)
                && gameState.getCurrentPlayer().equals(thisPlayer)) {

            updateGameState();
            System.out.println("Current phase: MOVE ON MAP");

            // display message
            GameScreen.displayMessage("""
                    It is time to travel across the map and collect your town pieces! Begin by clicking the travel card(s) that you want to use, then click on the town that you want to travel to.
                    The number of required travel cards depends on the region and is indicated by the chart in the bottom right corner. 
                    You can repeat this as many times as you want. When you are done travelling, click "DONE". 
                    
                    """, false, true);

            // TODO implement all logic in listeners and action manager
        }
    }


    /* GAME LOOP DOES NOT WORK -- TO BE DELETED
    private void gameLoop() {

        while (gameState.getCurrentRound() <= gameState.getTotalRounds()) {

            // includes dealing travel cards (STAGE 1) and drawing 1 face down counter for each player (STAGE 2)
            setUpRound();

            // **** STAGE 3 - DRAW COUNTERS **** //
            while (gameState.getCurrentPhase().equals(RoundPhaseType.DRAWCOUNTERS)) {

                // wait for other players to take their turns
                while (!gameState.getCurrentPlayer().equals(thisPlayer));

                // its my turn
                if (gameState.getCurrentPlayer().equals(thisPlayer)){
                    updateGameState();

                    // TODO: display message to let the user know that they need to select a counter

                    // waiting for the user to click a counter, either from the deck or face-up
                    while (!GameScreen.getInstance().getDeckSelected() && !GameState.instance().getAnyFaceUpCounterSelectedToOwn());

                    if (GameScreen.getInstance().getDeckSelected()) { // player wants to draw a counter from the pile
                        TransportationCounter drawn = GameState.instance().getCounterPile().draw();
                        thisPlayer.getHand().addUnit(drawn);
                        GameScreen.getInstance().setDeckSelected(false);
                    } else if (GameState.instance().getAnyFaceUpCounterSelectedToOwn()) { // player wants to take a face-up counter
                        TransportationCounter selected = GameState.instance().getFaceUpCounterSelectedToOwn();
                        thisPlayer.getHand().addUnit(selected);
                        selected.setSelectedToOwn(false);
                    }

                    endTurn(); // updates the GUI
                    sendGameState();
                }
            }

            // **** STAGE 4 - PLAN TRAVEL ROUTES **** //
            while (gameState.getCurrentPhase().equals(RoundPhaseType.PLANROUTES)) {

                // wait for other players to take their turns
                while (!gameState.getCurrentPlayer().equals(thisPlayer));

                // its my turn
                if (gameState.getCurrentPlayer().equals(thisPlayer)) {
                    updateGameState();

                    // TODO implement all logic for my turn

                    endTurn();
                    sendGameState();
                }
            }

            // **** STAGE 5 - MOVE ON MAP **** //
            while (gameState.getCurrentPhase().equals(RoundPhaseType.MOVE)) {

                // wait for other players to take their turns
                while (!gameState.getCurrentPlayer().equals(thisPlayer));

                // its my turn
                if (gameState.getCurrentPlayer().equals(thisPlayer)) {
                    updateGameState();

                    // TODO implement all logic for my turn

                    endTurn();
                    sendGameState();
                }
            }

            endRound();
        }

        endGame();
    } */


    private void initializeElfBoots() {

        ElfBootPanel elvenholdBootPanel = GameMap.getInstance().getTown("Elvenhold").getPanel().getElfBootPanel();

        for (Player p : gameState.getPlayers()) {
            gameState.addElfBoot(new ElfBoot(p.getColour(), GameScreen.getInstance().getWidth(), GameScreen.getInstance().getHeight(), elvenholdBootPanel, GameScreen.getInstance()));
        }
    }


    // totalRounds Round <--in-- numOfRoundPhaseType Phases <--in-- numOfPlayer Turns
    public void endTurn() {
        GameScreen.getInstance().updateAll(); // update the GUI
        actionManager.clearSelection();

        // all players have passed their turn in the current phase
        if (gameState.getCurrentPlayerIdx() + 1 == gameState.getNumOfPlayers()) {
            endPhase();
        }

        // within the same phase, next player will take action
        gameState.setToNextPlayer();
    }

    private void endPhase() {
        actionManager.clearSelection();
        int nextOrdinal = gameState.getCurrentPhase().ordinal() + 1;
        if (nextOrdinal == RoundPhaseType.values().length) {
            // all phases are done, go to the next round
            endRound();
        } else { // go to the next phase within the same round
            gameState.setCurrentPhase(RoundPhaseType.values()[nextOrdinal]);
            gameState.setToFirstPlayer();
        }
    }

    private void endRound() {
        actionManager.clearSelection();
        gameState.setToFirstPlayer();
        gameState.incrementCurrentRound();
        if (gameState.getCurrentRound() == gameState.getTotalRounds()) {
            endGame();
            return;
        }
        GameMap.getInstance().clearAllCounters();
        gameState.setCurrentPhase(RoundPhaseType.DRAW_COUNTERS);
        //TODO: update round card in UI
    }

    private void endGame() {
        //TODO: finishes game ending
    }

    private void updateGameState() {
        // TODO implement get game state from peers

        GameScreen.getInstance().updateAll();
    }

    private void sendGameState() {
        // TODO implement
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void requestAvailableColours() {
        try {
            String localAddress = NetworkUtils.getLocalIPAddPort();
            coms.sendGameCommandToAllPlayers(new GetBootColourCommand(localAddress));
        } catch (IOException e) {
            System.out.println("There was a problem sending the command to get players' boot colours!");
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("There was a problem getting the local IP.");
            e.printStackTrace();
        }
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }

    public CommunicationsManager getComs() {
        return coms;
    }


    public void removeAvailableColour(Colour c, String playerIP) {
        availableColours.remove(c);
        addPairToBootColours(c, playerIP);

        try {
            if (thisPlayer == null) { // I haven't chosen a boot colour yet
                int numPlayers = GameSession.getPlayerNames(sessionID).size();

                if (availableColours.size() == 6-numPlayers) { // we have received the boot colours from all players who have joined
                    bootWindow.displayAvailableColours();
                }
            }
        } catch (IOException e) {
            System.out.println("There was a problem getting the players' names in the session.");
            e.printStackTrace();
        }
    }

    public void addPairToBootColours(Colour c, String playerIP) {
        bootColours.put(c, playerIP);
    }

    public ArrayList<Colour> getAvailableColours() {
        return this.availableColours;
    }

    public String getSessionID() {
        return sessionID;
    }
      
    public boolean isLocalPlayerTurn() {
        return thisPlayer.equals(gameState.getCurrentPlayer());
    }
}
