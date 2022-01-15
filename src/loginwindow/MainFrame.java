package loginwindow;

import org.minueto.MinuetoTool;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame 
{
    static CardLayout cardLayout;
    static JPanel mainPanel;
    StartWindow start;
    LoginWindow login;
    LobbyWindow lobby;
    LobbyWindow lobbyAfterBack;
    VersionToPlayWindow version;
    LoadGameWindow load;
    GameScreen gameScreen;

    public MainFrame() 
    {
        setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartWindow(), "start");
        mainPanel.add(new GameScreen(this), "gameScreen");

        add(mainPanel);
        setVisible(true);

        cardLayout.show(mainPanel, "start");
    }

    public static void main(String[] args)
    {
        MainFrame mainFrame = new MainFrame();
    }
}
