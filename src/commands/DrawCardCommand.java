package commands;

import domain.GameManager;
import networking.GameState;

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
        GameState gameState = GameState.instance();
        gameState.getTravelCardDeck().removeFirst(numCards);
    }
}
