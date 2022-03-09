package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import domain.GameManager;
import loginwindow.MP3Player;

public class EndTurnButton extends JButton {
	MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
	public EndTurnButton() {
		setText("EndTurn");
		this.addActionListener(new ActionListener() {
			@Override
			 public void actionPerformed(ActionEvent e)
			{
				if (!GameManager.getInstance().isLocalPlayerTurn())
				{
					GameScreen.displayMessage("You cannot end someone else's turn!");
					return; // do nothing if it's not the local player's turn
				}
				else {
					GameManager.getInstance().endTurn();
					track1.play();
				}
		    }
		});
	}
}
