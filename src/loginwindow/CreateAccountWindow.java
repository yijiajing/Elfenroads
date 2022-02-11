package loginwindow;

import networking.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.Box.createVerticalStrut;

// designed to create an account in the LS
public class CreateAccountWindow extends JPanel {

    // layout stuff, pretty much copied from LoginWindow
    private JLabel background;
    private static Box labelBox;
    private static Box textFieldBox;
    private static Box boxPanel;
    private JPanel infoPanel;

    // fields for input
    private static JTextField usernameTextField;
    private static JPasswordField passwordTextField;
    private static JPasswordField confirmPasswordTextField;

    // labels for the fields
    private static JLabel usernameLabel;
    private static JLabel passwordLabel;
    private static JLabel confirmPasswordLabel;

    // buttons
    private static JButton createAccountAndLoginButton;
    private static JButton backButton;

    CreateAccountWindow()
    {

        MP3Player music = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());

        // layout setup, mostly copied from LoginWindow
        background = MainFrame.instance.getElfenroadsBackground();
        infoPanel = new JPanel(new BorderLayout());

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;

        constraints.gridwidth = 1;
        constraints.gridheight = 3;

        labelBox = Box.createVerticalBox();
        textFieldBox = Box.createVerticalBox();

        usernameTextField = new JTextField(15);
        passwordTextField = new JPasswordField(15);
        confirmPasswordTextField = new JPasswordField(15);

        usernameLabel = new JLabel();
        passwordLabel = new JLabel();
        confirmPasswordLabel = new JLabel();

        usernameLabel.setText("Choose a username:");
        passwordLabel.setText("Choose a password. If you see this message it means Nick forgot to write what the constraints are.");
        confirmPasswordLabel.setText("Confirm your password:");

        createAccountAndLoginButton = new JButton("Create account and log into the LS");
        backButton = new JButton(("Back to login"));

        // add the stuff to the layout
        labelBox.add(usernameLabel);
        usernameLabel.setAlignmentX(LEFT_ALIGNMENT);
        labelBox.add(createVerticalStrut(10));
        labelBox.add(passwordLabel);
        passwordLabel.setAlignmentX(LEFT_ALIGNMENT);
        labelBox.add(createVerticalStrut(10));
        labelBox.add(confirmPasswordLabel);
        confirmPasswordLabel.setAlignmentX(LEFT_ALIGNMENT);

        textFieldBox.add(usernameTextField);
        textFieldBox.add(passwordTextField);
        textFieldBox.add(confirmPasswordTextField);

        boxPanel = Box.createHorizontalBox();
        boxPanel.add(labelBox);
        boxPanel.add(textFieldBox);
        boxPanel.add(createAccountAndLoginButton);
        boxPanel.add(backButton);

        infoPanel.add(boxPanel, BorderLayout.CENTER);

        background.setLayout(layout);
        background.add(infoPanel, constraints);

        add(background);


        passwordTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                // creates an account and takes the user to the game lobby
                // TODO: validate the password before making any API calls

                // register the account at the LS
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();

                // TODO: allow the user to pick his role?
                try {
                    User.registerNewUser(username, password, User.Role.PLAYER);
                    MainFrame.loggedIn = User.init(username, password);
                    MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                    MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
                } catch (Exception ex) {
                    System.out.print("Error: there was a problem creating a user. Please try again.");
                    ex.printStackTrace();
                    return;
                }

            }
        });


        createAccountAndLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // creates an account and takes the user to the game lobby
                // TODO: validate the password before making any API calls

                // register the account at the LS
                String username = usernameTextField.getText();
                String password = passwordTextField.getText();

                // TODO: allow the user to pick his role?
                try {
                    User.registerNewUser(username, password, User.Role.PLAYER);
                    MainFrame.loggedIn = User.init(username, password);
                    MainFrame.mainPanel.add(new LobbyWindow(), "lobby");
                    MainFrame.cardLayout.show(MainFrame.mainPanel,"lobby");
                } catch (Exception ex) {
                    System.out.print("Error: there was a problem creating a user. Please try again.");
                    ex.printStackTrace();
                    return;
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.cardLayout.show(MainFrame.mainPanel, "login");
            }
        });





    }










}
