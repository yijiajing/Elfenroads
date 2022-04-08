package panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;

import gamemanager.GameManager;
import gamescreen.GameScreen;
import networking.GameState;
import utils.GameRuleUtils;

import commands.DrawGoldDeckCommand;

import windows.MP3Player;


public class DrawGoldDeckButton extends JButton {

	public DrawGoldDeckButton(GameState pState) {
		setText("Gold Cards: " + Integer.toString(pState.getGoldCardDeckCount()));
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MP3Player track = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
				track.play();
				if (GameRuleUtils.isDrawCardsPhase() && GameManager.getInstance().isLocalPlayerTurn()) {
					if (!(pState.getGoldCardDeckCount() > 0)) {
						GameScreen.displayMessage("There is nothing in the Gold Card Deck.");
					} else {
						// The player intends to take the gold card deck. 
						pState.getCurrentPlayer().addGoldCoins(3 * pState.getGoldCardDeckCount()); // earn 3 coins for each card
						pState.clearGoldCardDeck();
						// Update with other players
						try {
							GameManager.getInstance().getComs().sendGameCommandToAllPlayers(new DrawGoldDeckCommand());
						} catch (IOException err) {
							System.out.println("There was a problem sending the PassTurnCommand to all players.");
							err.printStackTrace();
						}
						//end turn
						GameManager.getInstance().endTurn();
					}
				}
			}
		});
	}
	

	
}
