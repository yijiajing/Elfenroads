import javax.swing.*;
import java.awt.Component;
import java.awt.Image;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.minueto.*;

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

    private JLabel background_elefenroads;

    private JButton startButton;
    private JButton aboutButton;
    private JButton exitButton;

    

    StartWindow(){
        startFrame = new JFrame("StartScreen");
        width = startFrame.getWidth();
        height = startFrame.getHeight();

        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        StartScreen();

    }

    private void StartScreen(){
    
        mainPanel = new JPanel();
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setBounds(0, 0, width, height);
        startFrame.add(mainPanel);

        menuPanel = new JPanel();

        // startButton config
        startButton = new JButton("Start");
        startButton.setForeground(new Color(255, 255, 255));
        startButton.setOpaque(false);
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setFocusPainted(false);

        // aboutButton config
        aboutButton = new JButton("Options");
        aboutButton.setForeground(new Color(255, 255, 255));
        aboutButton.setOpaque(false);
        aboutButton.setContentAreaFilled(false);
        aboutButton.setBorderPainted(false);
        aboutButton.setFocusPainted(false);

        // exitButton config
        exitButton = new JButton("Exit");
        exitButton.setForeground(new Color(255, 255, 255));
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);



        ImageIcon background_image = new ImageIcon("C:/Users/philb/Documents/McGill/U3 Fall/COMP 361/elfenroads.jpeg");
        Image background = background_image.getImage();


    }

    

    public static void main(String[] args){
        JFrame startWindow = new JFrame("StartScreen");
        startWindow.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        startWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        startWindow.add(new StartWindow());
        startWindow.setVisible(true);
    }


    
}
