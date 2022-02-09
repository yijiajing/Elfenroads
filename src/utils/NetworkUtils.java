package utils;

import loginwindow.LoginWindow;
import loginwindow.MainFrame;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    // a class to hold some static utility methods that don't really fit anywhere else

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

    // Popups for network-related errors

    public static Popup initializeNgrokErrorPopup(LoginWindow loginScreen)
    {
        PopupFactory factory = new PopupFactory();
        JLabel display = new JLabel("Ngrok did not start properly. Please check your token input and try again.");

        Popup out = factory.getPopup(loginScreen, display, 800, 225);

        return out;
    }

    public static Popup initializeWrongUsernameErrorPopup(LoginWindow loginScreen)
    {
        PopupFactory factory = new PopupFactory();
        JLabel display = new JLabel("That username does not exist in the LS system. Please try again.");

        Popup out = factory.getPopup(loginScreen, display, 800, 225);

        return out;
    }

    public static Popup initializeWrongPasswordErrorPopup(LoginWindow loginScreen)
    {
        PopupFactory factory = new PopupFactory();
        JLabel display = new JLabel("The password entered is incorrect. Please try again.");

        Popup out = factory.getPopup(loginScreen, display, 800, 225);

        return out;
    }




}
