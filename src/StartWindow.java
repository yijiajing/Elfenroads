import javax.swing.*;


import java.awt.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JPanel {
    
    
    private JLabel background_elvenroads;
    
    private JPanel buttons;
    private JButton startButton;
    private JButton aboutButton;
    private JButton exitButton;

    StartWindow(){
        
        ImageIcon background_image = 
        new ImageIcon("./assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);

        // startButton config
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                NetworkDemoPlayer2.mainPanel.add(new LoginWindow(), "login");
                NetworkDemoPlayer2.cardLayout.show(NetworkDemoPlayer2.mainPanel,"login");
            }
            
        });
        

        // aboutButton config
        aboutButton = new JButton("About");
        //aboutButton.addActionListener(this);
        

        // exitButton config
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);;
                
            }
            
        });
        

        
        buttons = new JPanel();
        buttons.add(startButton);
        buttons.add(aboutButton);
        buttons.add(exitButton);
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

   

    


    
}
