import org.minueto.MinuetoTool;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    static CardLayout cardLayout;
    static JPanel mainPanel;
    StartWindow start;
    LoginWindow login;
    LobbyWindow lobby;
    LobbyWindow lobbyAfterBack;
    VersionToPlayWindow version;
    LoadGameWindow load;
    GameScreen gameScreen;

    MainFrame() {

        setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();

        mainPanel = new JPanel(cardLayout);
        start = new StartWindow();
        login = new LoginWindow();
        lobby = new LobbyWindow();
        lobbyAfterBack = new LobbyWindow();
        version = new VersionToPlayWindow();
        load = new LoadGameWindow();
        gameScreen = new GameScreen(this);

        mainPanel.add(start, "start");
        mainPanel.add(login, "login");
        mainPanel.add(lobby, "lobby");
        mainPanel.add(lobbyAfterBack, "lobbyAfterBack");
        mainPanel.add(version, "version");
        mainPanel.add(load, "load");
        mainPanel.add(gameScreen, "gameScreen");

        add(mainPanel);
        setVisible(true);

        cardLayout.show(mainPanel, "start");
    }

    public static void main(String[] args){
        MainFrame mainFrame = new MainFrame();
    }
}
