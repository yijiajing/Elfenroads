package networking;
import java.net.*;
import java.io.*;

public class PlayerClient extends Thread
{
    private Socket aClientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String message;

    public PlayerClient(Socket pClientSocket, String msg)
    {
        aClientSocket = pClientSocket;
        message = msg;
        run();
    }

    public void run()
    {
        try
        {
            out = new PrintWriter(aClientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(aClientSocket.getInputStream()));

            String input = in.readLine();
            System.out.println("the message was: " + input);

            done();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void done() 
    {
        try
        {
            out.println("1");   
            in.close();
            out.close();
            aClientSocket.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    
}
