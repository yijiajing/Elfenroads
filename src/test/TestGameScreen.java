package test;

import domain.Player;
import domain.TransportationCounter;
import enums.Colour;
import enums.CounterType;
import enums.GameVariant;
import gamemanager.GameManager;
import gamescreen.EGGameScreen;
import gamescreen.GameScreen;
import networking.GameSession;
import networking.GameState;
import networking.User;
import utils.NetworkUtils;
import windows.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

public class TestGameScreen {

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
            String localIP = NetworkUtils.getLocalIPAddPort();
            GameManager.init(Optional.empty(), "123", GameVariant.ELFENLAND_CLASSIC, localIP); // set the variant here

            Player p = new Player(Colour.BLACK, "chloe");
            GameManager.getInstance().setThisPlayer(p);
            GameManager.getInstance().setUpNewGame();
            GameState.instance().setToFirstPlayer(); // need this to avoid a null pointer when testing saving
            GameScreen.getInstance().draw();

            Logger.getGlobal().info("Showing game screen");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "gameScreen");

            // nick made these logs, you can delete them if you want.
            Logger.getGlobal().info("There are " + GameState.instance().getNumOfPlayers() + "players in the game before saving.");

        } catch (Exception e) {

        }
    }
}
