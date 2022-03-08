package commands;

import domain.TravelCard;
import enums.TravelCardType;
import networking.GameState;
import panel.GameScreen;

import java.util.ArrayList;
import java.util.List;

public class ReturnTravelCardsCommand implements GameCommand {

    ArrayList<TravelCardType> travelCardTypes = new ArrayList<>();

    public ReturnTravelCardsCommand(List<TravelCard> travelCards) {
        for (TravelCard c : travelCards) {
            travelCardTypes.add(c.getType());
        }
    }

    /**
     * Create new Travel Cards with the types specified and add them to the deck
     */
    @Override
    public void execute() {
        for (TravelCardType t : travelCardTypes) {
            TravelCard card = new TravelCard(t, GameScreen.getInstance().getWidth()*135/1440, GameScreen.getInstance().getHeight()*2/9);
            GameState.instance().getTravelCardDeck().addDrawable(card);
        }
    }
}
