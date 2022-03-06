package commands;

import domain.*;
import enums.RegionType;
import panel.CounterPanel;

public class PlaceObstacleCommand implements GameCommand {

    private String start;
    private String destination;
    private RegionType regionType;

    public PlaceObstacleCommand(Road road) {
        GameMap map = GameMap.getInstance();
        start = map.getRoadSource(road).getName();
        destination = map.getRoadTarget(road).getName();
        regionType = road.getRegionType();
    }

    @Override
    public void execute() {
        GameMap map = GameMap.getInstance();

        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);
        Road road = map.getRoadBetween(startTown, destinationTown, regionType);
        Obstacle obstacle = Obstacle.getNew();
        road.placeObstacle(obstacle);
    }
}
