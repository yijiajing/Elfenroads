package commands;

import domain.TransportationCounter;
import enums.CounterType;
import networking.GameState;
import panel.GameScreen;

import java.util.ArrayList;
import java.util.Optional;

public class DrawCounterCommand implements GameCommand {

    private int numDrawn; // if the counter is drawn from the pile (or ==1 if taken from face-up options)
    private Optional<CounterType> type; // if the counter is taken from the face-up options

    public DrawCounterCommand(int numDrawn, Optional<CounterType> type) {
        this.numDrawn = numDrawn;
        this.type = type;
    }

    @Override
    public void execute() {
        GameState gameState = GameState.instance();

        // counter was taken from the face-up options
        if (type.isPresent()) {
            ArrayList<TransportationCounter> counters = gameState.getFaceUpCounters();
            for (TransportationCounter c : counters) {
                if (c.getType().equals(type.get())) {
                    counters.remove(c);
                    gameState.addFaceUpCounterFromPile();
                    GameScreen.getInstance().updateAll();
                    return;
                }
            }

            System.out.println("Error: Counter drawn by another player is not present in the face-up counters on this device.");
        }
        // counters were taken from the pile
        else {
            gameState.getCounterPile().removeFirst(numDrawn);
        }
    }
}
