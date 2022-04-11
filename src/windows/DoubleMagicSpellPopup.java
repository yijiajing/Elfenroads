package windows;

import commands.PlaceTransportationCounterCommand;
import domain.CounterUnit;
import domain.Player;
import domain.Road;
import domain.TransportationCounter;
import gamemanager.GameManager;
import gamescreen.EGGameScreen;
import gamescreen.GameScreen;
import gamemanager.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.logging.Logger;

public class DoubleMagicSpellPopup extends JPanel {

    public DoubleMagicSpellPopup() {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth()/4, (int) screenSize.getHeight()/6, (int) screenSize.getWidth()/2, (int) screenSize.getHeight()/2);
        setOpaque(true);

        JPanel textPanel = new JPanel();
        JLabel text = new JLabel();
        text.setText("<html>Select one of your counters below to place on this road.</html>");

        text.setFont(new Font("Serif", Font.PLAIN, 25));
        textPanel.add(text);
        textPanel.setVisible(true);

        JPanel counterPanel = new JPanel();
        counterPanel.setPreferredSize(new Dimension((int) screenSize.getWidth()/3, (int) screenSize.getHeight()/2));
        counterPanel.setVisible(true);

        Player thisPlayer = GameManager.getInstance().getThisPlayer();

        for (CounterUnit u : thisPlayer.getHand().getCounters()) {
            if (u instanceof TransportationCounter && ActionManager.getInstance().getSelectedRoad().canPlaceCounter()) {
                TransportationCounter counter = (TransportationCounter) u;
                ImageIcon counterIcon = new ImageIcon(counter.getImageFilePath());
                Image counterIconResized = counterIcon.getImage().getScaledInstance(screenSize.width/10,
                        screenSize.height/6,  java.awt.Image.SCALE_SMOOTH);
                JLabel counterImage = new JLabel(new ImageIcon(counterIconResized));
                counterPanel.add(counterImage);
                addMouseListener(counter, counterImage);
            }
        }

        add(textPanel);
        add(counterPanel);
    }

    // TODO delete, just for testing
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        frame.setVisible(true);
        frame.setLayout(new CardLayout());

        JLayeredPane boardGame_Layers = new JLayeredPane();
        boardGame_Layers.setBounds(0,0,(int) screenSize.getWidth(), (int) screenSize.getHeight());
        boardGame_Layers.add(new DoubleMagicSpellPopup());

        frame.add(boardGame_Layers);
    }

    private void addMouseListener(TransportationCounter counter, JLabel image) {
        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Road road = ActionManager.getInstance().getSelectedRoad();
                GameManager gameManager = GameManager.getInstance();

                if (road.setTransportationCounter(counter)) {
                    // remove this transportation counter from hand
                    gameManager.getThisPlayer().getHand().removeUnit(counter);
                    counter.setOwned(false);
                    Logger.getGlobal().info("Sending PlaceTransportationCounterCommand for double magic spell");
                    PlaceTransportationCounterCommand toSendOverNetwork = new PlaceTransportationCounterCommand(road, counter);
                    try {
                        gameManager.getComs().sendGameCommandToAllPlayers(toSendOverNetwork);
                        GameScreen.getInstance().updateAll();
                    } catch (IOException err) {
                        Logger.getGlobal().info("There was a problem sending the command to place the transportation counter!");
                        err.printStackTrace();
                    }
                    gameManager.endTurn();
                    ActionManager.getInstance().clearSelection();
                    ((EGGameScreen) GameScreen.getInstance()).hideMagicSpellPopup();
                    ActionManager.getInstance().setInExternalWindow(false);
                } else {
                    // Invalid move
                    // Should not occur bcs we check if the counter can be placed here before showing it in the popup window
                    GameScreen.displayMessage("You cannot place a transportation counter here. Please try again.");
                }
            }
        });
    }

}