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

        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        refreshButton = new JButton("REFRESH");
        buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(loadButton);
        buttons.add(refreshButton);

        ImageIcon background_image = new ImageIcon("./assets/sprites/elfenroads.jpeg");
        background = new JLabel(background_image);

        sessionsPanel = new JPanel(new BorderLayout());

        gameToJoin = new JLabel();
        gameToJoin.setText("");

        available = new JLabel();
        available.setText("Available Sessions");
        sessionsPanel.add(available, BorderLayout.PAGE_START);

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
            }
            catch (IOException e1) {
                e1.printStackTrace();
            }

            Box gameBox = Box.createVerticalBox();
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

            sessionsPanel.add(gameBox, BorderLayout.LINE_START);
        }

        sessionsPanel.repaint();
        sessionsPanel.revalidate();

        return sessionsPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }


}