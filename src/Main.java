import org.minueto.*;
import javax.swing.*;
import java.awt.*;

class Main {
    static JFrame startFrame;
    static JPanel currentPanel;
    
    public static void main(String[] args){
        startFrame = new JFrame("Elfenroads");
        JFrame loginFrame = new JFrame("Login");
        
        startFrame.setSize(MinuetoTool.getDisplayWidth() - 100, MinuetoTool.getDisplayHeight() - 100);
        startFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        currentPanel = new StartWindow();
        startFrame.add(currentPanel);
        startFrame.setVisible(true);
        

        

    }

    public static void setScreen(JPanel panel){
        //startFrame.setVisible(false);
        Main.currentPanel = panel;
        startFrame.add(currentPanel);
        startFrame.setVisible(true);
    }
}


