package commands;

import domain.*;
import enums.CounterType;
import enums.RegionType;

import java.util.logging.Logger;

public class PlaceTransportationCounterCommand implements GameCommand {

    private final String start;
    private final String destination;
    private final RegionType regionType;
    private final CounterType counterType;

    public PlaceTransportationCounterCommand(Road road, TransportationCounter counter) {
        GameMap map = GameMap.getInstance();
        start = map.getRoadSource(road).getName();
        destination = map.getRoadTarget(road).getName();
        regionType = road.getRegionType();
        counterType = counter.getType();
    }

    @Override
    public void execute() {
        Logger.getGlobal().info("Executing PlaceTransportationCounterCommand");
        GameMap map = GameMap.getInstance();

        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);
        Road road = map.getRoadBetween(startTown, destinationTown, regionType);
        TransportationCounter counter = TransportationCounter.getNew(counterType);
        road.setTransportationCounter(counter);
    }
}
