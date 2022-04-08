package commands;

import domain.Player;
import gamemanager.GameManager;
import networking.GameState;

public class AddGoldCoinsCommand implements GameCommand {

    private final int goldCoins;
    private final String senderName;

    public AddGoldCoinsCommand(int goldCoins) {
        this.goldCoins = goldCoins;
        senderName = GameManager.getInstance().getThisPlayer().getName();
    }

    @Override
    public void execute() {
        Player sender = GameState.instance().getPlayerByName(senderName);
        // we could just use addGoldCoins but this is for clarity
        if (goldCoins < 0) {
            sender.removeGoldCoins(-goldCoins);
        } else {
            sender.addGoldCoins(goldCoins);
        }
    }
}
