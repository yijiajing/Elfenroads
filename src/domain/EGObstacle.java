package domain;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import enums.ObstacleType;
import loginwindow.MainFrame;
import networking.ActionManager;

/**
 * This class represents the regular obstacle and the seaMonster obstacle in Elfengold, and shoud only be used in Elfengold.
 * Didn't rewrite the old EGObstacle class because I wanna leave the old one untouched for Elfenland for now. 
 * TODO: Merge this class into EGObstacle class.
 */
public class EGObstacle extends CounterUnit{
    public EGObstacle(ObstacleType pType, int resizeWidth, int resizeHeight) {
        super(pType, resizeWidth, resizeHeight, Integer.toString(9 + pType.ordinal())); // OBSTACLE is named M09, SEAMONSTER is named M010
        super.initializeMouseListener();
    }

    public static CounterUnit getNew(ObstacleType pType) {
        return new EGObstacle(pType ,MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900);
    }
}
