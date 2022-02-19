package networking;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.NetworkUtils;

import java.io.*;
import java.util.ArrayList;
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

    /**
     *
     * @param port the port to use (not sure if this even does anything with ngrok)
     * @param token the user's ngrok token
     */
    public void start(int port, String token)
    {
        try {
            System.out.println("Address this time around: " + NetworkUtils.ngrokAddrToPassToLS());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try
        { 
            // Open ServerSocket and set the message for the other incomming connections
            serverSocket = new ServerSocket(port);
            startNgrok(token); //
            Thread.sleep(1000);
            System.out.println(NetworkUtils.getServerInfo());
            
            // Loops for every other Players
            while(aConnections != 0)
            {
                // create a thread for another player and start up the connection and send message to it
                new PlayerClientHandler(serverSocket.accept(), message).start();
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
            stopNgrok();
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
    }

    public static void startNgrok(String token) throws IOException
    {

        String os = System.getProperty("os.name").toLowerCase();
        String command1 = "ngrok " + token;
        String command2 = "ngrok tcp 6666";
        if (!os.contains("win")) {
            command1 = "./" + command1;
            command2 = "./" + command2;
        }

        Process proc1 = Runtime.getRuntime().exec(command1);
        Process proc2 = Runtime.getRuntime().exec(command2);
    }

    public void setMessage(String msg)
    {
        message = msg;
    }

    public static void stopNgrok()
    {
        try
        {
            //serverSocket.close();
            String command = "killall ngrok";
            Process proc = Runtime.getRuntime().exec(command);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

    }

}
