import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LobbyWindow extends JPanel implements ActionListener{
    
    private static Box boxPanel;
    private static JButton createButton;
    private static JButton loadButton;

    LobbyWindow(){
        
        createButton = new JButton("CREATE NEW SESSION");
        loadButton = new JButton("LOAD SAVED SESSION");

        createButton.addActionListener(this);
        loadButton.addActionListener(this);

        boxPanel = Box.createHorizontalBox();
        boxPanel.add(createButton);
        boxPanel.add(loadButton);

        add(boxPanel);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        
    }
    
}
