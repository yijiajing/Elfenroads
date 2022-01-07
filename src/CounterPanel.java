import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CounterPanel extends JPanel {

    private int x;
    private int y;
    private GameScreen gameScreen;
    private TransportationCounter transportationCounter = null;
    private boolean hasObstacle = false;

    public CounterPanel(int x, int y, GameScreen pScreen) {
        this.x = pScreen.getWidth() * x / 1440;
        this.y = pScreen.getHeight() * y / 900;
        this.gameScreen = pScreen;

        pScreen.addElement(this);

        this.setBounds(this.x, this.y, gameScreen.getWidth() * 50 / 1440, gameScreen.getHeight() * 50 / 900);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //TODO: this is only for demonstration,
                // need to implement a mechanism to place a selected transportation counter
                setTransportationCounter(new TransportationCounter(TransportationCounter.CounterType.MAGICCLOUD, 40, 40));
                update();
            }
        });
    }

    public TransportationCounter getTransportationCounter() {
        return transportationCounter;
    }

    public void setTransportationCounter(TransportationCounter transportationCounter) {
        this.transportationCounter = transportationCounter;
        this.add(transportationCounter.getDisplay());
    }

    //TODO: implement obstacle

    public boolean hasObstacle() {
        return hasObstacle;
    }

    public void setObstacle(boolean hasObstacle) {
        this.hasObstacle = hasObstacle;
    }

    public void update()
    {
        this.repaint();
        this.revalidate();
    }
}
