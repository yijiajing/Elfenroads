package commands;

import java.util.List;
import java.util.logging.Logger;

import gamemanager.GameManager;
import domain.CounterUnit;
import domain.Hand;
import enums.CounterUnitType;
import networking.GameState;


/**
 * This command supports all types of counters.
 *
 */
public class ReturnCounterUnitCommand implements GameCommand{
	
    private final CounterUnitType type;
    private final boolean isSecret;
    private final String senderName;

    public ReturnCounterUnitCommand(CounterUnit returnedCounter) {
        this.type = returnedCounter.getType();
        this.isSecret = returnedCounter.isSecret();
        this.senderName = GameManager.getInstance().getThisPlayer().getName();
    }

    /**
     * Create a new counter with the specified type and add it to the counter pile
     */
    @Override
    public void execute() {
        Logger.getGlobal().info("Executing ReturnCounterCommand, keep " + type);
        CounterUnit counter = CounterUnit.getNew(type);
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
