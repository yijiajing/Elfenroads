package test;

import domain.*;
import enums.Colour;
import enums.CounterType;
import enums.GameVariant;
import enums.TravelCardType;
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
            GameManager.getInstance().getGameState().setCurrentPlayer(p);

//            for (int i=0; i<10; i++) {
                GameManager.getInstance().getThisPlayer().getHand().addUnit(new TravelCard(TravelCardType.DRAGON, GameScreen.getInstance().getWidth()*130/1440, GameScreen.getInstance().getHeight()/5));
//            }

            GameManager.getInstance().setUpNewGame();
            GameScreen.getInstance().draw();

            Logger.getGlobal().info("Showing game screen");
            MainFrame.cardLayout.show(MainFrame.mainPanel, "gameScreen");


            GoldTokenDeck goldTokenDeck = new GoldTokenDeck("123");
            System.out.println(goldTokenDeck.getSize());


            System.out.println(GameMap.getInstance().getTownList().size());

            for (Town town : GameMap.getInstance().getTownList()) {
                town.setGoldValueToken(goldTokenDeck.draw());
            }


        } catch (Exception e) {

        }
    }
}
