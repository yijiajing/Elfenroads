import org.minueto.*;
import javax.swing.*;
import java.awt.*;

class Main {

    public static void main(String[] args){

        JFrame mainFrame = new JFrame("Login");
        mainFrame.setSize(MinuetoTool.getDisplayWidth() - 100, MinuetoTool.getDisplayHeight() - 100);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainFrame.add(new LoginWindow());
        
        
        mainFrame.setVisible(true);
        

    }
}


