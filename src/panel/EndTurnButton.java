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
			 public void actionPerformed(ActionEvent e) {
		       GameManager.getInstance().endTurn();
		    }
		});
	}
}
