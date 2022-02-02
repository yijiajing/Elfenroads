package loginwindow;

import org.minueto.MinuetoTool;
import panel.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import javazoom.jl.player.Player;

public class MainFrame extends JFrame {

    static CardLayout cardLayout;
    static JPanel mainPanel;

    public MainFrame() {

        setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(new StartWindow(), "start");
        mainPanel.add(GameScreen.init(this), "gameScreen");

        add(mainPanel);
        setVisible(true);

        cardLayout.show(mainPanel, "start");
    }



    public static void main(String[] args){
        MainFrame mainFrame = new MainFrame();
        MP3Player track1 = new MP3Player("./assets/Music/alexander-nakarada-adventure.mp3");
        track1.playReapeated();
        //if (track1.)
        
    }
}
