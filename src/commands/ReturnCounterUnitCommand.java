package commands;

import java.util.logging.Logger;

import domain.GameManager;
import domain.CounterUnit;
import enums.CounterUnitType;
import loginwindow.MainFrame;
import networking.GameState;

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
        Logger.getGlobal().info("Executing ReturnTransportationCounterCommand, keep " + type);
        CounterUnit counter = new CounterUnit(type, MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900);
        GameState.instance().getCounterPile().addDrawable(counter);

        // update the sending player's hand
        CounterUnit newCounter = CounterUnit.getNew(type);
        newCounter.setSecret(isSecret);
        Hand senderHand = GameState.instance().getPlayerByName(senderName).getHand();
        senderHand.clearCounters();
        senderHand.addUnit(newCounter);
    }

}
