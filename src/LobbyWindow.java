import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyWindow extends JPanel implements ActionListener{
    
    private static Box boxPanel;
    private static JButton createButton;
    private static JButton loadButton;
    private static JButton gamesButton;

    LobbyWindow(){
        
        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");
        gamesButton = new JButton("JOIN");

        createButton.setAlignmentY(-10000);
        gamesButton.setAlignmentY(-50);
        gamesButton.setAlignmentX(-15);
        
        gamesButton.setBounds(50,100,95,30); 

        createButton.addActionListener(this);
        loadButton.addActionListener(this);

        boxPanel = Box.createHorizontalBox();
        boxPanel.add(createButton);
        boxPanel.add(loadButton);
        boxPanel.add(gamesButton);
        //boxPanel.setAlignmentX(-10);

        //add(createButton);
        //add(loadButton);
        //add(gamesButton);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
    }
    
}
