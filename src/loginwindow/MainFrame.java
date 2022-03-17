package loginwindow;

import com.apple.eawt.ApplicationEvent;
import com.apple.eawt.ApplicationListener;
import domain.GameManager;
import networking.GameSession;
import networking.PlayerServer;
import networking.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.WindowListener;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.logging.Logger;

import javazoom.jl.player.Player;

public class MainFrame extends JFrame implements ApplicationListener
{
    public static CardLayout cardLayout;
    public static JPanel mainPanel;
    public static User loggedIn;
    public static MainFrame instance;

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

    @Override
    /**
     * the code to make sure that pressing command-q on a Mac (quitting the application) has the same effect as just closing the window would
     * will do exactly the same thing as the windowClosing action listener
     */
    public void handleQuit(ApplicationEvent applicationEvent) {
        exitGame();
        PlayerServer.stopNgrok();;
        dispose();
        System.exit(0);
    }

    // we're not going to implement any of these. We just need to put them there so that the compiler doesn't complain.

    @Override
    public void handleAbout(ApplicationEvent applicationEvent) {

    }

    @Override
    public void handleOpenApplication(ApplicationEvent applicationEvent) {

    }

    @Override
    public void handleOpenFile(ApplicationEvent applicationEvent) {

    }

    @Override
    public void handlePreferences(ApplicationEvent applicationEvent) {

    }

    @Override
    public void handlePrintFile(ApplicationEvent applicationEvent) {

    }

    @Override
    public void handleReOpenApplication(ApplicationEvent applicationEvent) {

    }
}
