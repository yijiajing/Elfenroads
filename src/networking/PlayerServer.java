package networking;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;

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
            startNgrok(port);
            Thread.sleep(1000);
            System.out.println(getServerInfo());
            
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
            stop();
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }
    }

    public void startNgrok(int port) throws IOException
    {
        String command = "./ngrok tcp " + port;
        Process proc = Runtime.getRuntime().exec(command);
    }

    public String getServerInfo() throws IOException
    {
        URL url = new URL("http://127.0.0.1:4040/api/tunnels");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while((inputLine = in.readLine()) != null) {
	        content.append(inputLine);
        }
        in.close();
        con.disconnect();
        
        // Get the ngrok url with the port
        JSONObject response = new JSONObject(content.toString());
        JSONArray a = response.getJSONArray("tunnels");
        String address = a.getJSONObject(0).getString("public_url");

        return address;
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
            String command = "killall ngrok";
            Process proc = Runtime.getRuntime().exec(command);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

    }
}
