package networking;
import java.net.*;
import java.io.*;
//Use port > 1024

public class PlayerServer 
{
    private int aConnections;
    private ServerSocket serverSocket;
    String message = "allo\n";

    /* Once the PlayerServer object is created, simply use start(), stop() and sendMessage() methods to communicate */

    public PlayerServer(int pConnections)
    {
        aConnections = pConnections;
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

            stop();
        }
        catch (Exception e)
        {
            System.out.println(e);
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
        PlayerServer server = new PlayerServer(1);
        server.start(6666);
    }
    
}
