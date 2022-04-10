package windows;

import commands.SendBootColourCommand;
import commands.ValidateBootCommand;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;


public class ChooseBootWindow extends JPanel {

    private JLabel background_elvenroads;
    private String sessionID;
    private JPanel bootPanel;
    private JPanel textPanel;

    private boolean localPlayerIsCreator;

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

        localPlayerIsCreator = GameSession.isCreator(User.getInstance(), sessionID);
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
                    if (localPlayerIsCreator) // if we are the creator, we will still check whether someone has taken boot we want in the time it took us to choose.
                    {
                        // we can simply re-check the available colors list to see if someone has sent in a boot choice since we loaded the window
                        if (!GameManager.getInstance().getAvailableColours().contains(c))
                        {
                            // someone else took the color, so display the message and reinitialize the window.
                            JOptionPane.showMessageDialog(null, "Too slow! That boot color is taken. Please try again.");
                            // next, re-display the choose boot window with the updated choices
                            ChooseBootWindow window = new ChooseBootWindow(GameManager.getInstance().getSessionID(), GameManager.getInstance().getAvailableColours());
                            MainFrame.mainPanel.add(window, "choose-boot");
                            MainFrame.cardLayout.show(MainFrame.mainPanel, "choose-boot");
                        }

                        GameManager.getInstance().setThisPlayer(new Player(c, localPlayerName));
                        GameManager.getInstance().removeAvailableColour(c); // this line might be unecessary, but I'm just going to leave it
                        String localIP = GameManager.getInstance().getComs().getLocalAddress();
                        try {
                            GameManager.getInstance().getComs().sendGameCommandToAllPlayers(new SendBootColourCommand(c, localIP));
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }

                        remove(background_elvenroads);
                        MainFrame.mainPanel.add(new HostWaitWindow(sessionID), "hostWaitingRoom");
                        MainFrame.cardLayout.show(MainFrame.mainPanel, "hostWaitingRoom");

                    } else // if we aren't the creator, we need to validate the boot selection with the creator
                    {
                        try {
                            // validate the boot choice with the host
                            String hostName = GameSession.getCreatorName(sessionID);
                            ValidateBootCommand cmd = new ValidateBootCommand(localPlayerName, c);
                            // send the validateBootCommand. The response from the host will automatically execute.
                            GameManager.getInstance().getComs().sendCommandToIndividual(cmd, hostName); // sent

                            // we've sent the command. now, we can just wait for the response and handle everything else in the execute method of BootValidationResponseCommand
                        } catch (IOException e3) {
                            Logger.getGlobal().info("THere was a problem sending the command.");
                            e3.printStackTrace();
                        }
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
