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
     
            System.out.println("The client received: " + message + " from thread: " + PlayerServer.wait);

            done();
            synchronized(this)    
            {
                PlayerServer.wait--;
            }

            // kill the thread when it is done executing (maybe will need to find a safer way to kill a thread)
            this.stop();    
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
            in.close();
            out.close();
            aClientSocket.close();
        }
        catch(Exception e)
        {
            System.out.println(e.getStackTrace());
        }
    }

    
}
