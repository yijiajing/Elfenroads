package networking;

import commands.GameCommand;
import commands.MoveBootCommand;
import domain.ElfBoot;
import domain.GameManager;
import domain.GameMap;
import domain.Town;
import panel.ElfBootPanel;
import panel.GameScreen;
import utils.NetworkUtils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CommunicationsManager {

    // this class will be responsible for all inbound and outbound communication during the game
    // it will be comprised of 2 sockets per player--one to listen and one to send information
    // designed to be a singleton, initialized upon joining a game

    // private ArrayList<Socket> senders; // responsible for sending out information
    private GameUpdateListener listener; // listens for GameState updates from other players

    private GameManager game; // we can access the GameState through here
    private String sessionID; // the ID for the GameSession the player is in (this will be the same value across all players)
    private ArrayList<String> playerAddresses; // this will store the addresses of players. It will be used only at initialization

    private MoveBootCommand lastCommandReceived; // this will be used to update the GameState/GameScreen with whatever command we just received


    public CommunicationsManager(GameManager pGame, String gameSessionID)
    {
        sessionID = gameSessionID;
        game = pGame;

        // first, get all the Player addresses so we can set up the sockets
        recordPlayerAddresses();
        System.out.println("Done recording player addresses...");
        // next, set up the ServerSocket to listen for game updates
        System.out.println("Setting up the listener...");
        setUpListener();
        System.out.println("Done setting up the listener.");
    }


    /**
     * @pre this method should not be called until after every player has joined the game
     */
    private void recordPlayerAddresses()
    {
        try{playerAddresses = GameSession.getPlayerAddresses(sessionID);}
        catch (IOException e) {System.out.println("There was a problem getting player addresses for this server from the LS. PLease check the session ID and make sure it corresponds to a real session.");}
    }

    /**
     * sets up Sockets to send game information to all other players
     * it would be ideal to have all of these set up at the beginning and just reuse them, but the problem is that
     * we cannot start a socket to a player until he has set up his listener
     * @return
     */
    private ArrayList<Socket> setUpSenders()
    {
        ArrayList <Socket> senders = new ArrayList<Socket>();
        try {
            // String localAddress = NetworkUtils.ngrokAddrToPassToLS();
            String localAddress = NetworkUtils.getLocalIPAddPort();
            for (String otherPlayerIP : playerAddresses) {
                // if we are looking at our own address, do nothing
                if (otherPlayerIP.equals(localAddress)) {
                    continue;
                } else {
                    System.out.println("Now connecting to... " + otherPlayerIP);
                    String ip = NetworkUtils.getAddress(otherPlayerIP);
                    int port = NetworkUtils.getPort(otherPlayerIP); // the port should always be 999. we will leave this just in case
                    Socket toThatPlayer = new Socket(ip, port);
                    senders.add(toThatPlayer);
                    System.out.println("Successfully initialized the connection to " + otherPlayerIP + "!");
                }


            }
        }
        catch (Exception e)
        {
            System.out.println("There was a problem setting up the senders.");
            e.printStackTrace();
        }

        return senders;
    }


    /**
     * this sets up the ServerSocket to listen for game updates from other players
     * this needs to be initialized right when we open the game, since
     */
    private void setUpListener()
    {
        // for now, I'm going to try just using one server socket to listen for everyone
        // we should listen on the same port we told the LS we were using

        // int port = NetworkUtils.getPort(NetworkUtils.ngrokAddrToPassToLS());
        // for simplicity for now, we will have all players listen on port 999
        int port = 999; // obviously this is a useless line of code. just here to clearly illustrate what port we're using and to be able to read in a new one if we want to
        listener = new GameUpdateListener(port, this);


        // set up the listener on a different thread
        Thread listenForUpdates = new Thread(listener);
        // start it up
        listenForUpdates.start();

    }

    /**
     * @pre CommunicationsManager has been initialized with valid fields
     * by default, sends the GameState to all other players in the game
     * @throws IOException
     */
    public void sendGameCommand(GameCommand command) throws IOException
    {
        ArrayList<Socket> senders = setUpSenders();

        System.out.println("Sending the game command to the other users!");
        for (Socket otherPlayer : senders)
        {
            OutputStream out = otherPlayer.getOutputStream();
            ObjectOutputStream payload = new ObjectOutputStream(out);
            payload.writeObject(command);
            System.out.println("Wrote the command to the payload.");
            payload.flush(); // TODO: do we need to actually flush for the receiver to get the info?
            System.out.println("Flushed the payload.");
            otherPlayer.close(); // TODO: do we want to close the connection? do we leave it open? do we have to reinitialize next time?
            System.out.println("Closed the socket.");
        }
    }

    /**
     * will be called by the GameUpdateListener when an update has been received and is ready to be processed on the UI
     * this is a specific MoveBootCommand implementation for now
     */
    public void updateFromListener()
    {
        System.out.println("Received an update from the listener! Getting ready to update the UI...");
        lastCommandReceived = (MoveBootCommand) listener.getCommand();
        updateUI();
    }

    /**
     * will be called once an update has been received and the GameScreen needs to be updated
     * this will be a MoveBoot-specific implementation--we will need to create different versions of this method for other commands
     */
    public void updateUI()
    {
        // TODO: figure out wtf i did here last week
        ElfBootPanel startPanelLocally = null;
        ElfBootPanel destPanelLocally = null; // initialized these to null to force them to compile
        ElfBootPanel bootToMoveLocally;

        GameMap map = GameMap.getInstance();

        String startTownName = lastCommandReceived.getStart();
        String destinationTownName = lastCommandReceived.getDestination();
        Town startTown = map.getTown(startTownName);
        Town destinationTown = map.getTown(destinationTownName);

        ElfBootPanel startPanelFromCommmand = startTown.getBootPanel();
        ElfBootPanel destPanelFromCommand = destinationTown.getBootPanel();

        // first, since we receive UI objects, we need to be able to translate those to UI objects on our own computer
        // then, we can execute the command
        GameScreen thisComputer = game.getScreen();

        // go find the panels to update in the GameScreen
        List<Town> towns = thisComputer.getGameMap().getTownList();

        // we need to check every panel in the towns list to see which one we are supposed to update
        for (Town cur : towns)
        {
            ElfBootPanel curPanelToCheck = cur.getPanel().getElfBootPanel();
            if (ElfBootPanel.match(curPanelToCheck, startPanelFromCommmand))
            {
                startPanelLocally = curPanelToCheck;
            }
            else if (ElfBootPanel.match(curPanelToCheck, destPanelFromCommand))
            {
                destPanelLocally = curPanelToCheck;
            }
        }

        // now we know which panels to update. for testing purposes, we'll just arbitrarily pick a boot from the starting panel to move
        // TODO: update this to move the correct boot
        ElfBoot toMove = startPanelLocally.getBootsOnPanel().get(0);

        // now, actually move the boot on the screen
        MoveBootCommand toExecuteLocally = new MoveBootCommand(startPanelLocally, destPanelLocally, toMove);
        toExecuteLocally.execute(game);
        // execute method takes care of updating the ElfBootPanels

        // now redraw the game screen
        thisComputer.draw();

        // the move should now be visible
    }

}
