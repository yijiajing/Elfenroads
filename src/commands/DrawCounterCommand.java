package commands;

import domain.TransportationCounter;
import enums.CounterType;
import networking.GameState;
import panel.GameScreen;

import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class DrawCounterCommand implements GameCommand {

    private int numDrawn; // if the counter is drawn from the pile (or ==1 if taken from face-up options)
    private CounterType type; // if the counter is taken from the face-up options, otherwise type==null

    public DrawCounterCommand(int numDrawn, CounterType type) {
        this.numDrawn = numDrawn;
        this.type = type;
    }

    @Override
    public void execute() {
        Logger.getGlobal().info("Executing DrawCounterCommand with num: " + numDrawn + ", type: " + type);
        GameState gameState = GameState.instance();

        // counter was taken from the face-up options
        if (type != null) {
            gameState.removeFaceUpCounter(type);
        }
        // counters were taken from the pile
        else {
            gameState.getCounterPile().removeFirst(numDrawn);
            GameScreen.getInstance().updateAll();
        }
    }
}
