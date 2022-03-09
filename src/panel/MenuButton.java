package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import domain.GameManager;

public class MenuButton extends JButton {
    public MenuButton() {
		setText("Menu");
		this.addActionListener(new ActionListener() {
			@Override
			 public void actionPerformed(ActionEvent e)
			{
				
		    }
		});
	}

    
}
