package panel;

import domain.*;
import enums.CounterType;
import networking.ActionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CounterPanel extends JPanel {

    private final int x;
    private final int y;
    private GameScreen gameScreen;
    private Road road;

    public CounterPanel(int x, int y, Road road, GameScreen pScreen) {
        this.x = pScreen.getWidth() * x / 1440;
        this.y = pScreen.getHeight() * y / 900;
        this.road = road;
        this.gameScreen = pScreen;

        CounterPanel cp = this;

        gameScreen.addElement(this);

        this.setBounds(this.x, this.y, gameScreen.getWidth() * 40 / 1440, gameScreen.getHeight() * 40 / 900);
        this.setOpaque(false);
        this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(CENTER_ALIGNMENT);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionManager.getInstance().setSelectedRoad(CounterPanel.this.road);
                update();
            }

            public void mouseEntered(MouseEvent e){
                cp.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }

            public void mouseExited(MouseEvent e){
                cp.setBorder(null);
            }


        });
    }

    public void setTransportationCounter(TransportationCounter transportationCounter) {
        this.add(transportationCounter.getMiniDisplay());
        this.repaint();
        this.revalidate();
    }

    public void placeObstacle(Obstacle obstacle) {
    	gameScreen.addAncestorListener(null);
    	this.add(obstacle.getMiniDisplay());
    	this.repaint();
    	this.revalidate();
    }

    public void update() {
        this.repaint();
        this.revalidate();
    }

    public void clear() {
        this.removeAll();
        update();
    }
}
