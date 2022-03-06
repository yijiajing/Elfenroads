package panel;

import domain.Player;
import domain.CounterUnit;
import domain.TransportationCounter;
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
    	
    	//TODO: add a panel to display the counters owned by a player when the mouse hovering on this player's scoreboard.
    	JPanel countersPanelOfOtherPlayer = new JPanel();
    	countersPanelOfOtherPlayer.setVisible(false);
    	countersPanelOfOtherPlayer.setLayout(new FlowLayout());
    	List<TransportationCounter> counters = pPlayer.getHand().getCounters();
    	for(TransportationCounter c: counters) {
    		//display a black square if c is secret.
    		if (c.isSecret()){
    			JLabel display = new JLabel();
    			display.setForeground(Color.BLACK);
    			display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
    			display.setPreferredSize(new Dimension(aScreen.getWidth() * 10 / 1440, aScreen.getHeight() * 10 / 900));//TODO: adjust size
    			
    			countersPanelOfOtherPlayer.add(display);
    		}
    		//else if c is not secret, display it.
    		else {
    			//TODO:adjust the size of counter.
    			countersPanelOfOtherPlayer.add(c.getDisplay());
    		}
    	}
    	
    	//Make the countersPanelOfOtherPlayer visible only when mouse hovering on it.
    	this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	countersPanelOfOtherPlayer.setVisible(true);
            }
            @Override
            public void mouseExited(MouseEvent e) {
            	countersPanelOfOtherPlayer.setVisible(false);
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
