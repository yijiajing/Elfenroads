package commands;

import gamemanager.EGGameManager;
import gamemanager.GameManager;
import networking.GameState;
import windows.AuctionFrame;

public class IncreaseBidCommand implements GameCommand {

    private final int increaseAmount;

    public IncreaseBidCommand(int amount) {
        this.increaseAmount = amount;
    }

    @Override
    public void execute() {
        AuctionFrame auctionFrame = ((EGGameManager) GameManager.getInstance()).getAuctionFrame();
        auctionFrame.increaseCurrentBid(this.increaseAmount);
        GameState.instance().clearPassedPlayerCount();
        auctionFrame.setCounterHasBid(true); // the current counter has at least one bid
        auctionFrame.setCounterBidByLocalPlayer(false); // the highest bid is not from the local player now
    }
}
