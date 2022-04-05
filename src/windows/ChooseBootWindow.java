package windows;

import commands.SendBootColourCommand;
import gamemanager.GameManager;
import domain.Player;
import enums.Colour;
import networking.GameSession;
import networking.User;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;


public class ChooseBootWindow extends JPanel {

    private JLabel background_elvenroads;
    private String sessionID;
    private JPanel bootPanel;
    private JPanel textPanel;

    public ChooseBootWindow(String sessionID, ArrayList<Colour> availColours) {
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

        displayAvailableColours(availColours);
    }


    public void displayAvailableColours(ArrayList<Colour> colours) {

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
                        String localIP = NetworkUtils.getLocalIP(0);
                        GameManager.getInstance().removeAvailableColour(c, localIP);
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(new SendBootColourCommand(c, localIP));
                    } catch (Exception exception) {
                        System.out.println("Problem getting local IP.");
                        exception.printStackTrace();
                    }

                    // take the player to either the host or player waiting window, depending on whether they are the host of the session
                    remove(background_elvenroads);
                    if (GameSession.isCreator(User.getInstance(), sessionID)) // the player is the host of the session
                    {
                        MainFrame.mainPanel.add(new HostWaitWindow(sessionID), "hostWaitingRoom");
                        MainFrame.cardLayout.show(MainFrame.mainPanel, "hostWaitingRoom");
                    }
                    // the player is not the host of the session, so he should go to the playerWaitingRoom
                    else
                    {
                        // if we previously had a PlayerWaitWindow (meaning we were in a game before and then left,) we need to completely reinitialize it
                        if (MainFrame.getPlayerWait() != null)
                        {
                            PlayerWaitWindow prev = MainFrame.getPlayerWait();
                            MainFrame.mainPanel.remove(prev);
                        }
                        // initialize a new PlayerWaitWindow
                        PlayerWaitWindow updated = new PlayerWaitWindow(sessionID);
                        MainFrame.setPlayerWaitWindow(updated);
                        MainFrame.mainPanel.add(updated, "playerWaitingRoom");
                        MainFrame.cardLayout.show(MainFrame.mainPanel, "playerWaitingRoom");
                    }
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
