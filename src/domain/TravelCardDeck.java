package domain;

import enums.TravelCardType;
import panel.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class TravelCardDeck <CardUnit> extends Deck {


    public TravelCardDeck (String sessionID) {

        super(sessionID);

        for (TravelCardType type : TravelCardType.values()) {
            // leave out witch cards for now (TODO: incorporate witch variant for elfengold)
            if (type.equals(TravelCardType.WITCH)) {
                continue;
            }

            // add 10 or 12 (for raft) cards of each travel card type
            if (type.equals(TravelCardType.RAFT)) {
                for (int i = 0; i < 12; i++) {
                    components.add(new TravelCard(type, GameScreen.getInstance().getWidth()*135/1440, GameScreen.getInstance().getHeight()*2/9));
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    components.add(new TravelCard(type, GameScreen.getInstance().getWidth()*135/1440, GameScreen.getInstance().getHeight()*2/9));
                }
            }
        }

        shuffle();
    }
}
