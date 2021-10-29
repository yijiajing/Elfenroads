import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.Box.createVerticalStrut;


public class LoginWindow extends JPanel implements ActionListener {

    private JLabel background_elvenroads;
    private static Box labelBox;
    private static Box textFieldBox;
    private static Box boxPanel;
    private static JTextField usernameTextField;
    private static JPasswordField passwordTextField;
    private static JLabel usernameLabel;
    private static JLabel passwordLabel;
    private static JButton loginButton;
    private JPanel infoPanel;
    

    LoginWindow() {

        infoPanel = new JPanel(new BorderLayout());

        ImageIcon background_image = new ImageIcon("./assets/sprites/elfenroads.jpeg");
        background_elvenroads = new JLabel(background_image);
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.gridwidth = 1;
        gbc.gridheight= 3;

        labelBox = Box.createVerticalBox();
        textFieldBox = Box.createVerticalBox();

        usernameTextField = new JTextField(15);
        passwordTextField = new JPasswordField(15);
        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        usernameLabel.setText("Username:");
        passwordLabel.setText("Password:");
        loginButton = new JButton("Enter");
        

        loginButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                remove(background_elvenroads);
                MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
            }
            
        });
        

        labelBox.add(usernameLabel);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);
        labelBox.add(createVerticalStrut(10)); // small separation between lines
        labelBox.add(passwordLabel);
        passwordLabel.setAlignmentX(LEFT_ALIGNMENT);

        textFieldBox.add(usernameTextField);
        textFieldBox.add(passwordTextField);

        boxPanel = Box.createHorizontalBox();
        boxPanel.add(labelBox);
        boxPanel.add(textFieldBox);
        boxPanel.add(loginButton);
        infoPanel.add(boxPanel,BorderLayout.CENTER);
        
        background_elvenroads.setLayout(layout);
        background_elvenroads.add(infoPanel,gbc);

        add(background_elvenroads);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        

    }
}
