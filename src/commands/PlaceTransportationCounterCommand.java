package commands;

import domain.*;
import enums.CounterType;
import enums.RegionType;
import loginwindow.MainFrame;
import panel.CounterPanel;

public class PlaceTransportationCounterCommand implements GameCommand {

    private String start;
    private String destination;
    private RegionType regionType;
    private CounterType counterType;

    public PlaceTransportationCounterCommand(Road road, TransportationCounter counter) {
        GameMap map = GameMap.getInstance();
        start = map.getRoadSource(road).getName();
        destination = map.getRoadTarget(road).getName();
        regionType = road.getRegionType();
        counterType = counter.getType();
    }

    @Override
    public void execute(GameManager manager) {
        GameMap map = GameMap.getInstance();

        Town startTown = map.getTown(start);
        Town destinationTown = map.getTown(destination);
        Road road = map.getRoadBetween(startTown, destinationTown, regionType);
        CounterPanel panel = road.getCounterPanel();
        TransportationCounter counter = new TransportationCounter(counterType, MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900);
        panel.add(counter.getImage());
        panel.update();
    }
}
