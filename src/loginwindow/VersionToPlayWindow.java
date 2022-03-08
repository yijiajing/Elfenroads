package loginwindow;

import domain.GameManager;
import networking.*;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Optional;

public class VersionToPlayWindow extends JPanel implements ActionListener{
    
    private JLabel background_elvenroads;
    private JPanel choicesPanel;
    private Box choicesBox;
    private Box elfenlandChoice;
    private Box elfengoldChoice;
    private JButton classicGame1;
    private JButton classicGame2;
    private JButton longGame;
    private JButton destinationTown;
    private JButton travelCards;
    private JButton rgtDistribution;
    private JButton elvenWitch;
    private JButton backButton;
    private JLabel elfenlandText;
    private JLabel elfengoldText;

    VersionToPlayWindow(){
        MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        background_elvenroads = MainFrame.getInstance().getElfenroadsBackground();

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight= 3;

        choicesPanel = new JPanel(new BorderLayout());
        choicesBox = Box.createHorizontalBox();
        elfenlandChoice = Box.createVerticalBox();
        elfengoldChoice = Box.createVerticalBox();

        elfenlandText = new JLabel("Elfenland");
        elfengoldText = new JLabel("Elfengold");

        classicGame1 = new JButton("Classic");
        
        longGame = new JButton("Long Game");
        destinationTown = new JButton("Destination Town");

        classicGame1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                GameSession session = null;

                // TODO : add panel for user to input their game name and save game name
                try 
                {
                    track1.play();
                    session = new GameSession(User.getInstance(), "elfenlands", "My Save Game Name");

                    GameManager.init(Optional.empty(), session.getId());

                    // prompt user to choose a boot colour
                    // this calls the ChooseBootWindow once all players have responded
                    GameManager.getInstance().requestAvailableColours();

                } 
                catch (Exception problem) 
                {
                    problem.printStackTrace();
                    return;
                }

            }
        });

        classicGame2 = new JButton("Classic");
        travelCards = new JButton("Travel Cards");
        rgtDistribution = new JButton("Random Gold Token Distribution");
        elvenWitch = new JButton("The Elven Witch");

        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                track1.play();
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
            }
        });

        elfenlandChoice.add(elfenlandText);
        elfenlandText.setAlignmentY(TOP_ALIGNMENT);
        elfenlandText.setAlignmentX(CENTER_ALIGNMENT);
        elfenlandChoice.add(classicGame1);
        classicGame1.setAlignmentX(CENTER_ALIGNMENT);
        elfenlandChoice.add(longGame);
        longGame.setAlignmentX(CENTER_ALIGNMENT);
        elfenlandChoice.add(destinationTown);
        destinationTown.setAlignmentX(CENTER_ALIGNMENT);

        elfengoldChoice.add(elfengoldText);
        elfengoldText.setAlignmentX(CENTER_ALIGNMENT);
        elfengoldChoice.add(classicGame2);
        classicGame2.setAlignmentX(CENTER_ALIGNMENT);
        elfengoldChoice.add(travelCards);
        travelCards.setAlignmentX(CENTER_ALIGNMENT);
        elfengoldChoice.add(rgtDistribution);
        rgtDistribution.setAlignmentX(CENTER_ALIGNMENT);
        elfengoldChoice.add(elvenWitch);
        elvenWitch.setAlignmentX(CENTER_ALIGNMENT);

        choicesBox.add(elfenlandChoice);
        //elfenlandChoice.setAlignmentY(BOTTOM_ALIGNMENT);
        choicesBox.add(elfengoldChoice);
        //elfengoldChoice.setAlignmentY(BOTTOM_ALIGNMENT);

        choicesPanel.add(choicesBox, BorderLayout.CENTER);

        background_elvenroads.setLayout(layout);
        background_elvenroads.add(choicesPanel,gbc);

        background_elvenroads.add(backButton);
        add(background_elvenroads);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
