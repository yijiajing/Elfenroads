package commands;

import enums.CounterUnitType;
import gamemanager.GameManager;
import networking.GameState;

import java.util.logging.Logger;

public class DrawGoldDeckCommand implements GameCommand{

    private final String senderName;
	
    public DrawGoldDeckCommand() {
    	this.senderName = GameManager.getInstance().getThisPlayer().getName();
    }
    
    @Override
	public void execute() {
		Logger.getGlobal().info("Executing DrawGoldDeckCommand: Player" + senderName + "intends to draw the gold card deck");
		GameState gamestate = GameState.instance();
		
		//Update the sender's coins
		gamestate.getPlayerByName(senderName).addGoldCoins(gamestate.getGoldCardDeckCount());
		
		//Initialize the GoldCard Deck
		gamestate.clearGoldCardDeck();

	}

}
