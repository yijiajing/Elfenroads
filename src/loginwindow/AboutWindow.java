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
        MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        background_elvenroads = MainFrame.instance.getElfenroadsBackground();

        //Elfenland rules button
        elfenlandRulesButton = new JButton("Elfenland Rules");
        elfenlandRulesButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        track1.play();
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
                        track1.play();
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
                track1.play();
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
