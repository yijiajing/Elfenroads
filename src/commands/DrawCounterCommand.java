package commands;

import domain.GameManager;
import domain.TransportationCounter;
import enums.CounterUnitType;
import networking.GameState;
import panel.GameScreen;
import domain.CounterUnit;

import java.util.logging.Logger;

public class DrawCounterCommand implements GameCommand {

    private final CounterUnitType type; // if the counter is taken from the face-up options, otherwise type==null
    private final boolean isSecret;
    private final boolean isFaceUp;
    private final String senderName;

    public DrawCounterCommand(CounterUnit counter, boolean isFaceUp) {
        this.type = counter.getType();
        this.isSecret = counter.isSecret();
        this.isFaceUp = isFaceUp;
        this.senderName = GameManager.getInstance().getThisPlayer().getName();
    }

    @Override
    public void execute() {
        Logger.getGlobal().info("Executing DrawCounterCommand with type: " + type);
        GameState gameState = GameState.instance();

        // counter was taken from the face-up options
        if (isFaceUp) {
            gameState.removeFaceUpCounter(type);
        }
        // counters were taken from the pile
        else {
            gameState.getCounterPile().removeFirst(1);
        }

        // Add to the sender player's hand
        CounterUnit newCounter = CounterUnit.getNew(type);
        newCounter.setSecret(isSecret);
        GameState.instance().getPlayerByName(senderName).getHand().addUnit(newCounter);

        GameScreen.getInstance().updateAll();
    }
}
