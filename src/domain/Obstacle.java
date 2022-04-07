package domain;

import enums.GameVariant;
import utils.GameRuleUtils;
import windows.MainFrame;
import networking.ActionManager;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import enums.ObstacleType;
import savegames.SerializableObstacle;

public class Obstacle extends CounterUnit {

    public Obstacle(ObstacleType type, int resizeWidth, int resizeHeight) {
        super(type, resizeWidth, resizeHeight, Integer.toString(type == ObstacleType.TREE || type == ObstacleType.EGTREE ? 9 : 10));

        if (type == ObstacleType.EGTREE || type == ObstacleType.SEAMONSTER) {
            super.initializeMouseListener();
        } else {
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
    }

    public Obstacle(SerializableObstacle loaded) {
        super(loaded.getType(), MainFrame.instance.getWidth() * 67 / 1440, MainFrame.getInstance().getHeight() * 60 / 900, Integer.toString(9));

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

    public static CounterUnit getNew(ObstacleType type) {
        return new Obstacle(type, MainFrame.instance.getWidth() * 67 / 1440,
                MainFrame.instance.getHeight() * 60 / 900);
    }
}