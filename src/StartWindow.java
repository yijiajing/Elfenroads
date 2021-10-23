import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JPanel{

    private JFrame startFrame;
    private int width;
    private int height;

    private JPanel mainPanel;
    private JPanel menuPanel;
    private JPanel buttonsPanel;
    private JPanel playPanel;
    private JPanel aboutPanel;
    private JPanel exitPanel;

    private JButton startButton;
    private JButton aboutButton;
    private JButton exitButton;

    StartWindow(){
        startFrame = new JFrame("StartScreen");
        width = startFrame.getWidth();
        height = startFrame.getHeight();

        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    private void StartScreen(){
    
        mainPanel = new JPanel();

    }


    
}
