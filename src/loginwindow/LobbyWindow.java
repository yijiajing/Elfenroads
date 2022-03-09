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
import java.util.logging.Logger;

public class LobbyWindow extends JPanel implements ActionListener, Runnable {

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

    private static String prevPayload = ""; // used for long polling requests

    private static Thread t;
    private static int flag = 0;

    private void initThread()
    {
        t = new Thread(this);
    }

    LobbyWindow()
    {
        initThread();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        background_elvenroads = MainFrame.instance.getElfenroadsBackground();

        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        gamesButton = new JButton("JOIN");


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

        createButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                //t.stop();
                flag = 1;
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

        t.start();

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
        String getSessionsResponse = null;

        if (prevPayload == "") // when we first get into the window, we will have to make a synchronous api call to show the information
        {

        }

        // get a list of game sessions by ID
        try{getSessionsResponse = GameSession.getSessions(prevPayload);}
        catch (IOException e)
        {
            // we can assume that the exception is because of long polling timeout, so we'll just resend it
            getSessionsResponse = GameSession.getSessions(prevPayload);
        }

        if (getSessionsResponse == null)
        {
            Logger.getGlobal().info("Failed to initialize the game info in the LobbyWindow using long polling.");
        }
        prevPayload = getSessionsResponse;

        ArrayList<String> gameIDs = GameSession.getSessionIDFromSessions(prevPayload);

        int counter = 0;

        // iterate through the IDs and get info for each game & add it to the display
        for (String id : gameIDs)
        {
            // get game info
            JSONObject sessionDetails = GameSession.getSessionDetails(id);
            JSONObject sessionParameters = sessionDetails.getJSONObject("gameParameters");
            ArrayList<String> playerList = GameSession.getPlayerNames(id);
            int numPlayers = playerList.size();
            int maxPlayers = Integer.parseInt(sessionParameters.get("maxSessionPlayers").toString());
            // we don't want to display sessions that have already been launched, since we cannot join them anyway
            if (GameSession.isLaunched(id) || numPlayers == maxPlayers)
            {
                continue;
            }

            // separate the game info into pieces
            String creator = sessionDetails.get("creator").toString();
            String maxSessionPlayers = sessionParameters.get("maxSessionPlayers").toString();
            String minSessionPlayers = sessionParameters.get("minSessionPlayers").toString();
            String name = sessionParameters.get("name").toString();
            String playersOutOfMax = numPlayers + "/" + minSessionPlayers + "-" + maxSessionPlayers;

            String players = "";

            for (String player : playerList)
            {
                // conditionals to avoid having a trailing whitespace in the String
                if (players.equals(""))
                {
                    players = players + player;
                }
                else
                {
                    players = players + ", " + player;
                }
            }



            // TODO: add support to display other players as well, and any other additional info that would be helpful to the user

            // add the game info to labels
            JLabel creatorLabel = new JLabel("Creator: " + creator);
            JLabel nameLabel = new JLabel("Variant: " + name);
            JLabel playersInSessionLabel = new JLabel("Players: " + players);
            JLabel playerCountLabel = new JLabel("Number of Players: " + playersOutOfMax);

            // initialize join button
            JButton joinButton = new JButton("JOIN");

            joinButton.addActionListener(new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    // join the game
                    try 
                    {
                        //t.stop();
                        flag = 1;
                        GameSession.joinSession(MainFrame.loggedIn, id);
                        GameManager.init(Optional.empty(), id);

                        // prompt user to choose a boot colour
                        // this calls the ChooseBootWindow once all players have responded
                        GameManager.getInstance().requestAvailableColours();

                    } 
                    catch (Exception ex) 
                    {
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



            // initialize the box
            Box gameInfo = Box.createVerticalBox();
            gameInfo.setBorder(BorderFactory.createLineBorder(Color.black));
            // add the button and the labels to the box
            gameInfo.add(playersInSessionLabel);
            gameInfo.add(playerCountLabel);
            gameInfo.add(creatorLabel);
            gameInfo.add(nameLabel);
            gameInfo.add(joinButton);

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

    @Override
    public void run() 
    {
        // TODO Auto-generated method stub
        while (true)
        {System.out.println("thread alive");
        if (flag == 1){break;}
            try 
            {
                initializeGameInfo(sessions);
            } 
            catch (IOException e) 
            {
                // TODO Auto-generated catch block
                Logger.getGlobal().info("Caught an IOException in the LobbyWindow. The long polling request probably timed out. Sending a new one.");
                e.printStackTrace();
            }

            try 
            {
                Thread.sleep(5000);
            } 
            catch (InterruptedException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } 
    }
}