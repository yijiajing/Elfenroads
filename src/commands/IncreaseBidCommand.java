package commands;

import gamemanager.EGGameManager;
import gamemanager.GameManager;
import windows.AuctionFrame;

public class IncreaseBidCommand implements GameCommand {

    private final int increaseAmount;

    public IncreaseBidCommand(int amount) {
        this.increaseAmount = amount;
    }

    @Override
    public void execute() {
        ((EGGameManager) GameManager.getInstance()).getAuctionFrame().increaseCurrentBid(this.increaseAmount);
    }
}
