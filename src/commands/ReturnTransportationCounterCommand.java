package commands;

import gamemanager.GameManager;
import domain.TransportationCounter;
import domain.Hand;
import enums.CounterType;
import windows.MainFrame;
import networking.GameState;

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
        TransportationCounter counter = new TransportationCounter(type, MainFrame.instance.getWidth() * 67 / 1440, MainFrame.instance.getHeight() * 60 / 900);
        GameState.instance().getCounterPile().addDrawable(counter);

        // update the sending player's hand
        TransportationCounter newCounter = (TransportationCounter)TransportationCounter.getNew(type);
        newCounter.setSecret(isSecret);
        Hand senderHand = GameState.instance().getPlayerByName(senderName).getHand();
        senderHand.clearCounters();
        senderHand.addUnit(newCounter);
    }
}
