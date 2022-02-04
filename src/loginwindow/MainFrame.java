package loginwindow;

import networking.User;
import org.minueto.MinuetoTool;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame 
{
    public static CardLayout cardLayout;
    public static JPanel mainPanel;
    public static User loggedIn;

    public MainFrame() 
    {
        setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartWindow(), "start");
        mainPanel.add(GameScreen.init(this), "gameScreen");

        add(mainPanel);
        setVisible(true);

        cardLayout.show(mainPanel, "start");
    }

    // Everything starts here
    public static void main(String[] args)
    {
        MainFrame mainFrame = new MainFrame();
    }

}
