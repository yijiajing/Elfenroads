package windows;

import domain.CounterUnit;
import domain.TransportationCounter;
import enums.CounterType;
import gamemanager.EGGameManager;
import gamemanager.GameManager;
import gamescreen.EGGameScreen;
import gamescreen.GameScreen;
import gamemanager.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;

public class ChooseCounterPopup extends JPanel {

    CounterUnit counter1;
    CounterUnit counter2;

    public ChooseCounterPopup(CounterUnit counter1, CounterUnit counter2) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 40));
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth()/4, (int) screenSize.getHeight()/6, (int) screenSize.getWidth()/2, (int) screenSize.getHeight()/2);
        setOpaque(true);

        JPanel textPanel = new JPanel();
        JLabel text = new JLabel();
        text.setText(
                """
                <html>
                Both of these counters are being added to your hand.<br><br>
                You must decide which counter you would like to reveal<br>
                to other players and which one you want to keep hidden.<br><br>
                Click on the counter you wish to place face-up<br>
                and the other counter will be placed face-down.
                </html>
                """);

        text.setFont(new Font("Serif", Font.PLAIN, 25));
        textPanel.add(text);
        textPanel.setVisible(true);

        JPanel counterPanel = new JPanel();
        counterPanel.setVisible(true);

        // image for counter 1
        ImageIcon counterIcon1 = new ImageIcon(counter1.getImageFilePath());
        Image counterIcon1Resized = counterIcon1.getImage().getScaledInstance(screenSize.width/10,
                screenSize.height/6,  java.awt.Image.SCALE_SMOOTH);
        JLabel counterImage1 = new JLabel(new ImageIcon(counterIcon1Resized));
        this.counter1 = counter1;
        addMouseListener(counter1, counterImage1);

        // image for counter 2
        ImageIcon counterIcon2 = new ImageIcon(counter2.getImageFilePath());
        Image counterIcon2Resized = counterIcon2.getImage().getScaledInstance(screenSize.width/10,
               screenSize.height/6,  java.awt.Image.SCALE_SMOOTH);
        JLabel counterImage2 = new JLabel(new ImageIcon(counterIcon2Resized));
        this.counter2 = counter2;
        addMouseListener(counter2, counterImage2);

        counterPanel.add(counterImage1);
        counterPanel.add(Box.createHorizontalStrut(50));
        counterPanel.add(counterImage2);

        add(textPanel);
        add(counterPanel);

        Logger.getGlobal().info("Counter popup should be showing.");
        ActionManager.getInstance().setInExternalWindow(true);
    }


    // TODO delete, just for testing
    public static void main(String[] args) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JFrame frame = new JFrame();
        frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight());
        frame.setVisible(true);
        frame.setLayout(new CardLayout());

        JLayeredPane boardGame_Layers = new JLayeredPane();
        boardGame_Layers.setBounds(0,0,(int) screenSize.getWidth(), (int) screenSize.getHeight());
        boardGame_Layers.add(new ChooseCounterPopup(new TransportationCounter(CounterType.DRAGON,5,5),
                new TransportationCounter(CounterType.MAGICCLOUD, 5,5)));

        frame.add(boardGame_Layers);
    }

    private void addMouseListener(CounterUnit counter, JLabel image) {
        image.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                counter.setSecret(false); // the user has chosen this counter to be face-up
                ((EGGameScreen)GameScreen.getInstance()).hideCounterPopup();
                ActionManager.getInstance().setInExternalWindow(false);
                ((EGGameManager)GameManager.getInstance()).sendCounters(counter1, counter2);
            }
        });
    }
}
