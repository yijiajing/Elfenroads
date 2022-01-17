package networking;
import java.net.*;
import java.util.stream.Collectors;
import java.io.*;

public class PlayerClient 
{
    private String aIp;
    private int aPort;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // Use default constructor to create an instance of a client

    public void startConnection(String ip, int port)
    {
        try
        {
            clientSocket = new Socket(ip, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void stopConnection() 
    {
        try
        {
            in.close();
            out.close();
            clientSocket.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }

    public String getMessage()
    {
        try
        {
            String msg = in.lines().collect(Collectors.joining("\n"));
            return msg;
        }
        catch(Exception e)
        {

        }
        
        return null;
    }
}
