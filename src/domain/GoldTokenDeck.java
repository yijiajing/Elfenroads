package domain;

import enums.GameVariant;
import gamescreen.GameScreen;
import windows.MainFrame;

public class GoldTokenDeck extends Deck<GoldValueToken> {

    public GoldTokenDeck(String sessionID) {
        super(sessionID);
        int[] goldValues = new int[]{7, 6, 5, 5, 5, 5, 4, 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 2, 2};
        for (int value: goldValues) {
            components.add(new GoldValueToken(value, MainFrame.getInstance().getWidth()/50, MainFrame.getInstance().getHeight()/40));
        }
        shuffle();
    }

}
