package loginwindow;

import networking.User;

import javax.swing.*;
import java.awt.*;

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
    }

    public static MainFrame getInstance() {
        if (instance == null) {
            instance = new MainFrame();
        }
        return instance;
    }

}
