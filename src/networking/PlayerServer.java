package networking;
import java.net.*;
import java.io.*;
//Use port > 1024

public class PlayerServer 
{
    //Global variable to wait for all threads to finish exectuting
    public static int wait;

    private int aConnections;
    private ServerSocket serverSocket;
    private String message;

    /* Once the PlayerServer object is created, simply use start(), stop() and setMessage() methods to communicate */

    public PlayerServer(int pConnections)
    {
        aConnections = pConnections;
        wait = pConnections;
    }

    public void start(int port)
    {
        try
        { 
            // Open ServerSocket and set the message for the other incomming connections
            serverSocket = new ServerSocket(port);

            // Loops for every other Players
            while(aConnections != 0)
            {
                // create a thread for another player and start up the connection and send message to it
                new PlayerClient(serverSocket.accept(), message).start();
                aConnections--;
            }

            // Busy waiting to make sure every Thread finished their execution before closing the connection
            // Sleep to use less cpu while waiting
            while(wait != 0)
            {
                Thread.sleep(1000);
            }

            // Reinitialize "wait" variable to the number of initial connections for futur message and close connection
            wait = aConnections;
            stop();
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
    }

    public void setMessage(String msg)
    {
        message = msg;
    }

    public void stop()
    {
        try
        {
            serverSocket.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

    }

    public static void main(String[] args)
    {
        PlayerServer server = new PlayerServer(2);
        server.setMessage("It worked from the Server!!\n" + "You are thread number " + wait);
        server.start(6666);
    }
    
}
