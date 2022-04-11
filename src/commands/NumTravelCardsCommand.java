package commands;

import gamemanager.GameManager;
import networking.GameState;

import java.util.logging.Logger;

public class NumTravelCardsCommand implements GameCommand {

    private final int numTravelCards;
    private final String senderName;

    public NumTravelCardsCommand() {
        numTravelCards = GameManager.getInstance().getThisPlayer().getHand().getNumTravelCards();
        senderName = GameManager.getInstance().getThisPlayer().getName();
    }

    @Override
    public void execute() {
        Logger.getGlobal().info(senderName + " has " + numTravelCards + " travel cards");
        GameManager gameManager = GameManager.getInstance();
        gameManager.incrementCardNotifsReceived();

        // update number of travel cards of the sender
        GameState.instance().getPlayerByName(senderName).getHand().setNumTravelCards(numTravelCards);
    }
}
