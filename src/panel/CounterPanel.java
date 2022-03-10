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

        gameScreen.addElement(this);

        this.setBounds(this.x, this.y, gameScreen.getWidth() * 40 / 1440, gameScreen.getHeight() * 70 / 900);
        this.setOpaque(false);
        
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(CENTER_ALIGNMENT);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ActionManager.getInstance().setSelectedRoad(CounterPanel.this.road);
                update();
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
            	CounterPanel.this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            }
            @Override
            public void mouseExited(MouseEvent e) {
            	CounterPanel.this.setBorder(BorderFactory.createEmptyBorder());
            }
            
        });
    }

    public void setTransportationCounter(TransportationCounter transportationCounter) {
        this.add(transportationCounter.getMiniDisplay());
    }

    public void placeObstacle(Obstacle obstacle) {
    	gameScreen.addAncestorListener(null);
    	this.add(obstacle.getSuperMiniDisplay());
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
