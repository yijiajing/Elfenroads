package commands;

import domain.*;
import enums.ObstacleType;
import enums.RegionType;
import panel.CounterPanel;

public class PlaceObstacleCommand implements GameCommand {

    private final String start;
    private final String destination;
    private final RegionType regionType;
    private final ObstacleType obstacleType;

    public PlaceObstacleCommand(Road road, ObstacleType type) {
        GameMap map = GameMap.getInstance();
        start = map.getRoadSource(road).getName();
        destination = map.getRoadTarget(road).getName();
        regionType = road.getRegionType();
        obstacleType = type;
    }

    @Override
    public void execute() {
        GameMap map = GameMap.getInstance();

        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);
        Road road = map.getRoadBetween(startTown, destinationTown, regionType);
        Obstacle obstacle = (Obstacle)Obstacle.getNew(obstacleType);
        road.placeObstacle(obstacle);
    }
}
