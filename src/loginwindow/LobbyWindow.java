package loginwindow;

import domain.GameManager;
import org.json.JSONObject;
import networking.*;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class LobbyWindow extends JPanel implements ActionListener {

    private JLabel background_elvenroads;
    private static JButton createButton;
    private static JButton loadButton;
    private static JButton gamesButton;
    private static JButton refreshButton;
    private JPanel buttons;
    private JPanel sessions;
    private JLabel gameToJoin;
    private JLabel available;
    private JLabel gameName;
    private JLabel creator;
    private JLabel numPlayers;
    private Box gameInfo;

    LobbyWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        background_elvenroads = MainFrame.instance.getElfenroadsBackground();

        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        gamesButton = new JButton("JOIN");

        refreshButton = new JButton("REFRESH");

        // add an action listener

        gameToJoin = new JLabel();
        gameToJoin.setText("");
        sessions = new JPanel(new BorderLayout());
        available = new JLabel();
        available.setText("Available Sessions");
        sessions.add(available,BorderLayout.PAGE_START);

        try{initializeGameInfo(sessions);}
        catch(IOException gameProblem)
        {
            gameProblem.printStackTrace();
        }

        createButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new VersionToPlayWindow(), "version");
                MainFrame.cardLayout.show(MainFrame.mainPanel,"version");
            }

        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new LoadGameWindow(), "load");
                MainFrame.cardLayout.show(MainFrame.mainPanel,"load");

            }
        });
        gamesButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });
        refreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    initializeGameInfo(sessions);
                }

                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });


        buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(loadButton);
        buttons.add(refreshButton);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 3;
        gbc.gridheight= 3;
        background_elvenroads.setLayout(layout);
        background_elvenroads.add(buttons,gbc);
        gbc.gridy = 3;
        background_elvenroads.add(sessions,gbc);

        add(background_elvenroads);

    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }

    /**
     * designed to be called inside the LobbyWindow to display game information
     * can be called multiple times--it will clear the games displayed and reset every time
     */
    public static void initializeGameInfo(JPanel sessions) throws IOException
    {
        // reset the UI
        sessions.removeAll();

        // get a list of game sessions by ID
        ArrayList<String> gameIDs = GameSession.getAllSessionID();

        int counter = 0;

        // iterate through the IDs and get info for each game & add it to the display
        for (String id : gameIDs)
        {
            // we don't want to display sessions that have already been launched, since we cannot join them anyway
            if (GameSession.isLaunched(id))
            {
                continue;
            }

            // get game info
            System.out.println("We are now looking for details about id " + id);
            JSONObject sessionDetails = GameSession.getSessionDetails(id);
            JSONObject sessionParameters = sessionDetails.getJSONObject("gameParameters"); // TODO: make sure this method works, otherwise call regular get and cast to JSONObject manually instead

            // separate the game info into pieces
            String creator = sessionDetails.get("creator").toString();
            String maxSessionPlayers = sessionParameters.get("maxSessionPlayers").toString();
            String minSessionPlayers = sessionParameters.get("minSessionPlayers").toString();
            String name = sessionParameters.get("name").toString();
            int numPlayers = GameSession.getPlayerNames(id).size();
            // TODO: add support to display other players as well, and any other additional info that would be helpful to the user

            // add the game info to labels
            JLabel creatorLabel = new JLabel("Creator: " + creator);
            JLabel maxPlayersLabel = new JLabel("Max session players: " + maxSessionPlayers);
            JLabel minPlayersLabel = new JLabel("Min session players: " + minSessionPlayers);
            JLabel nameLabel = new JLabel("name: " + name);
            JLabel playersInSessionLabel = new JLabel("players: " + numPlayers);

            // initialize join button
            JButton joinButton = new JButton("JOIN");
            JButton startButton = new JButton("START");

            joinButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // join the game
                    try {
                        GameSession.joinSession(MainFrame.loggedIn, id);
                        GameManager.init(Optional.empty(), id);

                        // prompt user to choose a boot colour
                        // this calls the ChooseBootWindow once all players have responded
                        GameManager.getInstance().requestAvailableColours();

                    } catch (Exception ex) {
                        System.out.println("There was a problem attempting to join the session with User" + User.getInstance().getUsername());
                        ex.printStackTrace();
                        return;
                    }

                  /* TODO FIX THIS
                    try
                    {
                        MainFrame.mainPanel.add(new PlayerWaitWindow(id), "playerwait");
                    }
                    catch (IOException e1)
                    {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                        return;
                    }
                    MainFrame.cardLayout.show(MainFrame.mainPanel,"playerwait");
                } */
                }});

            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameManager.getInstance().launch();
                    // GameManager.init(Optional.empty(), id);
                }
            });


            // initialize the box
            Box gameInfo = Box.createVerticalBox();
            gameInfo.setBorder(BorderFactory.createLineBorder(Color.black));
            // add the button and the labels to the box
            gameInfo.add(playersInSessionLabel);
            gameInfo.add(creatorLabel);
            gameInfo.add(maxPlayersLabel);
            gameInfo.add(minPlayersLabel);
            gameInfo.add(nameLabel);
            gameInfo.add(joinButton);
            gameInfo.add(startButton);

            // add the box to the sessions panel
            // sessions.add(gameInfo);

            if (counter == 0)
            {
                sessions.add(gameInfo, BorderLayout.CENTER);
            }
            else if (counter == 1)
            {
                sessions.add(gameInfo, BorderLayout.LINE_END);
            }

            else if (counter == 2)
            {
                sessions.add(gameInfo, BorderLayout.LINE_START);
            }

            counter++;
            sessions.repaint();
            sessions.revalidate();

        }



    }
}