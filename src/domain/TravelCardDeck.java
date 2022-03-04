package domain;

import enums.TravelCardType;
import panel.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

public class TravelCardDeck {

    private Stack<TravelCard> cards = new Stack<>();
    private int seed = 3; // TODO somehow make the seed different for each game

    public TravelCardDeck() {

        for (TravelCardType type : TravelCardType.values()) {
            // leave out witch cards for now (TODO: incorporate witch variant for elfengold)
            if (type.equals(TravelCardType.WITCH)) {
                continue;
            }

            // add 10 or 12 (for raft) cards of each travel card type
            if (type.equals(TravelCardType.RAFT)) {
                for (int i = 0; i < 12; i++) {
                    cards.add(new TravelCard(type, GameScreen.getInstance().getWidth()*135/1440, GameScreen.getInstance().getHeight()*2/9));
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    cards.add(new TravelCard(type, GameScreen.getInstance().getWidth()*135/1440, GameScreen.getInstance().getHeight()*2/9));
                }
            }
        }

        shuffle();
    }

    public TravelCard draw() {
        return cards.pop();
    }

    public void shuffle() {
        Collections.shuffle(cards, new Random(seed)); // shuffle the deck
    }
}
