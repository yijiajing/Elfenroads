package domain;

import loginwindow.MainFrame;
import networking.ActionManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import enums.CounterType;
import enums.ObstacleType;
import savegames.SerializableObstacle;

public class Obstacle extends CounterUnit {

    public Obstacle(int resizeWidth, int resizeHeight) {
        super(ObstacleType.OBSTACLE, resizeWidth, resizeHeight, Integer.toString(9));
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getPlacedOn() == null) {
                    ActionManager.getInstance().setSelectedCounter(Obstacle.this);
                } else {
                    // If the counter is placed on a road, then the user's intention is to click on the road
                    ActionManager.getInstance().setSelectedRoad(getPlacedOn());
                }
            }
        });
    }

    public Obstacle (SerializableObstacle loaded)
    {
        super(ObstacleType.OBSTACLE, MainFrame.instance.getWidth()*67/1440, MainFrame.getInstance().getHeight() *60/900, Integer.toString(9));

        // init mouse listener
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getPlacedOn() == null) {
                    ActionManager.getInstance().setSelectedCounter(Obstacle.this);
                } else {
                    // If the counter is placed on a road, then the user's intention is to click on the road
                    ActionManager.getInstance().setSelectedRoad(getPlacedOn());
                }
            }
        });
    }

    public static CounterUnit getNew() {
        return new Obstacle(MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900);
    }


    
}
