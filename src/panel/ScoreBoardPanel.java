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


    
    public ScoreBoardPanel(GameScreen pScreen, Player pPlayer) {
    	aScreen = pScreen;
    	aPlayer = pPlayer;
    	

    	this.setPreferredSize(new Dimension(aScreen.getWidth() * 290 / 1440, aScreen.getHeight() * 40 / 900));
    	this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	
    	final CardLayout ObjCl = new CardLayout();
    	this.setLayout(ObjCl);
    	
    	//card1: the score card
    	JPanel scoreCard = new JPanel();
    	scoreCard.setLayout(new FlowLayout());
    	int playerIndex = GameState.instance().getPlayers().indexOf(pPlayer) + 1;
    	scoreCard.add(new JLabel("Player " + playerIndex));
    	scoreCard.add(new JLabel(Integer.toString(aPlayer.getScore())+" pts"));
    	
    	//Card2: the card showing counters owned by other players
    	JPanel countersCard = new JPanel();
    	countersCard.setLayout(new FlowLayout());
    	List<TransportationCounter> counters = pPlayer.getHand().getCounters();
    	for(TransportationCounter c: counters) {
    		//display a black square if c is secret.
    		if (c.isSecret()){
    			JLabel display = new JLabel();
    			display.setForeground(Color.BLACK);
    			display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
    			display.setPreferredSize(new Dimension(aScreen.getWidth() * 10 / 1440, aScreen.getHeight() * 10 / 900));//TODO: adjust size
    			
    			countersCard.add(display);
    		}
    		//else if c is not secret, display it.
    		else {
    			//TODO:adjust the size of counter.
    			countersCard.add(c.getDisplay());
    		}
    	}
    	
    	//Then add the two cards to the scoreBoardPanel
    	this.add(scoreCard);
    	this.add(countersCard);
    	
//    	this.setLayout(new FlowLayout());
//    	this.add(new JLabel("Player " + GameState.instance().getPlayers().indexOf(pPlayer)));
//    	this.add(new JLabel(Integer.toString(aPlayer.getScore())+" pts"));
//    	
//    	JPanel countersPanelOfOtherPlayer = new JPanel();
//    	countersPanelOfOtherPlayer.setBounds();
//    	countersPanelOfOtherPlayer.setVisible(false);
//    	countersPanelOfOtherPlayer.setLayout(new FlowLayout());
//    	List<TransportationCounter> counters = pPlayer.getHand().getCounters();
//    	for(TransportationCounter c: counters) {
//    		//display a black square if c is secret.
//    		if (c.isSecret()){
//    			JLabel display = new JLabel();
//    			display.setForeground(Color.BLACK);
//    			display.setBorder(BorderFactory.createLineBorder(Color.BLACK, 10));
//    			display.setPreferredSize(new Dimension(aScreen.getWidth() * 10 / 1440, aScreen.getHeight() * 10 / 900));//TODO: adjust size
//    			
//    			countersPanelOfOtherPlayer.add(display);
//    		}
//    		//else if c is not secret, display it.
//    		else {
//    			//TODO:adjust the size of counter.
//    			countersPanelOfOtherPlayer.add(c.getDisplay());
//    		}
//    	}
    	
    	//Switch to the counters card when hovering on this panel
    	this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
            	ObjCl.last(ScoreBoardPanel.this);
            }
            @Override
            public void mouseExited(MouseEvent e) {
            	ObjCl.first(ScoreBoardPanel.this);
            }
        });
    	
    	aScreen.addObserverPanel(this);
    }

	@Override
	public void updateView() {
		this.validate();
		this.repaint();
		//this.revalidate();
		
	}
	
	
	
}
