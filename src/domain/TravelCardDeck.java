package domain;

import commands.DrawCardCommand;
import commands.DrawCounterCommand;
import enums.GameVariant;
import enums.TravelCardType;
import gamemanager.GameManager;
import gamescreen.GameScreen;
import networking.GameState;
import utils.GameRuleUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import java.util.logging.*;

public class TravelCardDeck extends Deck <CardUnit> {

    private JLabel deckImage;

    public TravelCardDeck (String sessionID, GameVariant variant) {
        super(sessionID);

        // initialize the deck image
        ImageIcon imageIcon = new ImageIcon("./assets/sprites/grey_deck.png");
        Image image = imageIcon.getImage();
        Image imageResized = image.getScaledInstance(GameScreen.getInstance().getWidth()*130/1440-35, GameScreen.getInstance().getHeight()/5-40, java.awt.Image.SCALE_SMOOTH);
        this.deckImage = new JLabel(new ImageIcon(imageResized));

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

        initializeMouseListener();

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

    /**
     * construct an empty travel card deck
     * private for safety--we only want to use this when we are loading games
     */
    private TravelCardDeck(String sessionID)
    {

        super(sessionID);
        // initialize the deck image
        ImageIcon imageIcon = new ImageIcon("./assets/sprites/grey.png");
        Image image = imageIcon.getImage();
        Image imageResized = image.getScaledInstance(GameScreen.getInstance().getWidth()*130/1440-40, GameScreen.getInstance().getHeight()/5-50, java.awt.Image.SCALE_SMOOTH);
        this.deckImage = new JLabel(new ImageIcon(imageResized));

        // has no cards
    }

    /**
     * A special case constructor to get an empty TravelCardDeck
     * this is necessary for loading games, since we need to initialize the TravelCardDeck without all of the cards in it
     * @return a TravelCardDeck with no components
     */
    public static TravelCardDeck getEmpty(String pSessionID)
    {
        return new TravelCardDeck(pSessionID);
    }

    public JLabel getImage() {
        return this.deckImage;
    }

    private void initializeMouseListener() {
        this.deckImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (GameRuleUtils.isDrawCardsPhase() && GameManager.getInstance().isLocalPlayerTurn()) {
                	CardUnit drawn = GameState.instance().getTravelCardDeck().draw(); // draw a card
                    // tell the other peers to remove the card from the pile
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(new DrawCardCommand(1, null));
                    } catch (IOException err) {
                        System.out.println("Error: there was a problem sending the DrawCardCommand to the other peers.");
                    }
                	// if the card drawn is a gold card, the current player's turn does not end
                    if (drawn instanceof GoldCard) {
                    		//if drawn is a gold card, add to Gold deck and draw another one 
                    		GameState.instance().incrementGoldCardDeckCount();
                    		GameScreen.displayMessage("You drew a gold card! Choose another card or take the Gold Card Deck");
                            GameScreen.getInstance().updateAll(); // update GUI

                	}else {
                        GameManager.getInstance().getThisPlayer().getHand().addUnit(drawn); // add to player's hand
                        GameScreen.getInstance().updateAll(); // update GUI
                        GameManager.getInstance().endTurn();
                	}                  
                }
            }
        });
    }
}
