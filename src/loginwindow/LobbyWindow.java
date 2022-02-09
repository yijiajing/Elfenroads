package loginwindow;

import org.json.JSONObject;
import networking.*;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;

public class LobbyWindow extends JPanel implements ActionListener {

    //private static Box boxPanel;

    // private String creator;
    // private String numPlayers;
    // TODO: fill out the other fields to represent any info we need to see about a game


    private JLabel background_elvenroads;
    private static JButton createButton;
    private static JButton loadButton;
    private static JButton gamesButton;
    private static JButton refreshButton;
    private JPanel buttons;
    private JPanel sessions;
    private JLabel gameToJoin;
    private JLabel available;
    private JLabel gameName;
    private JLabel creator;
    private JLabel numPlayers;
    private Box gameInfo;


    private String filepathToRepo = ".";

    LobbyWindow(){

        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        gamesButton = new JButton("JOIN");

        refreshButton = new JButton("REFRESH");

        // add an action listener


        ImageIcon background_image =
                new ImageIcon(filepathToRepo + "/assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);
        gameToJoin = new JLabel();
        gameToJoin.setText("");
        sessions = new JPanel(new BorderLayout());
        available = new JLabel();
        available.setText("Available Sessions");
        sessions.add(available,BorderLayout.PAGE_START);
        gameInfo= Box.createVerticalBox();
        gameName = new JLabel("GAME NAME: Sample");
        creator = new JLabel("CREATOR: John");
        numPlayers = new JLabel("PLAYERS: 4");
        gameInfo.add(gameName);
        gameInfo.add(creator);
        gameInfo.add(numPlayers);
        gameInfo.add(gamesButton);
        sessions.add(gameInfo,BorderLayout.LINE_START);

        createButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);

                MainFrame.mainPanel.add(new VersionToPlayWindow(), "version");
                MainFrame.cardLayout.show(MainFrame.mainPanel,"version");

                // create a new session

                // first, create the gameService

                try
                {
                    // networking.User maex = new networking.User("maex", "abc123_ABC123");
                    // networking.GameService elfenlands = new networking.GameService (maex, "Elfenlands", "Elfenlands", "Password1", 2, 2);
                    // networking.GameSession newGame = new networking.GameSession(maex, "Elfenlands", "savegame2");

                }

                catch (Exception problem)
                {
                    problem.printStackTrace();
                }



            }

        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new LoadGameWindow(), "load");

                MainFrame.cardLayout.show(MainFrame.mainPanel,"load");

            }
        });
        //gamesButton.addActionListener(this);
        gamesButton.addActionListener(new ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {

            }
        });

        refreshButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                try
                {
                    NetworkUtils.initializeGameInfo(sessions, gamesButton);
                }

                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        });


        buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(loadButton);
        buttons.add(refreshButton);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 3;
        gbc.gridheight= 3;
        background_elvenroads.setLayout(layout);
        background_elvenroads.add(buttons,gbc);
        gbc.gridy = 3;
        background_elvenroads.add(sessions,gbc);

        add(background_elvenroads);

    }

    @Override
    public void actionPerformed(ActionEvent e) {


    }
}