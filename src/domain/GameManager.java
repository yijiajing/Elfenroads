package domain;

import loginwindow.*;
import networking.*;
import panel.GameScreen;

import javax.swing.*;
import java.util.Optional;

/**
 * A Singleton class that controls the main game loop
 */
public class GameManager {

    private static GameManager INSTANCE; // Singleton instance

    private GameState gameState;

    /**
     * If the User is starting a new game, then loadedState == null and sessionID != null
     * If the User is loading a previous game, then loadedState != null and sessionID == null (change this?)
     */
    private GameManager(Optional<GameState> loadedState, Optional<String> sessionID) {

        MainFrame.cardLayout.show(MainFrame.mainPanel,"gameScreen");

        // start a new game if there is no state to be loaded
        if (!loadedState.isPresent() && sessionID.isPresent()) {
            gameState = new GameState(GameScreen.getInstance());

            // TODO: delete this - need to add players to gameState based on users in gameSession
            gameState.setDummyPlayers();

            setUpNewGame();
        }

        // load state
        else {
            gameState = loadedState.get();
            //TODO implement
        }

        gameLoop();

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
        // TODO: implement 

    }


    private void gameLoop() {

        while (true) {

            // TODO: implement
            break;
        }
    }


}
