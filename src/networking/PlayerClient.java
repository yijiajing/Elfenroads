package networking;
import java.net.*;
import java.io.*;

public class PlayerClient 
{
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

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

    public String sendMessage(String msg) 
    {
        try
        {
            out.println(msg);
            String resp = in.readLine();
            return resp;
            
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        return null;
        
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
}
