package commands;

import domain.TransportationCounter;
import domain.TravelCard;
import enums.CounterType;
import loginwindow.MainFrame;
import networking.GameState;

import java.util.logging.Logger;

public class ReturnTransportationCounterCommand implements GameCommand {

    private CounterType type;

    public ReturnTransportationCounterCommand(TransportationCounter returnedCounter) {
        this.type = returnedCounter.getType();
    }

    /**
     * Create a new counter with the specified type and add it to the counter pile
     */
    @Override
    public void execute() {
        Logger.getGlobal().info("Executing ReturnTransportationCounterCommand, return " + type);
        TransportationCounter counter = new TransportationCounter(type, MainFrame.instance.getWidth()*67/1440, MainFrame.instance.getHeight()*60/900);
        GameState.instance().getCounterPile().addDrawable(counter);
        
    }
}
