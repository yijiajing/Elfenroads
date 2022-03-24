package commands;

import networking.GameState;

import java.util.logging.Logger;

public class DrawCardCommand implements GameCommand {

    private final int numCards;

    public DrawCardCommand(int numCards) {
        this.numCards = numCards;
    }

    /**
     * Once triggered, it removes the first numCards cards from the deck because they are assigned to
     * other players.
     */
    @Override
    public void execute() {
        Logger.getGlobal().info("Executing DrawCardCommand, removing the first " + numCards + " cards.");
        GameState gameState = GameState.instance();
        gameState.getTravelCardDeck().removeFirst(numCards);
    }
}
