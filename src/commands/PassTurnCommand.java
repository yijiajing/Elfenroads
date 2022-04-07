package commands;

import enums.EGRoundPhaseType;
import gamemanager.EGGameManager;
import gamemanager.GameManager;
import networking.GameState;
import windows.AuctionFrame;

public class PassTurnCommand implements GameCommand {

    @Override
    public void execute() {
        GameState gameState = GameState.instance();
        gameState.incrementPassedPlayerCount();
        if (gameState.getCurrentPhase() == EGRoundPhaseType.AUCTION
            && gameState.getPassedPlayerCount() == gameState.getNumOfPlayers() - 1) {
            AuctionFrame auctionFrame = ((EGGameManager) GameManager.getInstance()).getAuctionFrame();

            // someone has won the auction, or all of the players have passed their turn
            if (auctionFrame.isCounterHasBid()) {
                if (auctionFrame.isCounterBidByLocalPlayer()) {
                    //TODO: local player wins the auction, add the counter to hand and notify everyone
                }
            } else {
                //TODO: put counter back to pile

            }
        }
    }
}
