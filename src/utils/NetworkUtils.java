package utils;

import domain.GameManager;
import loginwindow.LoginWindow;
import loginwindow.MainFrame;
import networking.GameSession;
import networking.GameState;
import org.json.JSONArray;
import org.json.JSONObject;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;
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
     * designed to be called inside the LobbyWindow to display game information
     * can be called multiple times--it will clear the games displayed and reset every time
     */
    public static void initializeGameInfo(JPanel sessions) throws IOException
    {
        // reset the UI
        sessions.removeAll();

        // get a list of game sessions by ID
        ArrayList<String> gameIDs = GameSession.getAllSessionID();

        int counter = 0;

        // iterate through the IDs and get info for each game & add it to the display
        for (String id : gameIDs)
        {
            // get game info
            System.out.println("We are now looking for details about id " + id);
            JSONObject sessionDetails = GameSession.getSessionDetails(id);
            JSONObject sessionParameters = sessionDetails.getJSONObject("gameParameters"); // TODO: make sure this method works, otherwise call regular get and cast to JSONObject manually instead

            // separate the game info into pieces
            String creator = sessionDetails.get("creator").toString();
            String maxSessionPlayers = sessionParameters.get("maxSessionPlayers").toString();
            String minSessionPlayers = sessionParameters.get("minSessionPlayers").toString();
            String name = sessionParameters.get("name").toString();
            // TODO: add support to display other players as well, and any other additional info that would be helpful to the user

            // add the game info to labels
            JLabel creatorLabel = new JLabel("creator: " + creator);
            JLabel maxPlayersLabel = new JLabel("max session players: " + maxSessionPlayers);
            JLabel minPlayersLabel = new JLabel("min session players: " + minSessionPlayers);
            JLabel nameLabel = new JLabel("name: " + name);
            // initialize join button
            JButton joinButton = new JButton("JOIN");
            JButton startButton = new JButton("START");

            joinButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // join the game
                    try {
                        GameSession.joinSession(MainFrame.loggedIn, id);
                    } catch (Exception ex) {
                        System.out.println("There was a problem attempting to join the session with User" + MainFrame.loggedIn.getUsername());
                        ex.printStackTrace();
                        return;
                    }
                }
            });

            startButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    GameState state = GameState.init(GameScreen.init(MainFrame.getInstance()), 3);
                    GameManager.init(Optional.empty(), Optional.of(id));
                }
            });





            // initialize the box
            Box gameInfo = Box.createVerticalBox();
            gameInfo.setBorder(BorderFactory.createLineBorder(Color.black));
            // add the button and the labels to the box
            gameInfo.add(creatorLabel);
            gameInfo.add(maxPlayersLabel);
            gameInfo.add(minPlayersLabel);
            gameInfo.add(nameLabel);
            gameInfo.add(joinButton);
            gameInfo.add(startButton);

            // add the box to the sessions panel
            // sessions.add(gameInfo);

            if (counter == 0)
            {
                sessions.add(gameInfo, BorderLayout.CENTER);
            }
            else if (counter == 1)
            {
                sessions.add(gameInfo, BorderLayout.LINE_END);
            }

            else if (counter == 2)
            {
                sessions.add(gameInfo, BorderLayout.LINE_START);
            }

            counter++;
            sessions.repaint();
            sessions.revalidate();

        }



    }



}
