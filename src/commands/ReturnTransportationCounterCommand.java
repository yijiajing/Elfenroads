package commands;

import domain.TransportationCounter;
import networking.GameState;

public class ReturnTransportationCounterCommand implements GameCommand {

    private TransportationCounter returnedCounter;

    public ReturnTransportationCounterCommand(TransportationCounter returnedCounter) {
        this.returnedCounter = returnedCounter;
    }

    /**
     * Put the returned counter back in the counter pile
     */
    @Override
    public void execute() {
        GameState.instance().getCounterPile().addDrawable(returnedCounter);
    }
}
