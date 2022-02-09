package loginwindow;

import networking.User;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MainFrame extends JFrame
{
    public static CardLayout cardLayout;
    public static JPanel mainPanel;
    public static User loggedIn;

    public static MainFrame instance;

    private MainFrame()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
}
