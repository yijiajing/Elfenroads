package panel;

import domain.Player;
import networking.ActionManager;
import networking.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

//This panel represent the score of one single player. 

public class ScoreBoardPanel extends JPanel implements ObserverPanel{

    private GameScreen aScreen;
    private Player aPlayer;
//    int x;
//    int y;

    
    public ScoreBoardPanel(GameScreen pScreen, Player pPlayer) {
    	aScreen = pScreen;
    	aPlayer = pPlayer;
//    	x = px;
//    	y = py;
    	
    	this.setPreferredSize(new Dimension(aScreen.getWidth() * 290 / 1440, aScreen.getHeight() * 40 / 900));
//    	this.setBounds(this.x, this.y, aScreen.getWidth() * 290 / 1440, aScreen.getHeight() * 40 / 900);
    	this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	
    	this.setLayout(new FlowLayout());
    	this.add(new JLabel("Player " + GameState.instance().getPlayers().indexOf(pPlayer)));
    	this.add(new JLabel(Integer.toString(aPlayer.getScore())+" pts"));
    	
    	
    	this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                //TODO: display the counter info of the player when hover on this panel.
            }
        });
    	
    	aScreen.addObserverPanel(this);
    }

	@Override
	public void updateView() {
		this.repaint();
		this.revalidate();
		
	}
}
