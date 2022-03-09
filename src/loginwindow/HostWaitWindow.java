package loginwindow;

import domain.GameManager;

import networking.*;

import javax.swing.*;

import java.util.List;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.awt.BorderLayout;
import java.util.logging.Logger;

public class HostWaitWindow extends JPanel implements Runnable
{
    private String aId;
    private Thread t;

    private JButton start;
    private JLabel background_elvenroads;
    private JLabel wait_message;
    private JPanel message;
    private JPanel panel;
    private JTable table;

    private static String prevPayload = ""; // used for long polling

    public HostWaitWindow(String pId)
    {
        try
        {
            aId = pId;
            initThread();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

            background_elvenroads = MainFrame.getInstance().getElfenroadsBackground();
            initUI();

            t.start();
        }
        catch (IOException e)
        {
            // since the calls in initUI should catch their own timeouts, we should never get here
            Logger.getGlobal().info("Caught an IOException in HostWaitWindow constructor. This shouldn't have happened.");
            e.printStackTrace();}
    }

    private void initThread()
    {
        t = new Thread(this);
    }

    public void initUI() throws IOException
    {
        // Table
        panel = new JPanel();
        panel.setBounds(1440*600/1440, 900*400/900, 1440*290/1440, 900*274/900);
        panel.setBackground(Color.DARK_GRAY);

        List<String> aPlayers;

        if (prevPayload.equals(""))
        {
            aPlayers = GameSession.getPlayerNames(aId);
            // set prevPayload for the next request
            prevPayload = GameSession.getSessionDetailsReturnString(aId);
        }

        else
        {
            try
            {
                String getSessionDetailsResponse = GameSession.getSessionDetails(aId, prevPayload);
                prevPayload = getSessionDetailsResponse;
                aPlayers = GameSession.getPlayersFromSessionDetails(getSessionDetailsResponse);
            }
            catch (IOException e)
            {
                // since our API calls are very carefully structured, we can assume that any IOException here is probably called by a timeout on the long poll
                // so, we can just resend the request
                String getSessionDetailsResponse = GameSession.getSessionDetails(aId, prevPayload);
                prevPayload = getSessionDetailsResponse;
                aPlayers = GameSession.getPlayersFromSessionDetails(getSessionDetailsResponse);
            }
        }

        
        String[][] playerNames = new String [aPlayers.size()][2];
        for (int i = 0; i < playerNames.length; i++){
            playerNames[i][0] = String.valueOf(i+1);
            playerNames[i][1] = String.valueOf(aPlayers.get(i));
        }
        
        String[] titles = {"PLAYERS", "NAMES"};
        
        table = new JTable (playerNames, titles);
        table.setRowHeight(900*40/900);

        panel.setLayout(new BorderLayout());
        panel.add(table.getTableHeader(), BorderLayout.PAGE_START);
        panel.add(table, BorderLayout.CENTER);

        // "Please wait..." panel message
        message = new JPanel();
        message.setBounds(1440*580/1440, 900*350/900, 1440*380/1440, 900*50/900);
        message.setOpaque(false);

        wait_message = new JLabel("PLEASE WAIT FOR OTHER PLAYERS...");
        wait_message.setFont(new Font("Calibri", Font.BOLD, 20));

        message.setLayout(new BorderLayout());
        message.add(wait_message,  BorderLayout.CENTER);

        // Hidden Start game button
        start = new JButton("START GAME");
        start.setBounds(1440*560/1440, 900*700/900, 1440*380/1440, 900*50/900);
        start.setVisible(false);

        // add ActionListener to start
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // start a game from the current session
                // launch
                User creator = User.getInstance();
                try
                {GameSession.launch(creator, aId);
                System.out.println("Session launched!");}

                catch (Exception e2)
                {
                    System.out.println("There was a problem launching the session.");
                    e2.printStackTrace();
                }
                // record player names and addresses

                // enter the game ui
                GameManager.getInstance().initPlayers();
            }
        });

        // Add everything to the UI
        background_elvenroads.add(start, BorderLayout.CENTER);
        background_elvenroads.add(message, BorderLayout.CENTER)  ;      
        background_elvenroads.add(panel, BorderLayout.CENTER);
        add(background_elvenroads);
    }

    @Override
    public void run() 
    {
        while (true)
        {   
            try
            {
                if (GameSession.isLaunched(aId)) // stop checking for updates once the session has been launched
                {
                    break;
                }
                // create a new table
                String[] titles = {"PLAYERS", "NAMES"};

                // get all the information to update
                List<String> aPlayers;

                if (prevPayload.equals(""))
                {
                    aPlayers = GameSession.getPlayerNames(aId);
                    // set prevPayload for the next request
                    prevPayload = GameSession.getSessionDetailsReturnString(aId);
                }

                else
                {
                    try
                    {
                        String getSessionDetailsResponse = GameSession.getSessionDetails(aId, prevPayload);
                        prevPayload = getSessionDetailsResponse;
                        aPlayers = GameSession.getPlayersFromSessionDetails(getSessionDetailsResponse);
                    }
                    catch (IOException e)
                    {
                        // since our API calls are very carefully structured, we can assume that any IOException here is probably called by a timeout on the long poll
                        // so, we can just resend the request
                        Logger.getGlobal().info("The request timed out. Resending it.");
                        String getSessionDetailsResponse = GameSession.getSessionDetails(aId, prevPayload);
                        prevPayload = getSessionDetailsResponse;
                        aPlayers = GameSession.getPlayersFromSessionDetails(getSessionDetailsResponse);
                    }
                }

                String[][] playerNames = new String [aPlayers.size()][2];
                for (int i = 0; i < playerNames.length; i++)
                {
                    playerNames[i][0] = String.valueOf(i+1);
                    playerNames[i][1] = String.valueOf(aPlayers.get(i));
                    System.out.println(playerNames[i][1]);
                }

                table = new JTable (playerNames, titles);
                table.setRowHeight(900*40/900);

                // Remove old table and add new one
                panel.removeAll();
                panel.add(table.getTableHeader(), BorderLayout.PAGE_START);
                panel.add(table, BorderLayout.CENTER);
                
                // Get session info
                System.out.println(GameSession.getGameParameters(aId).getInt("minSessionPlayers"));

                // Update UI
                panel.repaint();
                panel.revalidate();
                table.repaint();
                table.revalidate();

                // Check if there are enough players to start the game
                if (aPlayers.size() >= GameSession.getGameParameters(aId).getInt("minSessionPlayers"))
                {
                    wait_message.setText("YOU CAN NOW START THE GAME!!"); 
                    start.setVisible(true);
                }
 
                else
                {
                    wait_message.setText("PLEASE WAIT FOR OTHER PLAYERS...");
                    start.setVisible(false);
                }
            } 
            catch (IOException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*
            try 
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

             */
        }

    }
}