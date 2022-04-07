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
import networking.User;
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
            GameManager.init(Optional.empty(), "123", GameVariant.ELFENLAND_CLASSIC); // set the variant here

            Player p = new Player(Colour.BLACK, "chloe");
            GameManager.getInstance().setThisPlayer(p);
            GameManager.getInstance().setUpNewGame();
            GameScreen.getInstance().draw();

            Logger.getGlobal().info("Showing game screen");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "gameScreen");

        } catch (Exception e) {

        }
    }
}
