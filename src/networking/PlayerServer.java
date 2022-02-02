package networking;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;

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

    public void start(int port)
    {
        try
        { 
            // Open ServerSocket and set the message for the other incomming connections
            serverSocket = new ServerSocket(port);
            startNgrok("23pqd8dMfU3nAVUXDPkJfk6v4qO_5LPatNkgcUPXZn9rHuGAc");
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

    public static void startNgrok(String token) throws IOException
    {
        String command1 = "./ngrok " + token;
        Process proc1 = Runtime.getRuntime().exec(command1);

        String command2 = "./ngrok tcp 6666";
        Process proc2 = Runtime.getRuntime().exec(command2);
    }

    /**
     * @pre we have validated ngrok setup using validateNgrok()
     * @return
     * @throws IOException
     */
    public static String getServerInfo() throws IOException
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
            //Process proc = Runtime.getRuntime().exec(command);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }

    }

    public static boolean validateNgrok() throws IOException
    {
        // will send a request to the status thing for ngrok to see if it is running
        // we will check using the response code. so, we will say that ngrok startup failed if the response code is anything other than 200.

        try {
            URL url = new URL("http://127.0.0.1:4040/status");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int status = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            // check the status. if it's 200, ngrok is up and running. if not, it's not working

            if (status == 200) {
                return true;
            } else {
                return false;
            }
        }
        catch (IOException e)
        {
            return false;
        }
    }

    /**
     * this will call getServerInfo and get the full ngrok address with the port and everything.
     * it will have to do some sanitizing of the output, and then
     * it will split it and return the ip and the port, ready for the Socket constructor
     * @return an array: element at index 0 is the ip and element at index 1 is the port, both are String
     */
    public static String[] tokenizeNgrokAddr() throws IOException
    {
        // TODO: trim whitespaces just in case
        String fullAddr = getServerInfo();
        String [] tokenized = fullAddr.split(":");

        // at this point we have an array of something like:
        // {"tcp", "//4.tcp.ngrok.io", "14714"}
        // we will ignore the first element and clean up the second element to remove the slashes
        String ipUntrimmed = tokenized[1];
        String ip = ipUntrimmed.replaceAll("/", "");

        // now fill in the return array
        String port = tokenized[2];
        String [] results = new String[2];
        results[0] = ip;
        results[1] = port;

        return results;

    }
}
