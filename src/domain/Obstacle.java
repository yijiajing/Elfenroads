package domain;

import networking.ActionManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Obstacle extends CounterUnit {

    public Obstacle(int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, 9);
        this.getImage().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getPlacedOn() == null) {
                    ActionManager.getInstance().obstacleSelected();
                } else {
                    // If the counter is placed on a road, then the user's intention is to click on the road
                    ActionManager.getInstance().setSelectedRoad(getPlacedOn());
                }
            }
        });
    }

}
