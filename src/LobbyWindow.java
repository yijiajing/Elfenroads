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
    
    LobbyWindow(){
        
        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        gamesButton = new JButton("JOIN");
        ImageIcon background_image = 
        new ImageIcon("C:/Users/philb/Documents/GitHub/f2021-hexanome-12/assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);
        


        createButton.addActionListener(this);
        loadButton.addActionListener(this);


        buttons = new JPanel();
        buttons.add(createButton);
        buttons.add(loadButton);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight= 3;
        background_elvenroads.setLayout(layout);
        background_elvenroads.add(buttons,gbc);

        add(background_elvenroads);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
    }
    
}
