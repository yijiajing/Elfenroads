package loginwindow;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import networking.*;

public class AboutWindow extends JPanel {

    private JLabel background_elvenroads;
    
    private JPanel buttons;
    private JButton elfenlandRulesButton;
    private JButton elfengoldRulesButton;
    private JButton backButton;
    
    public AboutWindow(){
        ImageIcon background_image = 
        new ImageIcon("./assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);

        //Elfenland rules button
        elfenlandRulesButton = new JButton("Elfenland Rules");
        elfenlandRulesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File("./assets/rules/Elfenland Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
                
            }

            
        });

        //Elfengold rules button
        elfengoldRulesButton = new JButton("Elfengold Rules");
        elfengoldRulesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        File myFile = new File("./assets/rules/Elfengold Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
                
            }

            
        });

        //Back button
        backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                MainFrame.mainPanel.add(new StartWindow(), "start");
                MainFrame.cardLayout.show(MainFrame.mainPanel,"start");
            }

        });
        buttons = new JPanel();
        buttons.add(elfenlandRulesButton);
        buttons.add(elfengoldRulesButton);
        buttons.add(backButton);
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
