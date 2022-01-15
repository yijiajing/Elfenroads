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
    private String message = "This is server!";

    /* Once the PlayerServer object is created, simply use start(), stop() and sendMessage() methods to communicate */

    public PlayerServer(int pConnections)
    {
        aConnections = pConnections;
        wait = pConnections;
    }

    public void start(int port)
    {
        try
        { 
            serverSocket = new ServerSocket(port);
            setMessage("It worked from the Server!!");

            while(aConnections != 0)
            {
                // create a thread for another player and start up the connection
                new PlayerClient(serverSocket.accept(), message).start();
                aConnections--;
            }

            while(wait != 0)
            {
                // Wait for all threads just in case
                Thread.sleep(1000);
            }

            // Reinitialize wait variable to the number of connections for futur message
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
        PlayerServer server = new PlayerServer(8);
        server.start(6666);
    }
    
}
