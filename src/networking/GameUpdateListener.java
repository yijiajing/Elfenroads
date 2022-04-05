package networking;

// is a server socket that listens for game updates and lets the CommunicationsManager it is a part of know when there is an update
// the player will never have to interact with this class directly. It will all be automated by the CommunicationsManager

import commands.GameCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.logging.Logger;

public class GameUpdateListener implements Runnable
{
    private int port;
    private ServerSocket listener;
    private GameState mostRecentUpdate; // not using this for now, trying out sending commands instead
    // private GameCommand command;
    private LinkedList<GameCommand> commands;
    private CommunicationsManager managedBy;

    public GameUpdateListener(int pPort, CommunicationsManager pManagedBy)
    {
        port = pPort;
        managedBy = pManagedBy;
        commands = new LinkedList<GameCommand>();
    }

    @Override
    public void run() {
        try
        {listener = new ServerSocket(port);}
        catch (Exception e)
        {
            Logger.getGlobal().info("Listener intialization failed.");
        }

        while (true) {

            try {
                // listener = new ServerSocket(port);
                Logger.getGlobal().info("Going into accept() method and waiting for information...");
                Socket update = listener.accept(); // the accept () will sit there and wait until an update is received
                Logger.getGlobal().info("Got a message from the update listener! Accept method terminated.");
                InputStream updateContents = update.getInputStream();
                // ObjectInputStream gameStateReceived = new ObjectInputStream(updateContents);
                ObjectInputStream commandReceived = new ObjectInputStream(updateContents);
                readInCommand(commandReceived);
                notifyManager(); // tell the CommunicationsManager that an update has been received
                // listener.close(); // close the connection and do it again

            } catch (Exception e) {
                Logger.getGlobal().info("There was a problem setting up the ServerSocket.");
                e.printStackTrace();
                break;
            }

        }

    }

    // this will read in the Game State being received and save it so that the CommunicationsManager knows how to update it
    private void readInGameState(ObjectInputStream received) throws Exception
    {
        mostRecentUpdate = (GameState) received.readObject();
    }

    private void readInCommand(ObjectInputStream received) throws Exception
    {
        commands.add((GameCommand) received.readObject());
        notifyManager();
    }

    private void notifyManager()
    {
        // notify the communications manager when an update has been received
        managedBy.updateFromListener();
    }

    /**
     * this method will be called by a CommunicationsManager after it has been told there is a game update available
     * @return the last command received by this listener
     */
    public LinkedList <GameCommand> getCommands()
    {
        return commands;
    }
}