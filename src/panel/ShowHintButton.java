package panel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import gamescreen.GameScreen;
import windows.MP3Player;

public class ShowHintButton extends JButton {
    public ShowHintButton() {

		setText("Reshow hint");

		this.addActionListener(new ActionListener() {
			@Override
			 public void actionPerformed(ActionEvent e)
			{
				MP3Player track = new MP3Player("./assets/Music/JLEX5AW-ui-medieval-click-heavy-positive-01.mp3");
				track.play();
				GameScreen.displayMessage(GameScreen.getPrevMessage());
		    }
		});
	}

    
}
