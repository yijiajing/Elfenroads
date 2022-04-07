package commands;

import domain.*;
import enums.CounterType;
import enums.RegionType;
import gamemanager.GameManager;
import networking.GameState;
import gamescreen.GameScreen;

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
        TransportationCounter counter = (TransportationCounter)TransportationCounter.getNew(counterType);
        road.setTransportationCounter(counter);

        // remove the counter from the sending player's hand
        List<CounterUnit> senderHand = GameState.instance().getPlayerByName(senderName).getHand().getCounters();
        CounterUnit toRemove = null;
        for (CounterUnit c: senderHand) {
            if (c.getType() == counterType && c.isSecret() == isSecret) {
                toRemove = c;
            }
        }
        if (toRemove == null) {
            Logger.getGlobal().severe("the counter to remove is not in the sending player's hand");
        } else {
            toRemove.setOwned(false);
            Logger.getGlobal().info("Counter " + toRemove.getType() + " is removed");
            senderHand.remove(toRemove);
        }

        GameScreen.getInstance().updateAll();
    }
}
