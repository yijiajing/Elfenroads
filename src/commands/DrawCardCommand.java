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
     * other players. It also notifies all peers to deal travel cards, and only the current player
     * will actually be assigned cards. See the implementation of GameManager.distributeTravelCards.
     */
    @Override
    public void execute() {
        GameState gameState = GameState.instance();
        GameManager gameManager = GameManager.getInstance();
        gameState.getTravelCardDeck().removeFirst(numCards);
        gameManager.distributeTravelCards();
    }
}
