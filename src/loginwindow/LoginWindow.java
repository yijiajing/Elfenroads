package loginwindow;

import networking.*;
import utils.NetworkUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static javax.swing.Box.createVerticalStrut;


public class LoginWindow extends JPanel {

    private JLabel background_elvenroads;
    private static Box labelBox;
    private static Box textFieldBox;
    private static Box boxPanel;
    private static JTextField usernameTextField;
    private static JPasswordField passwordTextField;
    private static JTextField ngrokTextField;
    private static JLabel usernameLabel;
    private static JLabel passwordLabel;
    private static JLabel ngrok;
    private static JButton loginButton;
    private static JButton backButton;
    private JPanel infoPanel;

    private static Box test;
    private static JButton ngrokLogin;
    private static JButton ngrokSingup;
    private static JButton pasteClipboardButton;
    private static JButton createNewAccountButton;

    private static Popup invalidCredentialsPopup;
    private static Popup ngrokErrorPopup;
    private static Popup wrongUsernameErrorPopup;
    private static Popup wrongPasswordErrorPopup;

    LoginWindow() 
    {
        MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        infoPanel = new JPanel(new BorderLayout());

        background_elvenroads = MainFrame.instance.getElfenroadsBackground();

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
        ngrokTextField = new JTextField(20);
        ngrok = new JLabel();
        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        usernameLabel.setText("Username:");
        passwordLabel.setText("Password:");
        ngrok.setText("Ngrok token:");
        loginButton = new JButton("Enter");
        ngrokLogin = new JButton("ngrok LOGIN");
        ngrokSingup = new JButton("ngrok SIGNUP");
        createNewAccountButton = new JButton("Create a new account");
        pasteClipboardButton = new JButton("Paste ngrok token");

        // add popups to be displayed later
        ngrokErrorPopup = NetworkUtils.initializeNgrokErrorPopup(this);
        wrongUsernameErrorPopup = NetworkUtils.initializeWrongUsernameErrorPopup(this);
        wrongPasswordErrorPopup = NetworkUtils.initializeWrongPasswordErrorPopup(this);

        pasteClipboardButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                ngrokTextField.paste();
            }
        });

        createNewAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // open a new createNewAccount window
                MainFrame.mainPanel.add(new CreateAccountWindow(), "createAccount");
                MainFrame.cardLayout.show(MainFrame.mainPanel, "createAccount");
            }
        });

        ngrokSingup.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    Desktop.getDesktop().browse(new URI("https://dashboard.ngrok.com/signup"));

                } 
                catch (IOException | URISyntaxException e1) 
                {
                    e1.printStackTrace();
                }
            }
            
        });
        
        ngrokLogin.addActionListener(new ActionListener() 
        {

            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    Desktop.getDesktop().browse(new URI("https://dashboard.ngrok.com/login"));
                     
                } 
                catch (IOException | URISyntaxException e1) 
                {
                    e1.printStackTrace();
                }  
            }
            
        });

        loginButton.addActionListener(new ActionListener()
        {

            @SuppressWarnings("deprecation")
			@Override
            public void actionPerformed(ActionEvent e) 
            {
                
//            	String username = usernameTextField.getText();
//            	String password = passwordTextField.getText();
//                String token = ngrokTextField.getText();
                String username = "maex";
                String password = "abc123_ABC123";
                String token = "24tPIsoQJZYgdwB6QYLiQtYMhrc_4pYokqaUZXa95qB5ikAx8";
            	boolean u = false;
            	try 
            	{
					u = User.doesUsernameExist(username);
				} 
            	catch (Exception e1) // TODO: create a custom exception for the one raised in doesUsernameExist and edit this to catch both an IOException and the custom one
            	{
					e1.printStackTrace();
				}
            	boolean p = User.isValidPassword(password);
            	// if (u && p)
            	// {
                    try 
                    {
                        // first, make sure the username exists. if not, display the appropriate pop-up.
                        if (!u)
                        {
                                wrongUsernameErrorPopup.show();
                                return;
                        }

                        // log into the LS

                        MainFrame.loggedIn = User.init(username, password);

                        //MainFrame.loggedIn = null; // unecessary to set to null probably but I just want to make sure that we have no issues with User
                        //MainFrame.loggedIn = User.getInstance(username, password);


                    } 
                    catch (Exception loginProblem)
                    {
                        loginProblem.printStackTrace();
                        wrongPasswordErrorPopup.show();
                        return;
                    }

                    // we have made it through the LS login, so the last error to check for is whether Ngrok is running properly

                try {
                    PlayerServer.startNgrok(token);
                    track1.play();
                } catch (IOException ngrokStartupProblem) {
                    ngrokErrorPopup.show();
                    return;
                }

                if (!NetworkUtils.validateNgrok())
                    {
                        ngrokErrorPopup.show();
                        return;
                    }

            		remove(background_elvenroads);
                    MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                    MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
            	// }
                
            }
            
        });

        // making it so that the user can log in by pressing enter
        // the default action listener for a text field is the enter key, so we don't have to specify
        passwordTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();
                String token = ngrokTextField.getText();
                boolean u = false;
                try
                {
                    u = User.doesUsernameExist(username);
                }
                catch (Exception e1) // TODO: create a custom exception for the one raised in doesUsernameExist and edit this to catch both an IOException and the custom one
                {
                    e1.printStackTrace();
                }
                boolean p = User.isValidPassword(password);
                // if (u && p)
                // {
                try
                {
                    // first, make sure the username exists. if not, display the appropriate pop-up.
                    if (!u)
                    {
                        wrongUsernameErrorPopup.show();
                        return;
                    }

                    // log into the LS

                    MainFrame.loggedIn = User.init(username, password);

                    //MainFrame.loggedIn = null; // unecessary to set to null probably but I just want to make sure that we have no issues with User
                    //MainFrame.loggedIn = User.getInstance(username, password);


                }
                catch (Exception loginProblem)
                {
                    loginProblem.printStackTrace();
                    wrongPasswordErrorPopup.show();
                    return;
                }

                // we have made it through the LS login, so the last error to check for is whether Ngrok is running properly

                try {
                    PlayerServer.startNgrok(token);
                    track1.play();
                } catch (IOException ngrokStartupProblem) {
                    ngrokErrorPopup.show();
                    return;
                }

                if (!NetworkUtils.validateNgrok())
                {
                    ngrokErrorPopup.show();
                    return;
                }

                remove(background_elvenroads);
                MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
                // }
            }
        });
        

        labelBox.add(usernameLabel);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);
        labelBox.add(createVerticalStrut(10)); // small separation between lines
        labelBox.add(passwordLabel);
        passwordLabel.setAlignmentX(LEFT_ALIGNMENT);
        labelBox.add(createVerticalStrut(10));
        labelBox.add(ngrok);
        ngrok.setAlignmentX(LEFT_ALIGNMENT);

        textFieldBox.add(usernameTextField);
        textFieldBox.add(passwordTextField);
        textFieldBox.add(ngrokTextField);

        boxPanel = Box.createHorizontalBox();
        boxPanel.add(labelBox);
        boxPanel.add(textFieldBox);
        boxPanel.add(loginButton);
        boxPanel.add(ngrokLogin);
        boxPanel.add(ngrokSingup);
        boxPanel.add(pasteClipboardButton);
        boxPanel.add(createNewAccountButton);

        test = Box.createVerticalBox();
        test.add(pasteClipboardButton);
        
        infoPanel.add(boxPanel,BorderLayout.CENTER);
        infoPanel.add(test,BorderLayout.SOUTH);

        background_elvenroads.setLayout(layout);
        background_elvenroads.add(infoPanel,gbc);

        add(background_elvenroads);

    }

}
