package loginwindow;

import domain.GameManager;
import org.json.JSONObject;
import networking.*;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class LobbyWindow extends JPanel implements ActionListener {

    private JLabel background;
    private JButton createButton;
    private JButton loadButton;
    private JButton refreshButton;
    private JPanel buttons;
    private JPanel sessionsPanel;
    private JLabel gameToJoin;
    private JLabel available;
    private JSONObject sessions;

    
    public LobbyWindow(){
        MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        track1.play();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        refreshButton = new JButton("REFRESH");
        buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(loadButton);
        buttons.add(refreshButton);

        background = MainFrame.instance.getElfenroadsBackground();

        sessionsPanel = new JPanel();
        sessionsPanel.setLayout(new BoxLayout(sessionsPanel, BoxLayout.LINE_AXIS));

        gameToJoin = new JLabel();
        gameToJoin.setText("");

        available = new JLabel();
        available.setText("Available Sessions");
        sessionsPanel.add(available);

        createButton.addActionListener(e -> {
            remove(background);

            MainFrame.mainPanel.add(new VersionToPlayWindow(), "version");
            MainFrame.cardLayout.show(MainFrame.mainPanel,"version");
        });

        loadButton.addActionListener(e -> {
            remove(background);
            MainFrame.mainPanel.add(new LoadGameWindow(), "load");
            MainFrame.cardLayout.show(MainFrame.mainPanel,"load");
        });

        refreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                draw();
            }
        });


        GridBagLayout layout = new GridBagLayout();
        background.setLayout(layout);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.gridheight= 3;
        background.add(buttons,gbc);
        gbc.gridy = 3;

        background.add(sessionsPanel,gbc);
        add(background);

        draw();

    }

    public void refreshSessions() {
        try {
            sessions = GameSession.getSessions();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public JPanel draw() {

        refreshSessions();
        sessionsPanel.removeAll();

        // TODO: delete dummy box
        Box gameBox1 = Box.createVerticalBox();
        gameBox1.setBorder(BorderFactory.createLineBorder(Color.black));
        gameBox1.add(new JLabel("Creator: John Smith"));
        gameBox1.add(new JLabel("MaxSessionPlayers: 2"));
        gameBox1.add(new JLabel("MinSessionPlayers: 6"));
        gameBox1.add(new JLabel("Name: John's Game"));

        JButton joinButton1 = new JButton("JOIN");
        JButton startButton1 = new JButton("START");
        startButton1.addActionListener(e -> {
            GameManager.init(Optional.empty(), Optional.empty());
        });

        gameBox1.add(joinButton1);
        gameBox1.add(startButton1);

        sessionsPanel.add(gameBox1, BorderLayout.LINE_START);

        ArrayList<String> ids = GameSession.getAllSessionIDs(sessions);

        // draw all sessions to screen
        for (String id : ids) {

            JSONObject gameParams = null;
            JSONObject sessionDetails = null;

            try {
                gameParams = GameSession.getGameParameters(id);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                sessionDetails = GameSession.getSessionDetails(id);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            Box gameBox = Box.createVerticalBox();
            gameBox.setBorder(BorderFactory.createLineBorder(Color.black));

            gameBox.add(new JLabel("Creator: " + sessionDetails.get("creator")));
            gameBox.add(new JLabel("MaxSessionPlayers: " + gameParams.get("maxSessionPlayers")));
            gameBox.add(new JLabel("MinSessionPlayers: " + gameParams.getDouble("minSessionPlayers")));
            gameBox.add(new JLabel("Name: " + gameParams.get("name")));

            JButton joinButton = new JButton("JOIN");
            JButton startButton = new JButton("START");
            startButton.addActionListener(e -> {
                GameManager.init(Optional.empty(), Optional.of(id));
            });

            gameBox.add(joinButton);
            gameBox.add(startButton);

            sessionsPanel.add(gameBox, BorderLayout.LINE_END);
        }

        sessionsPanel.repaint();
        sessionsPanel.revalidate();

        background.repaint();
        background.revalidate();

        return sessionsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}