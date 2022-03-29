package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class NetworkUtils {

    // a class to hold some static utility methods that don't really fit anywhere else


    /**
     * from max's code
     * makes sure a (potential) LS password conforms to the constraints of the system
     * @param password
     * @return
     */
    public static boolean isValidPassword(String password)
    {
        return Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}").matcher(password).find();
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

    public static boolean validateNgrok() {
        // will send a request to the status thing for ngrok to see if it is running
        // we will check using the response code. so, we will say that ngrok startup failed if the response code is anything other than 200.

        // give ngrok time to start up
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


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
     * @pre ngrok is up and running, has been validated by validateNgrok()
     * @return an array: element at index 0 is the ip and element at index 1 is the port, both are String
     */
    public static String[] tokenizeNgrokAddr() throws IOException
    {
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
        results[0] = ip.trim(); // trim whitespaces just in case
        results[1] = port.trim();

        return results;
    }

    // DNS lookup part based on code from https://github.com/DoctorLai/DNSLookup
    public static String ngrokAddrToPassToLS() throws IOException
    {
        String [] info = tokenizeNgrokAddr();
        String dns = info[0];
        String port = info[1];

        // String full = dns + ":" + port;

        // at this stage, the IP is not fit for input into the LS.
        // we need to perform a DNS lookup to get a valid IP address.

        String ip;

        try
        {
            InetAddress add;
            add = InetAddress.getByName(dns);
            ip = add.getHostAddress();
        }

        catch (UnknownHostException e)
        {
            System.out.println("Failed to get the address!");
            e.printStackTrace();
            return null;
        }

        return ip + ":" + port;

    }

    // as of 03-02-2022, ngrok public address changes (at least the DNS -> IP mapping does) too often to work with
    // this method exists to get the local (only usable on the local network) IP to pass to the LS for the local-multiplayer-only implementation
    public static String getLocalIP() throws Exception {
        // need to get the local IP address
        // InetAddress.getLocalHost() returns the loopback address sometimes, so we have to do this a different way

        InetAddress local = InetAddress.getLocalHost();
        String localHostname = local.getHostName();
        InetAddress [] allAddresses = InetAddress.getAllByName(localHostname);

        for (InetAddress address : allAddresses)
        {
            if (address.isLoopbackAddress() || !isValidIP(address.getHostAddress()) || !beginsWithTen(address.getHostAddress())) // we don't want the loopback address or an invalid one, like a MAC address
            {
                // do nothing and keep going
                continue;
            }

            else
            {
                return address.getHostAddress();
            }
        }

        Logger.getGlobal().info("PROBLEM: COULD NOT FIND A VALID NON-LOOPBACK IP ADDRESS");
        throw new Exception("Could not find a valid IP address.");
        // Logger.getGlobal().info("There was a problem finding a valid local IP address for this computer.");
        // throw new Exception ("Could not find an IP that was not a loopback and was a valid IP.");
    }

    /**
     * used for adding a port onto an address since, the LS wants one
     * for now, we are going to use port 999
     * if, for some reason, we want to use a different port, we can overload this method and take port as argument
     * @return the local IP address with a port tacked onto it, to send to the LobbyService
     * @throws UnknownHostException
     */
    public static String getLocalIPAddPort() throws Exception
    {
        // hardcode port 999. this is what everyone will use
        String port = "999";
        // tack the desired port onto the local IP address to make it valid for the LS
        String localIP = getLocalIP();
        String localIPPlusPort = localIP + ":" + port;

        return localIPPlusPort;
    }

    /**
     * based on code from https://www.geeksforgeeks.org/md5-hash-in-java/
     * @param input the stuff to hash (will be a payload for long polling)
     * @return the hashed version of the stuff
     */
    public static String md5Hash(String input)
    {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte [] stuff = md5.digest(input.getBytes());
        BigInteger num = new BigInteger (1, stuff);

        String hash = num.toString(16);

        // bit extend the hash to 32 bits
        while (hash.length() < 32)
        {
            hash = "0" + hash;
        }
        return hash;
    }

    /**
     * @param address: a valid IP address in the format IP: port, returned from an API call to the LS
     * will be called in CommunicationsManager.setUpSenders()
     * @return the IP address (without the port)
     */
    public static String getAddress(String address)
    {
        String [] wholeThingSplit = address.split(":");
        return wholeThingSplit[0];
    }

    /**
     *
     * @param address a valid IP address in the format IP:port, returned from an API call to the LS
     * will be called in CommunicationsManager.setUpSenders()
     * @return the port number
     */
    public static int getPort(String address)
    {
        String [] wholeThingSplit = address.split(":");
        return Integer.parseInt(wholeThingSplit[1]);
    }

    /**
     * taken from LocationValidator.java at github.com/kartoffelquadrat/LobbyService
     * we need this to make sure our getLocalAddress method returns the correct, valid IP and not the MAC address or something (it has happened)
     * @param ip the address to check
     * @return
     */
    public static boolean isValidIP(String ip)
    {
        return Pattern.compile("(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|" +
                "2[0-4][0-9]|25[0-5])").matcher(ip).find();
    }

    public static boolean beginsWithTen(String ip)
    {
       return ip.startsWith("10");
    }




}
