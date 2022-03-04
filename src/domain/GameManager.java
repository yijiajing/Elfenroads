package domain;

import enums.Colour;
import enums.CounterType;
import enums.RoundPhaseType;
import enums.TravelCardType;
import loginwindow.*;
import networking.*;
import panel.ElfBootPanel;
import panel.GameScreen;

import javax.swing.*;
import java.util.*;

/**
 * A Singleton class that controls the main game loop
 */
public class GameManager {

    private static GameManager INSTANCE; // Singleton instance

    private GameState gameState;
    private ActionManager actionManager;
    private Player thisPlayer = null; // represents the Player who is using this GUI, set by ChooseBootWindow
    private boolean loaded;

    // stuff for managing networking operations
    private CommunicationsManager coms;
    private String sessionID;

    /**
     * Constructor is called when "join" is clicked
     * If the User is starting a new game, then loadedState == null and sessionID != null
     * If the User is loading a previous game, then loadedState != null and sessionID == null (change this?)
     */
    private GameManager(Optional<GameState> loadedState, String pSessionID) {

        MainFrame.mainPanel.add(GameScreen.init(MainFrame.getInstance()), "gameScreen");
        sessionID = pSessionID;
        coms = new CommunicationsManager(this, sessionID); // initializes the CommunicationsManager


        // start a new game if there is no state to be loaded
        if (!loadedState.isPresent()) {
            gameState = GameState.init(3);
            actionManager = ActionManager.init(gameState);
            loaded = false;

            // prompt user to choose a boot colour
            MainFrame.mainPanel.add(new ChooseBootWindow(), "choose-boot");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "choose-boot");
        }

        // load state
        else {
            gameState = loadedState.get();
            loaded = true;
            //TODO implement
        }
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

        distributeTravelCards(); // distribute cards to each player (PHASE 1)
        distributeHiddenCounter(); // distribute 1 face down counter to each player (PHASE 2)

        GameScreen.getInstance().updateAll();

        gameState.setCurrentPhase(RoundPhaseType.DRAWCOUNTERS);
        gameState.setToFirstPlayer();
    }

    /**
     * PHASE 3
     */
    public void drawCounters() {
        if (gameState.getCurrentRound() <= gameState.getTotalRounds()
                && gameState.getCurrentPhase().equals(RoundPhaseType.DRAWCOUNTERS)
                && gameState.getCurrentPlayer().equals(thisPlayer)) {

            updateGameState();
            System.out.println("Current phase: DRAW COUNTERS");

            // TODO: display message to let the user know that they need to select a counter

            // all logic is implemented in the mouse listeners of the counters
        }
    }

    /**
     * PHASE 4
     */
    public void planTravelRoutes() {
        System.out.println("Current phase: PLAN TRAVEL ROUTES");
    }

    /**
     * PHASE 5
     */
    public void moveOnMap() {
        System.out.println("Current phase: MOVE ON MAP");
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
            int nextOrdinal = gameState.getCurrentPhase().ordinal() + 1;
            if (nextOrdinal == RoundPhaseType.values().length) {
                // all phases are done, go to the next round
                endRound();
            } else {
                // go to the next phase within the same round
                gameState.setCurrentPhase(RoundPhaseType.values()[nextOrdinal]);
            }
            return;
        }

        // within the same phase, next player will take action
        gameState.setToNextPlayer();
    }

    private void endRound() {
        gameState.setToFirstPlayer();
        gameState.incrementCurrentRound();
        if (gameState.getCurrentRound() == gameState.getTotalRounds()) {
            endGame();
            return;
        }
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

    public static ArrayList<Colour> getAvailableColours() {
        // TODO: this method is WRONG - we need to get the available colours by communicating with
        // TODO: the other players in the session about what colours they chose

        ArrayList<Colour> availableColours = new ArrayList<>();
        availableColours.addAll(Arrays.asList(Colour.values()));

        return availableColours;
    }

    public Player getThisPlayer() {
        return thisPlayer;
    }


    /**
     * Distribute travel cards to each Player by popping from the TravelCardDeck
     * Fills the Player's hand to have 8 travel cards
     */
    public void distributeTravelCards() {

        gameState.getTravelCardDeck().shuffle();

        for (Player p : gameState.getPlayers()) {
            int numCards = p.getHand().getNumTravelCards();

            for (int i=numCards; i<8; i++) {
                p.getHand().addUnit(gameState.getTravelCardDeck().draw());
            }
        }
    }


    /**
     * Distribute 1 face-down transportation counter to each Player by popping from the CounterPile
     */
    public void distributeHiddenCounter() {
        for (Player p : gameState.getPlayers()) {
            p.getHand().addUnit(gameState.getCounterPile().draw());
        }
    }
}
