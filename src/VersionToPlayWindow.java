import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JLabel elfenlandText;
    private JLabel elfengoldText;

    VersionToPlayWindow(){
        ImageIcon background_image = 
        new ImageIcon("C:/Users/philb/Documents/GitHub/f2021-hexanome-12/assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);
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
        classicGame1.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                Main.setScreen(new GameScreen(Main.startFrame));
                
            }
            
        });
        longGame = new JButton("Long Game");
        destinationTown = new JButton("Destination Town");

        classicGame2 = new JButton("Classic");
        travelCards = new JButton("Travel Cards");
        rgtDistribution = new JButton("Random Gold Token Distribution");
        elvenWitch = new JButton("The Elven Witch");

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

        add(background_elvenroads);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }
    
}
