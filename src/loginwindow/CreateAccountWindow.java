package loginwindow;

import javax.swing.*;
import java.awt.*;

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
    private static JButton createAccountButton;
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

        createAccountButton = new JButton("Create account");
        createAccountAndLoginButton = new JButton("Create account and log into the LS");

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
        boxPanel.add(createAccountButton);
        boxPanel.add(createAccountAndLoginButton);

        infoPanel.add(boxPanel, BorderLayout.CENTER);

        background.setLayout(layout);
        background.add(infoPanel, constraints);

        add(background);

    }










}
