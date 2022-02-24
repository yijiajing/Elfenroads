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
    private Player thisPlayer; // represents the Player who is using this GUI, set by ChooseBootWindow
    private boolean loaded;

    /**
     * Constructor is called when "join" is clicked
     * If the User is starting a new game, then loadedState == null and sessionID != null
     * If the User is loading a previous game, then loadedState != null and sessionID == null (change this?)
     */
    private GameManager(Optional<GameState> loadedState, Optional<String> sessionID) {

        MainFrame.mainPanel.add(GameScreen.init(MainFrame.getInstance()), "gameScreen");

        // start a new game if there is no state to be loaded
        if (!loadedState.isPresent() && sessionID.isPresent()) {
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

        //gameLoop();
    }

    public static GameManager init(Optional<GameState> loadedState, Optional<String> sessionID) {
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

        distributeTravelCards();

        initializeElfBoots();
    }


    private void gameLoop() {

        while (gameState.getCurrentRound() <= gameState.getTotalRounds()) {

            setUpRound();

            // **** DRAW COUNTERS **** //
            while (gameState.getCurrentPhase().equals(RoundPhaseType.DRAWCOUNTERS)) {
                // TODO IMPLEMENT

                break;
            }

            // **** PLAN TRAVEL ROUTES **** //
            while (gameState.getCurrentPhase().equals(RoundPhaseType.PLANROUTES)) {

                // wait for other players to take their turns
                while (!gameState.getCurrentPlayer().getUser().equals(User.getInstance()));

                // its my turn
                if (gameState.getCurrentPlayer().getUser().equals(User.getInstance())) {
                    updateGameState();

                    // TODO implement all logic for my turn

                    sendGameState();
                    endTurn();
                }
            }

            // **** MOVE ON MAP **** //
            while (gameState.getCurrentPhase().equals(RoundPhaseType.MOVE)) {

                // wait for other players to take their turns
                while (!gameState.getCurrentPlayer().getUser().equals(User.getInstance()));

                // its my turn
                if (gameState.getCurrentPlayer().getUser().equals(User.getInstance())) {
                    updateGameState();

                    // TODO implement all logic for my turn

                    sendGameState();
                    endTurn();
                }
            }

            endRound();
        }

        endGame();
    }


    /**
     * Distribute travel cards to each Player by popping from the front of the TravelCardDeck
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


    private void initializeElfBoots() {

        ElfBootPanel elvenholdBootPanel = GameMap.getInstance().getTown("Elvenhold").getPanel().getElfBootPanel();

        for (Player p : gameState.getPlayers()) {
            gameState.addElfBoot(new ElfBoot(p.getColour(), GameScreen.getInstance().getWidth(), GameScreen.getInstance().getHeight(), elvenholdBootPanel, GameScreen.getInstance()));
        }
    }


    // totalRounds Round <--in-- numOfRoundPhaseType Phases <--in-- numOfPlayer Turns
    public void endTurn() {
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

    private void setUpRound() {
        distributeTravelCards(); // distribute cards to each player
        gameState.setCurrentPhase(RoundPhaseType.DRAWCOUNTERS);
        gameState.setToFirstPlayer();
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
        // TODO implement

        // get game state from peers and update
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
}
