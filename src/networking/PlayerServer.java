package networking;
import java.net.*;
import java.io.*;
//Use port > 1024

public class PlayerServer 
{
    int aPort;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // just for testing 
    String r = "";

    public void start(int pPort)
    {
        aPort = pPort;
        try
        { 
            serverSocket = new ServerSocket(aPort);
            clientSocket = serverSocket.accept();

            System.out.println("Connection found!");

            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); 
            
            String greeting = in.readLine();
            r = greeting;
            System.out.println(greeting);
            stop();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void stop()
    {
        try
        {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

    }

    public static void main(String[] args)
    {
        PlayerServer server = new PlayerServer();
        server.start(6666);
    }
    
}
