package windows;

import gamemanager.GameManager;
import networking.GameSession;
import networking.PlayerServer;
import networking.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Logger;

public class MainFrame extends JFrame
{
    public static CardLayout cardLayout;
    public static JPanel mainPanel;
    public static User loggedIn;
    public static MainFrame instance;

    // we need to store these to be able to remove and fully reinitialize them later
    private static LobbyWindow lobby;
    private static PlayerWaitWindow playerWait;

    private MainFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent event) 
            {
                exitGame();
                PlayerServer.stopNgrok();;
                dispose();
                System.exit(0);
            }
        });

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartWindow(), "start");

        add(mainPanel);
        setVisible(true);

       }

    // Everything starts here
    public static void main(String[] args)
    {
        MainFrame mainFrame = getInstance();
        cardLayout.show(mainPanel, "start");
        MP3Player track1 = new MP3Player("./assets/Music/alexander-nakarada-adventure.mp3");
        track1.playReapeated();
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

    public JLabel getElfenroadsBackground() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        ImageIcon background_image = new ImageIcon("./assets/sprites/elfenroads.jpeg");
        Image background_image_resized = background_image.getImage().getScaledInstance((int) screenSize.getWidth(), (int) screenSize.getHeight(), java.awt.Image.SCALE_SMOOTH);
        JLabel background_elfenroads = new JLabel(new ImageIcon(background_image_resized));

        return background_elfenroads;
    }

    /**
     * will be called when the main window is closed.
     * will leave any sessions the player is in.
     */
    public static void exitGame()
    {
        // if the user was logged into the LS and in a session, leave the session
        try
        {
            GameSession.leaveSession(User.getInstance(), GameManager.getInstance().getSessionID());
        }
        catch (Exception e) // if the user wasn't logged in and in a session, we don't really have to do anything
        // TODO: handle the case where the User is the host of the session
        {
            Logger.getGlobal().info("User wasn't logged in or wasn't in a session, so nothing needed to be done upon close.");
        }

    }

    public static void setLobbyWindow(LobbyWindow pLobbyWindow)
    {
        lobby = pLobbyWindow;
    }

    public static void setPlayerWaitWindow(PlayerWaitWindow pPlayerWaitWindow)
    {
        playerWait = pPlayerWaitWindow;
    }

    public static LobbyWindow getLobby() {
        return lobby;
    }

    public static PlayerWaitWindow getPlayerWait() {
        return playerWait;
    }
}
