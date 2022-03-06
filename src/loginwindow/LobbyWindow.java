package loginwindow;

import domain.GameManager;
import org.json.JSONObject;
import networking.*;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.util.Optional;

public class LobbyWindow extends JPanel implements ActionListener {

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

    LobbyWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        background_elvenroads = MainFrame.instance.getElfenroadsBackground();

        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        gamesButton = new JButton("JOIN");

        refreshButton = new JButton("REFRESH");

        // add an action listener

        gameToJoin = new JLabel();
        gameToJoin.setText("");
        sessions = new JPanel(new BorderLayout());
        available = new JLabel();
        available.setText("Available Sessions");
        sessions.add(available,BorderLayout.PAGE_START);

        try{NetworkUtils.initializeGameInfo(sessions);}
        catch(IOException gameProblem)
        {
            gameProblem.printStackTrace();
        }

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
                    NetworkUtils.initializeGameInfo(sessions);
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