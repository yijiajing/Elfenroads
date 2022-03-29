package commands;

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
    
    private CounterUnit pCounter;

    public ReturnCounterUnitCommand(CounterUnit returnedCounter) {
        this.type = returnedCounter.getType();
        this.isSecret = returnedCounter.isSecret();
        this.senderName = GameManager.getInstance().getThisPlayer().getName();
        pCounter = returnedCounter;
    }

    /**
     * Create a new counter with the specified type and add it to the counter pile
     */
    @Override
    public void execute() {
        Logger.getGlobal().info("Executing ReturnCounterCommand, keep " + type);
        CounterUnit counter = pCounter.getNew();
        GameState.instance().getCounterPile().addDrawable(counter);

        // update the sending player's hand
        CounterUnit newCounter = pCounter.getNew();
        newCounter.setSecret(isSecret);
        Hand senderHand = GameState.instance().getPlayerByName(senderName).getHand();
        senderHand.clearCounters();
        senderHand.addUnit(newCounter);
    }

}
