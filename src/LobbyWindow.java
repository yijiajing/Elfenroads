import org.json.JSONObject;

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

    private String filepathToRepo = "/Users/charlescouture/eclipse-workspace/COMP361/src";

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
                gameInfo.removeAll();
                gameInfo.repaint();
                gameInfo.revalidate();
                JSONObject j = null;
                try
                {
                    j = GameSession.getSessions();
                }
                catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }

                String id = GameSession.getFirstSessionID(j);
                JSONObject json = null;

                try
                {
                    json = GameSession.getGameParameters(id);
                }
                catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                JSONObject json2 = null;
                try
                {
                    json2 = GameSession.getSessionDetails(id);
                }
                catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                gameInfo.add(new JLabel("creator: " + json2.get("creator")));
                gameInfo.add(new JLabel("MaxSessionPlayers: " + json.get("maxSessionPlayers")));
                gameInfo.add(new JLabel("MinSessionPlayers: " + json.getDouble("minSessionPlayers")));
                gameInfo.add(new JLabel("name: " + json.get("name")));
                gameInfo.add(gamesButton);
                gameInfo.repaint();
                gameInfo.revalidate();
                // TODO: get info about game
                /* JSONObject allSessions = GameSession.getSessions();
                String firstSessionID = GameSession.getFirstSessionID(allSessions);
                JSONObject sessionDetails = GameSession.getSessionDetails(firstSessionID);
                 */

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