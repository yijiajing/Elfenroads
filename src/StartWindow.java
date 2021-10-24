import javax.swing.*;
import java.awt.Component;
import java.awt.Image;
import java.awt.*;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.minueto.*;

public class StartWindow extends JPanel implements ActionListener{
    
    
    private JLabel background_elvenroads;
    
    private JPanel buttons;
    private JButton startButton;
    private JButton aboutButton;
    private JButton exitButton;

    StartWindow(){
        //startFrame = new JFrame();
        ImageIcon background_image = 
        new ImageIcon("C:/Users/philb/Documents/GitHub/f2021-hexanome-12/assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);
        // startButton config
        startButton = new JButton("Start");
        startButton.addActionListener(this);
        //startButton.setForeground(Color.BLACK);
        //startButton.setOpaque(false);
        //startButton.setContentAreaFilled(false);
        //startButton.setBorderPainted(false);
        //startButton.setFocusPainted(false);



        // aboutButton config
        aboutButton = new JButton("About");
        aboutButton.addActionListener(this);
        //aboutButton.setForeground(Color.BLACK);
        //aboutButton.setOpaque(false);
        //aboutButton.setContentAreaFilled(false);
        //aboutButton.setBorderPainted(false);
        //aboutButton.setFocusPainted(false);

        // exitButton config
        exitButton = new JButton("Exit");
        exitButton.addActionListener(this);
        //exitButton.setForeground(Color.BLACK);
        //exitButton.setOpaque(false);
        //exitButton.setContentAreaFilled(false);
        //exitButton.setBorderPainted(false);
        //exitButton.setFocusPainted(false);

        
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

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        
    }

    


    
}
