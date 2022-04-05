package domain;

import enums.GameVariant;
import enums.TravelCardType;
import gamescreen.GameScreen;
import networking.GameState;
import utils.GameRuleUtils;

import javax.swing.*;
import java.awt.*;

public class TravelCardDeck extends Deck <CardUnit> {

    private GameVariant variant;
    private JLabel deckImage;

    public TravelCardDeck (String sessionID, GameVariant variant) {
        super(sessionID);

        // initialize the deck image
        ImageIcon imageIcon = new ImageIcon("./assets/sprites/grey.png");
        Image image = imageIcon.getImage();
        Image imageResized = image.getScaledInstance(GameScreen.getInstance().getWidth()*130/1440-40, GameScreen.getInstance().getHeight()/5-50, java.awt.Image.SCALE_SMOOTH);
        this.deckImage = new JLabel(new ImageIcon(imageResized));

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
            //add 6 witch cards only if the gamevariant is elfengold_witch
            if (type.equals(TravelCardType.WITCH)) {
                if (variant == GameVariant.ELFENGOLD_WITCH) {
                	for(int i = 0; i < 6; i++) {
                		components.add(new TravelCard(TravelCardType.WITCH,GameScreen.getInstance().getWidth()*130/1440, GameScreen.getInstance().getHeight()/5));
                	}
                }else {
                	continue;
                }
            }

            if (type.equals(TravelCardType.RAFT)) {
                for (int i = 0; i < numRafts; i++) {
                    components.add(new TravelCard(type, GameScreen.getInstance().getWidth()*130/1440, GameScreen.getInstance().getHeight()/5));
                }
            } else {
                for (int i = 0; i < numOtherTypes; i++) {
                    components.add(new TravelCard(type, GameScreen.getInstance().getWidth()*130/1440, GameScreen.getInstance().getHeight()/5));
                }
            }
        }

        shuffle();
    }

    public void addGoldCards() {
        // Add gold cards for Elfengold
        if (GameRuleUtils.isElfengoldVariant(variant)) {
            for (int i = 0; i < 7; i++) {
                components.add(new GoldCard(GameScreen.getInstance().getWidth()*130/1440, GameScreen.getInstance().getHeight()/5));
            }
        }
        shuffle();
    }

    public JLabel getImage() {
        return this.deckImage;
    }
}
