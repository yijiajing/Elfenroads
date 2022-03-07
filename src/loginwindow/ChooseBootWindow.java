package loginwindow;

import commands.SendBootColourCommand;
import domain.GameManager;
import domain.Player;
import enums.Colour;
import networking.CommunicationsManager;
import networking.GameSession;
import networking.GameState;
import networking.User;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;


public class ChooseBootWindow extends JPanel {

    private JLabel background_elvenroads;
    private String sessionID;
    private JPanel bootPanel;
    private JPanel textPanel;

    public ChooseBootWindow(String sessionID) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        setOpaque(false);
        setLayout(new FlowLayout());

        this.background_elvenroads = MainFrame.instance.getElfenroadsBackground();
        this.sessionID = sessionID;

        this.bootPanel = new JPanel();
        this.bootPanel.setBounds(0, MainFrame.mainPanel.getHeight()*4/10,
                MainFrame.mainPanel.getWidth(), MainFrame.mainPanel.getHeight()/3);
        this.bootPanel.setOpaque(false);

        this.textPanel = new JPanel();
        this.textPanel.setBounds(0, MainFrame.mainPanel.getHeight()*3/10,
                MainFrame.mainPanel.getWidth(), MainFrame.mainPanel.getHeight()/10);
        this.textPanel.setOpaque(false);
        JLabel text = new JLabel("Please choose from one of the available boot colours below.");
        text.setFont(new Font("Serif", Font.PLAIN, 30));
        this.textPanel.add(text);
    }


    public void launch() {
        try {
            int numPlayers = GameSession.getPlayerNames(sessionID).size();

            if (numPlayers == 1) { // I am the creator of the session
                displayAvailableColours(); // all colours are available
            } else {
                GameManager.getInstance().requestAvailableColours(); // ask the existing players for their colours
            }

        } catch (IOException e) {
            System.out.println("There was a problem getting the players' names in the session.");
            e.printStackTrace();
        }
    }


    public void displayAvailableColours() {
        ArrayList<Colour> colours = GameManager.getInstance().getAvailableColours();

        for (Colour c : colours) {
            ImageIcon bootIcon = new ImageIcon("./assets/boppels-and-boots/boot-" + c + ".png");
            Image bootResized = bootIcon.getImage().getScaledInstance(MainFrame.mainPanel.getWidth()/11,
                    MainFrame.mainPanel.getHeight()/7,  java.awt.Image.SCALE_SMOOTH);
            JLabel bootImage = new JLabel(new ImageIcon(bootResized));

            bootImage.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String localPlayerName = User.getInstance().getUsername();
                    GameManager.getInstance().setThisPlayer(new Player(c, localPlayerName));
                    try {
                        String localIP = NetworkUtils.getLocalIP();
                        GameManager.getInstance().removeAvailableColour(c, localIP);
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(new SendBootColourCommand(c, localIP));
                    } catch (Exception exception) {
                        System.out.println("Problem getting local IP.");
                        exception.printStackTrace();
                    }

                    //TODO: for now this will send the user back to the lobby screen
                    // TODO: but we need to implement an intermediary screen between lobby and gameScreen
                    remove(background_elvenroads);
                    MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                    MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });

            this.bootPanel.add(bootImage);

            background_elvenroads.add(textPanel);
            background_elvenroads.add(bootPanel);
            add(background_elvenroads);

            setVisible(true);
        }
    }
}
