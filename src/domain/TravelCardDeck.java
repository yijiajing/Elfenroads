package domain;

import enums.GameVariant;
import enums.TravelCardType;
import gamescreen.GameScreen;
import utils.GameRuleUtils;

public class TravelCardDeck extends Deck <CardUnit> {

    private GameVariant variant;

    public TravelCardDeck (String sessionID, GameVariant variant) {

        super(sessionID);

        this.variant = variant;

        // the number of each card to add to the pile, depends on the variant
        int numRafts;
        int numOtherTypes;

        if (GameRuleUtils.isElfengoldVariant(variant)) {
            numRafts = 9;
            numOtherTypes = 9;
        } else {
            numRafts = 12;
            numOtherTypes = 10;
        }

        for (TravelCardType type : TravelCardType.values()) {
            // leave out witch cards for now (TODO: incorporate witch variant for elfengold)
            if (type.equals(TravelCardType.WITCH)) {
                continue;
            }

            if (type.equals(TravelCardType.RAFT)) {
                for (int i = 0; i < numRafts; i++) {
                    components.add(new TravelCard(type, GameScreen.getInstance().getWidth()*130/1440, GameScreen.getInstance().getHeight()*2/10));
                }
            } else {
                for (int i = 0; i < numOtherTypes; i++) {
                    components.add(new TravelCard(type, GameScreen.getInstance().getWidth()*130/1440, GameScreen.getInstance().getHeight()*2/10));
                }
            }
        }

        shuffle();
    }
}
