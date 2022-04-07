package commands;

import domain.CounterUnit;
import gamemanager.GameManager;
import domain.TransportationCounter;
import enums.CounterType;
import networking.GameState;

import java.util.List;
import java.util.logging.Logger;

public class ReturnTransportationCounterCommand implements GameCommand {

    private final CounterType type;
    private final boolean isSecret;
    private final String senderName;

    public ReturnTransportationCounterCommand(TransportationCounter returnedCounter) {
        this.type = returnedCounter.getType();
        this.isSecret = returnedCounter.isSecret();
        this.senderName = GameManager.getInstance().getThisPlayer().getName();
    }

    /**
     * Create a new counter with the specified type and add it to the counter pile
     */
    @Override
    public void execute() {
        Logger.getGlobal().info("Executing ReturnTransportationCounterCommand, keep " + type);
        TransportationCounter counter = (TransportationCounter) TransportationCounter.getNew(type);
        // put counter back to the pile
        GameState.instance().getCounterPile().addDrawable(counter);

        // update the sending player's hand
        List<CounterUnit> senderHand = GameState.instance().getPlayerByName(senderName).getHand().getCounters();
        CounterUnit toRemove = null;
        for (CounterUnit c: senderHand) {
            if (c.getType() == type && c.isSecret() == isSecret) {
                toRemove = c;
            }
        }
        assert toRemove != null;
        senderHand.remove(toRemove);
        toRemove.setOwned(false);

    }
}
