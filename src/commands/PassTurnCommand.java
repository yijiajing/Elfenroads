package commands;

import domain.CounterUnit;
import enums.EGRoundPhaseType;
import gamemanager.EGGameManager;
import gamemanager.GameManager;
import networking.GameState;
import windows.AuctionFrame;

import java.util.logging.Logger;

public class PassTurnCommand implements GameCommand {

    @Override
    public void execute() {
        GameState gameState = GameState.instance();
        gameState.incrementPassedPlayerCount();
        Logger.getGlobal().info("Executing PassTurnCommand, updated passed player ct: " + gameState.getPassedPlayerCount());

        if (gameState.getCurrentPhase() == EGRoundPhaseType.AUCTION) {
            AuctionFrame auctionFrame = ((EGGameManager) GameManager.getInstance()).getAuctionFrame();
            if (auctionFrame.getNumCountersInAuction() == 0) return;

            if (gameState.getPassedPlayerCount() == gameState.getNumOfPlayers()) {
                CounterUnit currentCounter = auctionFrame.removeFirstCounter();
                // There is no bid
                if (auctionFrame.getHighestBidPlayer() == null) {
                    Logger.getGlobal().info("No player bids on the current item, returning it back to the pile");
                    // Put the counter back to pile
                    GameState.instance().getCounterPile().addDrawable(currentCounter);
                }

                // A player wins the auction
                else {
                    Logger.getGlobal().info(auctionFrame.getHighestBidPlayer() + " wins the auction with "
                            + auctionFrame.getCurrentBid() + " gold coins");
                    // Add the counter to the hand of the winning player
                    auctionFrame.getHighestBidPlayer().getHand().addUnit(currentCounter);
                    // Remove gold coins from the winning player
                    auctionFrame.getHighestBidPlayer().removeGoldCoins(auctionFrame.getCurrentBid());
                }
                // Clear bid status
                auctionFrame.resetBidStatus();
            }
        }
    }
}
