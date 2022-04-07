package commands;

import domain.TravelCard;
import domain.CardUnit;
import domain.GoldCard;
import enums.TravelCardType;
import gamescreen.GameScreen;
import networking.GameState;

import java.util.logging.Logger;

public class DrawCardCommand implements GameCommand {

    private final int numCards;
    private final TravelCardType type;

    public DrawCardCommand(int numCards, TravelCardType type) {
        this.numCards = numCards;
        this.type = type;
    }

    /**
     * Once triggered, it removes the first numCards cards from the deck because they are assigned to
     * other players.
     */
    @Override
    public void execute() {
        Logger.getGlobal().info("Executing DrawCardCommand, removing the first " + numCards + " cards.");
        GameState gameState = GameState.instance();
        if (type != null) {
            gameState.removeFaceUpCard(type);
        }else if(numCards == 1) {
        	CardUnit drawnCard = gameState.getTravelCardDeck().draw();
        	if (drawnCard instanceof GoldCard) {
        		gameState.incrementGoldCardDeckCount();
        	}
        }else {
            gameState.getTravelCardDeck().removeFirst(numCards);
        }
    }
}
