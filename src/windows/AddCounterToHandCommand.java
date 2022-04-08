package windows;

import commands.GameCommand;
import domain.CounterUnit;
import enums.CounterUnitType;
import gamemanager.GameManager;
import gamescreen.GameScreen;
import networking.GameState;

import java.util.logging.Logger;

public class AddCounterToHandCommand implements GameCommand {

    private final CounterUnitType type;
    private final String senderName;

    public AddCounterToHandCommand(CounterUnit counter) {
        this.type = counter.getType();
        this.senderName = GameManager.getInstance().getThisPlayer().getName();
    }

    @Override
    public void execute() {
        Logger.getGlobal().info("Executing AddCounterToHandCommand with type: " + this.type);

        // Add to the sender player's hand
        CounterUnit newCounter = CounterUnit.getNew(type);
        newCounter.setSecret(false);
        GameState.instance().getPlayerByName(senderName).getHand().addUnit(newCounter);

        GameScreen.getInstance().updateAll();
    }
}
