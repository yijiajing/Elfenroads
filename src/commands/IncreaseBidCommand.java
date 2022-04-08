package commands;

import gamemanager.EGGameManager;
import gamemanager.GameManager;
import networking.GameState;
import windows.AuctionFrame;

import java.util.logging.Logger;

public class IncreaseBidCommand implements GameCommand {

    private final int increaseAmount;
    private final String senderName;

    public IncreaseBidCommand(int amount) {
        increaseAmount = amount;
        senderName = GameManager.getInstance().getThisPlayer().getName();
    }

    @Override
    public void execute() {
        Logger.getGlobal().info("Executing IncreaseBidCommand, " + senderName + " increases the bid by " + increaseAmount);
        AuctionFrame auctionFrame = ((EGGameManager) GameManager.getInstance()).getAuctionFrame();
        auctionFrame.increaseCurrentBid(increaseAmount);
        auctionFrame.setHighestBidPlayer(senderName);
        // reset the passed player count to 1
        GameState.instance().clearPassedPlayerCount();
        GameState.instance().incrementPassedPlayerCount();
    }
}
