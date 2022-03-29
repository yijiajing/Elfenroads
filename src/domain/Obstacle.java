package domain;

import windows.MainFrame;
import networking.ActionManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import enums.ObstacleType;

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
    
    public static CounterUnit getNewObstacle() {
        return new Obstacle(MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900);
    }

	@Override
	public CounterUnit getNew() {
		return new Obstacle(MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900);
	}
    
}
