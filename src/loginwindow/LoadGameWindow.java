package loginwindow;

import loginwindow.LobbyWindow;
import loginwindow.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class LoadGameWindow extends JPanel implements ActionListener{
    private JLabel background_elvenroads;
    private Box gameInfo;
    private JPanel gamesPanel;
    private JPanel gamePanel;
    private JButton loadButton;
    private JButton backButton;
    private JLabel fileName;
    private JLabel players;
    private JLabel dateSaved;

    LoadGameWindow(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        gamesPanel = new JPanel(new BorderLayout());
        gamePanel = new JPanel(new BorderLayout());
        loadButton = new JButton("Load");
        backButton = new JButton("Back");

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                MainFrame.cardLayout.show(MainFrame.mainPanel, "lobby");
            }
        });

        gamePanel.add(loadButton, BorderLayout.LINE_START);
        gameInfo = Box.createVerticalBox();
        
        //gameInfo
        fileName = new JLabel();
        fileName.setText("FILE NAME: AAA");

        players = new JLabel();
        players.setText("PLAYERS: AA; BB; CC; DD");

        dateSaved = new JLabel();
        dateSaved.setText("DATE SAVED: 9/30/2021 21:30");

        gameInfo.add(fileName);
        gameInfo.add(players);
        gameInfo.add(dateSaved);

        gamePanel.add(gameInfo,BorderLayout.CENTER);

        gamesPanel.add(gamePanel, BorderLayout.LINE_START);

        background_elvenroads = MainFrame.instance.getElfenroadsBackground();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight= 3;
        background_elvenroads.setLayout(layout);
        background_elvenroads.add(gamesPanel,gbc);
        background_elvenroads.add(backButton);


        add(background_elvenroads);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
