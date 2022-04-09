package test;

import domain.CardUnit;
import domain.CounterUnit;
import domain.Player;
import enums.Colour;
import enums.GameVariant;
import gamemanager.GameManager;
import gamescreen.GameScreen;
import networking.GameSession;
import networking.GameState;
import networking.User;
import savegames.Savegame;
import utils.NetworkUtils;
import windows.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class TestLoadedGame {

    /**
     * A test class that displays the GameScreen only, without having to login and create a session
     *
     * IMPORTANT: Comment out the MainFrame constructor and uncomment the test constructor
     *
     * The game screen won't show up immediately. Wait until the message "Showing game screen" is logged to the console.
     * At this point, if the screen is still white, try adjusting the size of the frame. The game screen should show up now.
     */

    public static void main(String[] args) {
        MainFrame mainFrame = MainFrame.getInstance();

        try {
            // load in a gamestate from a save file
            Logger.getGlobal().info("Attempting to read in the savegame from a file.");
            Savegame testSaveGame = Savegame.read("123_ROUND1.elf");
            Logger.getGlobal().info("Successfully read in the savegame from a file.");
            String localIP = NetworkUtils.getLocalIPAddPort();
            GameManager.init(Optional.of(new GameState(testSaveGame)), "123", GameVariant.ELFENGOLD_CLASSIC, localIP); // set the variant here
            Logger.getGlobal().info("Successfully initialized the GameManager with the saved game.");

            // GameScreen.init(MainFrame.instance, GameState.instance().getGameVariant());
            // Logger.getGlobal().info("Initialized GameScreen.");
            GameScreen.getInstance().draw();
            GameScreen.getInstance().updateAll();

            Logger.getGlobal().info("Showing game screen");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "gameScreen");

            // feel free to delete this if I forget to
            Logger.getGlobal().info("The actual savegame has " + testSaveGame.getPlayers().size() + " players in it.");
            Logger.getGlobal().info("There are " + GameState.instance().getNumOfPlayers() + " players in the game after loading.");

            // print hand info. just for debugging
            for (Player inGame : GameState.instance().getPlayers())
            {
                for (CounterUnit ctr : inGame.getHand().getCounters())
                {
                    Logger.getGlobal().info("In hand: " + ctr);
                }
                for (CardUnit crd : inGame.getHand().getCards())
                {
                    Logger.getGlobal().info("In hand: " + crd);
                }
                if (inGame.hasObstacle())
                {
                    Logger.getGlobal().info(inGame.getHand().getObstacle().toString());
                }

            }




        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}