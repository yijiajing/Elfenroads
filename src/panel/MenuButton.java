package panel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gamescreen.GameScreen;
import loginwindow.MP3Player;

public class MenuButton extends JButton {
    MP3Player track1 = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
    public MenuButton() {
		setText("Reshow hint");
		this.addActionListener(new ActionListener() {
			@Override
			 public void actionPerformed(ActionEvent e)
			{
				track1.play();
				GameScreen.displayMessage(GameScreen.getPrevMessage());
		    }
		});
	}

    
}
