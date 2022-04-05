package domain;

import commands.DrawCardCommand;
import commands.DrawCounterCommand;
import enums.TravelCardType;
import windows.MP3Player;
import gamemanager.GameManager;
import gamescreen.GameScreen;
import windows.MP3Player;
import windows.MainFrame;
import networking.ActionManager;
import networking.GameState;
import savegames.SerializableTravelCard;
import utils.GameRuleUtils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Logger;

public class TravelCard extends CardUnit {

    private TravelCardType type;
    MP3Player track1 = new MP3Player("./assets/Music/0000171.mp3");
    private boolean owned;

    public TravelCard(TravelCardType type, int resizeWidth, int resizeHeight) {
        super(resizeWidth, resizeHeight, "T0" + (type.ordinal() + 1));
        this.type = type;
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                track1.play();
                if (!isOwned() && GameRuleUtils.isElfengoldDrawCardsPhase()) {
                    GameState.instance().getFaceUpCards().remove(TravelCard.this);
                    GameManager.getInstance().getThisPlayer().getHand().addUnit(TravelCard.this);
                    GameState.instance().addFaceUpCardFromDeck();
                    GameScreen.getInstance().updateAll(); // update GUI
                    TravelCard.this.owned = true;
                    Logger.getGlobal().info("Just added " + (TravelCard.this).getType() +
                            ", current cards in hand: " +
                            GameManager.getInstance().getThisPlayer().getHand().getCards().toString());

                    // tell the other peers to remove the card
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(
                                new DrawCardCommand(1, TravelCard.this.type));
                    } catch (IOException err) {
                        System.out.println("Error: there was a problem sending the DrawCardCommand to the other peers.");
                    }

                    GameManager.getInstance().endTurn();
                } else {
                    ActionManager.getInstance().addSelectedCard(TravelCard.this);
                }
            }
        });
    }

    public TravelCard (SerializableTravelCard loaded)
    {
        super(MainFrame.getInstance().getWidth() * 130 / 1440, MainFrame.getInstance().getHeight() * 2 / 10, "T0" + loaded.getType());
        owned = false;
        type = loaded.getType();

        // init listener
        this.getDisplay().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                track1.play();
                if (!isOwned() && GameRuleUtils.isElfengoldDrawCardsPhase()) {
                    GameState.instance().getFaceUpCards().remove(TravelCard.this);
                    GameManager.getInstance().getThisPlayer().getHand().addUnit(TravelCard.this);
                    GameState.instance().addFaceUpCardFromDeck();
                    GameScreen.getInstance().updateAll(); // update GUI
                    TravelCard.this.owned = true;
                    Logger.getGlobal().info("Just added " + (TravelCard.this).getType() +
                            ", current cards in hand: " +
                            GameManager.getInstance().getThisPlayer().getHand().getCards().toString());

                    // tell the other peers to remove the card
                    try {
                        GameManager.getInstance().getComs().sendGameCommandToAllPlayers(
                                new DrawCardCommand(1, TravelCard.this.type));
                    } catch (IOException err) {
                        System.out.println("Error: there was a problem sending the DrawCardCommand to the other peers.");
                    }

                    GameManager.getInstance().endTurn();
                } else {
                    ActionManager.getInstance().addSelectedCard(TravelCard.this);
                }
            }
        });
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public TravelCardType getType() {
        return type;
    }


    @Override
    public String toString() {
        return "TravelCard{" +
                "type=" + type +
                '}';
    }
}
