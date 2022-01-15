package networking;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.io.*;
//Use port > 1024

public class PlayerServer 
{
    private int aConnections;
    private ServerSocket serverSocket;
    private PrintWriter out;
    private BufferedReader in;

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

            while(aConnections != 0)
            {
                // create a thread for another player and start up the connection
                Socket client = serverSocket.accept();
                sendMessage("Hi from the Server!");

                new PlayerClient(client).start();
                aConnections--;
            }

            stop();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    public void sendMessage(String msg)
    {
        out.println(msg);
    }

    public void stop()
    {
        try
        {
            in.close();
            out.close();
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
