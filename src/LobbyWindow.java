import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

public class LobbyWindow extends JPanel implements ActionListener{
    
    //private static Box boxPanel;
    private JLabel background_elvenroads;
    private static JButton createButton;
    private static JButton loadButton;
    private static JButton gamesButton;
    private JPanel buttons;
    private JPanel sessions;
    private JLabel gameToJoin;
    private JLabel available;
    private JLabel gameName;
    private JLabel creator;
    private JLabel numPlayers;
    private Box gameInfo;

    LobbyWindow(){
        
        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        gamesButton = new JButton("JOIN");
        ImageIcon background_image = 
        new ImageIcon("./assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);
        gameToJoin = new JLabel();
        gameToJoin.setText("");
        sessions = new JPanel(new BorderLayout());
        available = new JLabel();
        available.setText("Avalable Sessions");
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
                MainFrame.cardLayout.show(MainFrame.mainPanel,"version");
                
            }
            
        });
        loadButton.addActionListener(this);
        gamesButton.addActionListener(this);


        buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(loadButton);
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
