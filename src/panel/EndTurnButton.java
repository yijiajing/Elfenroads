package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import domain.GameManager;

public class EndTurnButton extends JButton {
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
				else {GameManager.getInstance().endTurn();}
		    }
		});
	}
}
