package networking;

import commands.*;
import enums.Colour;
import enums.GameVariant;
import gamemanager.GameManager;
import utils.GameRuleUtils;
import utils.NetworkUtils;
import windows.MainFrame;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class CommunicationsManager {

    // this class will be responsible for all inbound and outbound communication during the game
    // it will be comprised of 2 sockets per player--one to listen and one to send information
    // designed to be a singleton, initialized upon joining a game

    // private ArrayList<Socket> senders; // responsible for sending out information
    private static CommunicationsManager INSTANCE;
    private GameUpdateListener listener; // listens for GameState updates from other players

    private GameManager managedBy; // we can access the GameState through here
    private String sessionID; // the ID for the GameSession the player is in (this will be the same value across all players)
    private ArrayList<String> playerAddresses; // this will store the addresses of players. It will be used only at initialization
    private HashMap<String, String> namesAndAddresses;

    private static String localAddress; // stores the local IP

    // private GameCommand lastCommandReceived; // this will be used to update the GameState/GameScreen with whatever command we just received
    // private Queue<GameCommand> toExecute;
    private static int drawCardCommandsExecuted; // used to help ensure the proper order of command execution



    private CommunicationsManager(GameManager pManagedBy, String gameSessionID, String pLocalAddress)
    {
        sessionID = gameSessionID;
        managedBy = pManagedBy;

        localAddress = pLocalAddress;

        // toExecute = new LinkedList<GameCommand>();

        // first, get all the Player addresses so we can set up the sockets
        recordPlayerAddresses();
        // next, set up the ServerSocket to listen for game updates
        setUpListener();
    }

    public static CommunicationsManager init(GameManager pManagedBy, String gameSessionID, String pLocalAddress)
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CommunicationsManager(pManagedBy, gameSessionID, pLocalAddress);
        }
        return INSTANCE;
    }

    public static void reset()
    {
        Logger.getGlobal().info("RESETTING THE COMMUNICATIONS MANAGER. ALL CONNECTIONS WITH PLAYERS WILL BE LOST.");
    }


    /**
     * @pre this method should not be called until after every player has joined the game
     */
    private void recordPlayerAddresses()
    {
        try{playerAddresses = GameSession.getPlayerAddresses(sessionID);}
        catch (IOException e) {Logger.getGlobal().info("There was a problem getting player addresses for this server from the LS. PLease check the session ID and make sure it corresponds to a real session.");}
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
            for (String otherPlayerIP : playerAddresses) {
                // if we are looking at our own address, do nothing
                if (otherPlayerIP.equals(localAddress)) {
                    continue;
                } else {
                    Logger.getGlobal().info("Now connecting to... " + otherPlayerIP);
                    String ip = NetworkUtils.getAddress(otherPlayerIP);
                    int port = NetworkUtils.getPort(otherPlayerIP); // the port should always be 999. we will leave this just in case
                    Socket toThatPlayer = new Socket(ip, port);
                    senders.add(toThatPlayer);
                    Logger.getGlobal().info("Successfully initialized the connection to " + otherPlayerIP + "!");
                }
            }
        }
        catch (Exception e)
        {
            Logger.getGlobal().info("There was a problem setting up the senders.");
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

        Logger.getGlobal().info("Sending the game command " + command.getClass().getName() + " to the other users!");
        for (Socket otherPlayer : senders)
        {
            OutputStream out = otherPlayer.getOutputStream();
            ObjectOutputStream payload = new ObjectOutputStream(out);
            payload.writeObject(command);
//            Logger.getGlobal().info("Wrote the command to the payload.");
//            Logger.getGlobal().info("Flushed the payload.");
//            Logger.getGlobal().info("Closed the socket.");
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
            if (otherPlayerIP.equals(localAddress)) {
                return; // don't send a command to ourself
            }
        } catch (Exception e) {
            Logger.getGlobal().info("There was a problem retrieving the local address.");
        }

//        Logger.getGlobal().info("Now connecting to... " + otherPlayerIP);
        String ip = NetworkUtils.getAddress(otherPlayerIP);
        int port = NetworkUtils.getPort(otherPlayerIP); // the port should always be 999. we will leave this just in case
        Socket thatPlayer = new Socket(ip, port);
//        Logger.getGlobal().info("Successfully initialized the connection to " + otherPlayerIP + "!");

        Logger.getGlobal().info("Sending the game command " + command.getClass().getName() + " to the other user!");
        OutputStream out = thatPlayer.getOutputStream();
        ObjectOutputStream payload = new ObjectOutputStream(out);
        payload.writeObject(command);
        Logger.getGlobal().info("Wrote the command to the payload.");
        Logger.getGlobal().info("Flushed the payload.");
        Logger.getGlobal().info("Closed the socket.");
    }


    /**
     * sends a command to only a single player
     * @param command the command to send for the other player to execute
     * @param recipientName the name of the player to receive the command
     */
    public void sendCommandToIndividual(GameCommand command, String recipientName) throws IOException
    {
        recordPlayerNamesAndAddresses();
        String otherPlayerAddressWithPort = namesAndAddresses.get(recipientName);
        String otherPlayerAddressNoPort = NetworkUtils.getAddress(otherPlayerAddressWithPort);
        int port = NetworkUtils.getPort(otherPlayerAddressWithPort);

        Socket sendCmd = new Socket (otherPlayerAddressNoPort, port);
        Logger.getGlobal().info("Sending the game command " + command.getClass().getName() + " to the other user!");

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
//        Logger.getGlobal().info("Received an update from the listener. Updating the game.");

        while (listener.getCommands().size() > 0)
        {
            Logger.getGlobal().info("Queue looks like: " + listener.getCommands().stream().map(c -> c.getClass().toString()).collect(Collectors.toList()));
            // we always want to execute boot choice-related commands first
            GameCommand firstCommand = listener.getCommands().peek();
            if (firstCommand instanceof GetBootColourCommand || firstCommand instanceof SendBootColourCommand || firstCommand instanceof BootValidationResponseCommand || firstCommand instanceof ValidateBootCommand)
            {
                GameCommand toExecute = listener.getCommands().poll();
                Logger.getGlobal().info("Executing a command related to the boot colour");
                toExecute.execute();
            }

            // if there are still addPlayerCommands on the queue, we need to execute those first
            else if (!playerSetupFinished())
            {
                Logger.getGlobal().info("Player initialization is not finished yet. Looking for another AddPlayerCommand");
                getAndExecuteFirstAddPlayerCommand();
                // get the next AddPlayerCommand from the queue
            }
            else if (GameRuleUtils.isElfengoldVariant(GameState.instance().getGameVariant()) && GameState.instance().getCurrentRound() == 1 || listener.getCommands().peekFirst() instanceof AddGoldCoinsCommand)
            {
                GameCommand toExecute = listener.getCommands().poll();
                toExecute.execute();
            }
            else if (!drawCardsFinished()) // if we are done adding players but not drawing cards, we need to make sure to execute all of the drawCardCommands first
            {
                getAndExecuteFirstDrawCardCommand();
                Logger.getGlobal().info(drawCardCommandsExecuted + " DrawCardCommands have been executed.");
            }

            else // if we are done initializing the players and drawing cards, we can just execute whatever command is next in the queue
            {
                GameCommand toExecute = listener.getCommands().poll();
                toExecute.execute();
            }
        }
    }

    /**
     * used to determine if we have initialized all the players
     * will be called in updateFromListener to make sure that we don't execute any other commands before we have initialized all the players
     *
     * @return
     */
    private boolean playerSetupFinished()
    {
        int numPlayersShouldBe = 9999999;
        try {numPlayersShouldBe = GameSession.getPlayerNames(sessionID).size();}
        catch (IOException e) {
            Logger.getGlobal().info("There was a problem getting all of the player names in the session with ID " + sessionID);}

        int numPlayersInitialized = GameState.instance().getNumOfPlayers();

        return numPlayersShouldBe == numPlayersInitialized;
    }

    /**
     * similarly to playerSetupFinished, this will be called when we deal with the command queue
     * basically, we want to make sure we deal with any drawCardCommands after players are initialized and before any other commands happen
     * @pre we have initialized all of the Players properly
     * @return
     */
    private boolean drawCardsFinished()
    {
        int numPlayers = GameState.instance().getNumOfPlayers();
        int thisPlayerIndex = GameState.instance().getPlayers().indexOf(GameManager.getInstance().getThisPlayer());
        int numCommandsToWaitFor;


        numCommandsToWaitFor = thisPlayerIndex;
        Logger.getGlobal().info("We need to receive " + numCommandsToWaitFor + " DrawCardCommands before we can proceed.");
        
        return drawCardCommandsExecuted == numCommandsToWaitFor;
    }

    /**
     * retrieve and remove the next AddPlayerCommand in the queue
     * @return
     */
    private void getAndExecuteFirstAddPlayerCommand()
    {
        for (int i  = 0; i < listener.getCommands().size(); i++)
        {
            // if it's an AddPlayerCommand, remove and return it
            GameCommand cmd = listener.getCommands().get(i);
            if (cmd instanceof AddPlayerCommand)
            {
                listener.getCommands().remove(i);
                cmd.execute();
                return;
            }
        }

        Logger.getGlobal().info("Some unexpected behavior happened. The system was looking for an AddPlayerCommand in the queue but there wasn't one.");
    }

    /**
     * retrieve and remove the next DrawCardCommand in the queue
     * @return
     */
    private void getAndExecuteFirstDrawCardCommand()
    {
        for (int i  = 0; i < listener.getCommands().size(); i++)
        {
            // if it's an AddPlayerCommand, remove and return it
            GameCommand cmd = listener.getCommands().get(i);
            if (cmd instanceof DrawCardCommand)
            {
                listener.getCommands().remove(i);
                cmd.execute();
                drawCardCommandsExecuted++;
                return;
            }
        }

        Logger.getGlobal().info("Some unexpected behavior happened. The system was looking for a DrawCardCommand in the queue but there wasn't one.");
    }


    // TODO: can we use methods like contains() and remove() with enum values?
    /**
     * will be called when another player tries to validate their color selection
     * checks if the color is taken already
     */
    public boolean checkIfColorAvailable(Colour toCheck)
    {
        return getAvailableColors().contains(toCheck);
    }

    /**
     * will be called when another player's boot choice is validated
     * makes note of the fact when that color is taken
     */
    public static void recordColorChoice(Colour toCheck)
    {
        INSTANCE.getAvailableColors().remove(toCheck);
    }

    public ArrayList<Colour> getAvailableColors() {
        return GameManager.getInstance().getAvailableColours();
    }

    public static CommunicationsManager getINSTANCE() {
        return INSTANCE;
    }

    public static String getLocalAddress() {
        return localAddress;
    }

    public static void clear () {INSTANCE = null;}
}