package loginwindow;

import domain.GameManager;
import org.json.JSONObject;

import domain.Player;
import networking.*;
import panel.GameScreen;
import utils.NetworkUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.awt.BorderLayout;

public class PlayerWaitWindow extends JPanel implements Runnable
{
    private String aId;
    private Thread t;

    private JLabel background_elvenroads;
    private JLabel wait_message;
    private JPanel message;
    private JPanel panel;
    private JTable table;

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
    
        List<String> aPlayers = GameSession.getPlayerNames(aId);
        
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
                // if the session has been launched, go into the game ui
                // TODO: is it fine to do this here? do we need to leave the thread?
                if (GameSession.isLaunched(aId))
                {
                    System.out.println("The game session has been launched already! Time to start!");
                    break;
                }
                // create a new table
                String[] titles = {"PLAYERS", "NAMES"};

                List<String> aPlayers = GameSession.getPlayerNames(aId);

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

            try 
            {
                Thread.sleep(3000);
            } 
            catch (InterruptedException e) 
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // stop checking for updates
        t.stop();
        GameManager.getInstance().launch();

    }
}
