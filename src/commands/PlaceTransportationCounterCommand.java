package commands;

import domain.*;
import enums.CounterType;
import enums.RegionType;
import networking.GameState;
import panel.GameScreen;

import java.util.List;
import java.util.logging.Logger;

public class PlaceTransportationCounterCommand implements GameCommand {

    private final String start;
    private final String destination;
    private final RegionType regionType;
    private final CounterType counterType;
    private final boolean isSecret;
    private final String senderName;

    public PlaceTransportationCounterCommand(Road road, TransportationCounter counter) {
        GameMap map = GameMap.getInstance();
        start = map.getRoadSource(road).getName();
        destination = map.getRoadTarget(road).getName();
        regionType = road.getRegionType();
        counterType = counter.getType();
        isSecret = counter.isSecret();
        senderName = GameManager.getInstance().getThisPlayer().getName();
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

        // remove the counter from the sending player's hand
        List<TransportationCounter> senderHand = GameState.instance().getPlayerByName(senderName).getHand().getCounters();
        int toRemoveIdx = -1;
        for (int i = 0; i < senderHand.size(); i++) {
            if (senderHand.get(i).getType() == counterType
                    && senderHand.get(i).isSecret() == isSecret) {
                toRemoveIdx = i;
            }
        }
        assert toRemoveIdx >= 0; // The counter should be in the sending player's hand
        senderHand.remove(toRemoveIdx);

        GameScreen.getInstance().updateAll();
    }
}
