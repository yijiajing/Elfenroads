package domain;

import enums.CounterType;
import enums.RoundPhaseType;
import enums.TravelCardType;
import loginwindow.*;
import networking.*;
import panel.ElfBootPanel;
import panel.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Stack;

/**
 * A Singleton class that controls the main game loop
 */
public class GameManager {

    private static GameManager INSTANCE; // Singleton instance

    private GameState gameState;
    private ActionManager actionManager;

    /**
     * If the User is starting a new game, then loadedState == null and sessionID != null
     * If the User is loading a previous game, then loadedState != null and sessionID == null (change this?)
     */
    private GameManager(Optional<GameState> loadedState, Optional<String> sessionID) {

        MainFrame.mainPanel.add(GameScreen.init(MainFrame.getInstance()), "gameScreen");

        // start a new game if there is no state to be loaded
        if (!loadedState.isPresent() && sessionID.isPresent()) {
            gameState = GameState.init(GameScreen.getInstance(), 3);
            actionManager = ActionManager.init(gameState);

            // TODO: delete this - need to add players to gameState based on users in gameSession
            gameState.setDummyPlayers();

            setUpNewGame();
        }

        // load state
        else {
            gameState = loadedState.get();
            //TODO implement
        }

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

    private void setUpNewGame() {

        for (int i=0; i<5; i++) {
            this.gameState.addFaceUpCounterFromPile();
        }

        distributeTravelCards(); // distribute cards to each player

        initializeElfBoots();

        // TODO: add more stuff to set up from game rules
    }


    private void gameLoop() {

        while (gameState.getCurrentRound() <= gameState.getTotalRounds()) {

            setUpRound();

            // **** DRAW CARDS **** //
            while (gameState.getCurrentPhase().equals(RoundPhaseType.DRAWCARDS)) {
                // TODO IMPLEMENT
                break;
            }

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
     * Distribute 8 travel cards to each Player by popping from the front of the TravelCardDeck
     */
    public void distributeTravelCards() {
        for (Player p : gameState.getPlayers()) {
            for (int i=0; i<8; i++) {
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
        //TODO

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
}
