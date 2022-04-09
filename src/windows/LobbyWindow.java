package windows;

import gamemanager.GameManager;
import enums.GameVariant;
import org.json.JSONObject;
import networking.*;
import utils.NetworkUtils;

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
    private static Thread stopper; // use to forcefully stop the other thread when we need to
    private static int flag = 0;

    static MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");

    private void initThreads()
    {
        t = new Thread(this);
        stopper = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try{Thread.sleep(500);}
                    catch (InterruptedException e3) {e3.printStackTrace();}
                    if (flag == 1)
                    {
                        Logger.getGlobal().info("Attempting to stop the main update thread.");
                        t.stop();
                        break;
                    }

                }

            }
        });
    }

    LobbyWindow()
    {
        initThreads();
        prevPayload = "";
        flag = 0;
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
                track1.play();
                flag = 1;
                t.interrupt(); // kill the thread
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new VersionToPlayWindow(), "version");
                MainFrame.cardLayout.show(MainFrame.mainPanel,"version");
            }

        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                track1.play();
                flag = 1;
                t.interrupt(); // kill the thread
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



        buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(loadButton);
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
        stopper.start();

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
        String getSessionsResponse = null;

        if (prevPayload.equals("")) // when we first get into the window, we will have to make a synchronous api call to show the information
        {
            Logger.getGlobal().info("Sending the first long polling request now...");
            getSessionsResponse = GameSession.getSessionsReturnString();
            prevPayload = getSessionsResponse;
        }

        else // if it is not our first request
        {// get a list of game sessions by ID
            try {
                getSessionsResponse = GameSession.getSessions(prevPayload, t);
                Logger.getGlobal().info("Sending another long poll request...");
            } catch (Exception e) {
                // if interruptedException, return.
                if (e instanceof InterruptedException)
                {
                    return;
                }
                // else, re-call the long polling method (it was probably a timeout)
                else
                {
                    getSessionsResponse = GameSession.getSessions(prevPayload);
                }
            }

            if (getSessionsResponse == null) {
                Logger.getGlobal().info("Failed to initialize the game info in the LobbyWindow using long polling.");
            }
            prevPayload = getSessionsResponse;
        }

        // now that we have new game information, reset the ui
        sessions.removeAll();

        // parse the String response and turn it into a list of String ids
        ArrayList<String> gameIDs = GameSession.getSessionIDFromSessions(getSessionsResponse);

        int counter = 0;

        // iterate through the IDs and get info for each game & add it to the display
        // if there are no sessions, just clear everything
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
            String gameName = sessionParameters.get("name").toString();
            String variant = gameNameToDisplayName(gameName);
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
            JLabel variantLabel = new JLabel("Variant: " + variant);
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
                        track1.play();
                        flag = 1;
                        t.interrupt();
                        String localIP = NetworkUtils.getLocalIPAddPort();
                        GameSession.joinSession(MainFrame.loggedIn, id, localIP);
                        GameManager.init(Optional.empty(), id, interpretVariant(gameName), localIP);

                        // prompt user to choose a boot colour
                        // this calls the ChooseBootWindow once all players have responded
                        GameManager.getInstance().requestAvailableColours();

                    } 
                    catch (Exception ex) 
                    {
                        System.out.println("There was a problem attempting to join the session with User" + User.getInstance().getUsername());
                        ex.printStackTrace();
                    }
                }});



            // initialize the box
            Box gameInfo = Box.createVerticalBox();
            gameInfo.setBorder(BorderFactory.createLineBorder(Color.black));
            // add the button and the labels to the box
            gameInfo.add(playersInSessionLabel);
            gameInfo.add(playerCountLabel);
            gameInfo.add(creatorLabel);
            gameInfo.add(variantLabel);
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
                e.printStackTrace();
            }

        }
        Logger.getGlobal().info("Ended the thread while loop.");
    }

    /**
     * turns a String into a value from the GameVariant enum
     * @param variant the variant as a String, obtained from the LS
     * @return
     */
    public static GameVariant interpretVariant(String variant)
    {
        switch (variant)
        {
            case "Elfenland_Classic": return GameVariant.ELFENLAND_CLASSIC;
            case "Elfenland_Long": return GameVariant.ELFENLAND_LONG;
            case "Elfenland_Destination": return GameVariant.ELFENLAND_DESTINATION; // I know this one is different. It is different in the LS.
            case "Elfengold_Classic": return GameVariant.ELFENGOLD_CLASSIC;
            case "Elfengold_TravelCards": return GameVariant.ELFENGOLD_TRAVEL_CARDS;
            case "Elfengold_RandomGold": return GameVariant.ELFENGOLD_RANDOM_GOLD;
            case "Elfengold_Witch": return GameVariant.ELFENGOLD_WITCH;
            default: return null; // if we set up the LS right, this will never happen
        }
    }

    /**
     * when we fetch the game information for a session , we get the gameName and not the gameDisplayName. The displayName is prettier, so we'll translate it before showing it on the sessions info box
     * @param gameName the game name retrieved from the LS
     * @return
     */
    public static String gameNameToDisplayName(String gameName)
    {
        switch (gameName)
        {
            case "Elfenland_Classic": return "Elfenland (Classic)";
            case "Elfenland_Long": return "Elfenland (Long)";
            case "Elfenland_Destination": return "Elfenland (Destination)";
            case "Elfengold_Classic": return "Elfengold (Classic)";
            case "Elfengold_TravelCards": return "Elfengold (Travel Cards)";
            case "Elfengold_RandomGold": return "Elfengold (Random Gold)";
            case "Elfengold_Witch": return "Elfengold (Witch)";
            default: return null;
        }
    }



}