import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.regex.Pattern;

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
    
    private String filepathToRepo = "/Users/charlescouture/eclipse-workspace/COMP361/src";
    

    LoginWindow() {

        infoPanel = new JPanel(new BorderLayout());

        ImageIcon background_image = new ImageIcon(filepathToRepo + "/assets/sprites/elfenroads.jpeg");
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
        

        loginButton.addActionListener(new ActionListener()
        {

            @SuppressWarnings("deprecation")
			@Override
            public void actionPerformed(ActionEvent e) 
            {
            	String username = usernameTextField.getText();
            	String password = passwordTextField.getText();
            	boolean u = false;
            	try 
            	{
					u = User.doesUserExist(username);
				} 
            	catch (IOException e1) 
            	{
					e1.printStackTrace();
				}
            	boolean p = Pattern.compile("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,32}").matcher(password).find();
            	if (u && p)
            	{
            		remove(background_elvenroads);
                    MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
            	}
                
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
