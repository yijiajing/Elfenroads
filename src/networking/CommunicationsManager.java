package networking;

import commands.GameCommand;
import commands.GetBootColourCommand;
import commands.MoveBootCommand;
import commands.SendBootColourCommand;
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
import java.util.*;

public class CommunicationsManager {

    // this class will be responsible for all inbound and outbound communication during the game
    // it will be comprised of 2 sockets per player--one to listen and one to send information
    // designed to be a singleton, initialized upon joining a game
    // TODO: turn into a singleton

    // private ArrayList<Socket> senders; // responsible for sending out information
    private GameUpdateListener listener; // listens for GameState updates from other players

    private GameManager managedBy; // we can access the GameState through here
    private String sessionID; // the ID for the GameSession the player is in (this will be the same value across all players)
    private ArrayList<String> playerAddresses; // this will store the addresses of players. It will be used only at initialization
    private HashMap<String, String> namesAndAddresses;

    // private GameCommand lastCommandReceived; // this will be used to update the GameState/GameScreen with whatever command we just received
    private Queue<GameCommand> toExecute;


    public CommunicationsManager(GameManager pManagedBy, String gameSessionID)
    {
        sessionID = gameSessionID;
        managedBy = pManagedBy;

        toExecute = new LinkedList<GameCommand>();

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
     * @pre this method should not be called until after every player has joined the game
     */
    private void recordPlayerNamesAndAddresses()
    {
        try {namesAndAddresses = GameSession.getPlayersWithLocations(sessionID);}
        catch (Exception e) {e.printStackTrace();}
    }

    /**
     * sets up Sockets to send game information to all other players
     * it would be ideal to have all of these set up at the beginning and just reuse them, but the problem is that
     * we cannot start a socket to a player until he has set up his listener
     * @return
     */
    private ArrayList<Socket> setUpSenders()
    {
        recordPlayerAddresses();
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
     * Sends the GameCommand to all other players in the game
     * @throws IOException
     */
    public void sendGameCommandToAllPlayers(GameCommand command) throws IOException
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
     * @pre CommunicationsManager has been initialized with valid fields
     * Sends the GameCommand to the player specified by otherPlayerIP
     * @throws IOException
     */
    public void sendGameCommandToPlayer(GameCommand command, String otherPlayerIP) throws IOException {

        recordPlayerNamesAndAddresses();

        try {
            String localAddress = NetworkUtils.getLocalIPAddPort();
            if (otherPlayerIP.equals(localAddress)) {
                return; // don't send a command to ourself
            }
        } catch (Exception e) {
            System.out.println("There was a problem retrieving the local address.");
        }

        System.out.println("Now connecting to... " + otherPlayerIP);
        String ip = NetworkUtils.getAddress(otherPlayerIP);
        int port = NetworkUtils.getPort(otherPlayerIP); // the port should always be 999. we will leave this just in case
        Socket thatPlayer = new Socket(ip, port);
        System.out.println("Successfully initialized the connection to " + otherPlayerIP + "!");

        System.out.println("Sending the game command to the other user!");
        OutputStream out = thatPlayer.getOutputStream();
        ObjectOutputStream payload = new ObjectOutputStream(out);
        payload.writeObject(command);
        System.out.println("Wrote the command to the payload.");
        payload.flush(); // TODO: do we need to actually flush for the receiver to get the info?
        System.out.println("Flushed the payload.");
        thatPlayer.close(); // TODO: do we want to close the connection? do we leave it open? do we have to reinitialize next time?
        System.out.println("Closed the socket.");
    }


    /**
     * sends a command to only a single player
     * @param command the command to send for the other player to execute
     * @param recipientName the name of the player to receive the command
     */
    public void sendCommandToIndividual(GameCommand command, String recipientName) throws IOException
    {
        String otherPlayerAddressWithPort = namesAndAddresses.get(recipientName);
        String otherPlayerAddressNoPort = NetworkUtils.getAddress(otherPlayerAddressWithPort);
        int port = NetworkUtils.getPort(otherPlayerAddressWithPort);

        Socket sendCmd = new Socket (otherPlayerAddressNoPort, port);

        OutputStream out = sendCmd.getOutputStream();
        ObjectOutputStream payload = new ObjectOutputStream(out);
        payload.writeObject(command);
        payload.flush();
        sendCmd.close();
    }

    /**
     * Called by the GameUpdateListener when an update has been received and is ready to be processed on the UI
     */
    public void updateFromListener()
    {
        System.out.println("Received an update from the listener! Getting ready to update the UI...");

        while (listener.getCommands().size() > 0)
        {
            GameCommand toExecute = listener.getCommands().poll();
            toExecute.execute();
        }
    }
}
