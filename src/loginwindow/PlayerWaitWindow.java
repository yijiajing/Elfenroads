package loginwindow;

import gamemanager.GameManager;

import networking.*;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.*;
import java.io.IOException;
import java.awt.BorderLayout;
import java.util.logging.Logger;

public class PlayerWaitWindow extends JPanel implements Runnable
{
    private String aId;
    private Thread t;

    private JLabel background_elvenroads;
    private JLabel wait_message;
    private JPanel message;
    private JPanel panel;
    private JTable table;

    private JButton leaveButton;

    private static String prevPayload = "";

    public PlayerWaitWindow(String pId)
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
        catch (IOException e) {e.printStackTrace();}
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
        panel.setOpaque(false);

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

        // add the "leave" button
        leaveButton = new JButton("LEAVE");
        leaveButton.setBounds(1440*560/1440, 900*700/900, 1440*380/1440, 900*50/900);
        leaveButton.setVisible(true);


        background_elvenroads.add(message, BorderLayout.CENTER);
        background_elvenroads.add(panel, BorderLayout.CENTER);
        background_elvenroads.add(leaveButton, BorderLayout.CENTER);
        add(background_elvenroads);


        // add ActionListener to leave
        leaveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // leave the session
                try
                {GameSession.leaveSession(User.getInstance(), aId);}

                catch (Exception e2)
                {
                    Logger.getGlobal().info("There was a problem leaving the session.");
                    e2.printStackTrace();
                }

                // go back to the lobby
                MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");;
            }
        });

    }

    @Override
    public void run() 
    {
        while (true)
        {   
            try
            {
                // if the session has been launched, go into the game ui
                if (GameSession.isLaunched(aId)) // stop us from continuing to update game information when it has already been started
                {
                    System.out.println("The game session has been launched already! Time to start!");
                    break;
                }

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
                // create a new table
                String[] titles = {"PLAYERS", "NAMES"};

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
                
                // Update UI
                panel.repaint();
                panel.revalidate();
                table.repaint();
                table.revalidate();

                if (aPlayers.size() >= GameSession.getGameParameters(aId).getInt("minSessionPlayers"))
                {
                    wait_message.setText("WAIT FOR THE HOST TO START..."); 
                }
            } 
            catch (IOException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            /*try
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

        GameManager.getInstance().initPlayers();


    }
}
