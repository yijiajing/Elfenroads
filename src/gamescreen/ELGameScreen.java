package gamescreen;

import java.awt.*;

import java.util.List;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.Border;

import domain.*;
import enums.GameVariant;
import gamemanager.GameManager;
import networking.GameState;
import panel.ScoreBoardPanel;

/**
 * A Singleton class that represents the main screen in which the board game is played
 */
public class ELGameScreen extends GameScreen
{
	protected final JPanel[] panelForFaceUpTransportationCounters = new JPanel[5];
	protected final JPanel panelForDeckOfTransportationCounters = new JPanel();
	protected final JPanel panelForObstacle = new JPanel();
	protected final JPanel backgroundPanel_ForFaceUpTransportationCounters = new JPanel();
	protected final JPanel[] panelsForPlayerCards = new JPanel[8];

	ELGameScreen(JFrame frame, GameVariant variant) {
		super(frame, variant);
	}

	@Override
	public void updateAll() {
		updateTransportationCountersAndObstacle(); // updates the player's counter area
		updateCards(); // update's the player's cards
		updateFaceUpTransportationCounters(); // updates the face-up transportation counters
		notifyObservers(); // updates elf boots and town pieces
		updateLeaderboard();
	}

	@Override
	public void initialization()
	{
		Logger.getGlobal().info("Initializing...");
		initializeMapImage();
		initializeRoundCardImage(1);
		initializeTransportationCountersAndObstacle();
		initializeBackgroundPanels();
		initializeCardPanels();
		initializeInformationCardImage();
		initializeFaceUpTransportationCounters();
		initializeDeckOfTransportationCounters();
		initializeLeaderboard();
		initializeButtons();
		initializeMenu();
		initializeChat();

		updateAll();
	}


	public void initializeBackgroundPanels() {
		// Set Bounds for background Player's Transportation Counter zone
		backgroundPanel_ForTransportationCounters.setBounds(0, height * 624 / 900, width *  1150 / 1440, height * 69 / 900);
		backgroundPanel_ForTransportationCounters.setBackground(Color.DARK_GRAY);

		// Set Bounds for background Image zone
		backgroundPanel_ForMap.setBounds(0, 0, width * 1150 / 1440, height * 625 / 900);
		backgroundPanel_ForMap.setBackground(Color.BLUE);

		// Set Bounds for background Round zone
		backgroundPanel_ForRound.setBounds(width * 990 / 1440, height * 15 / 900, width * 105 / 1440, height * 195 / 900);
		backgroundPanel_ForRound.setOpaque(false);

		// Set Bounds for background Cards zone
		backgroundPanel_ForCards.setBounds(0, height * 690 / 900, width * 1150 / 1440, height * 3 / 9);
		backgroundPanel_ForCards.setBackground(Color.WHITE);

		// Set Bounds for background Information zone
		backgroundPanel_ForInformationCard.setBounds(width * 1150 / 1440, height * 565 / 900, width * 290 / 1440, height * 330 / 900);
		backgroundPanel_ForInformationCard.setBackground(Color.DARK_GRAY);

		// Set Bounds for background Face Up Transportation Counter zone
		backgroundPanel_ForFaceUpTransportationCounters.setBounds(width * 1150 / 1440, height * 270 / 900, width * 290 / 1440, height * 300 / 900);
		backgroundPanel_ForFaceUpTransportationCounters.setBackground(Color.DARK_GRAY);

		backgroundPanel_ForLeaderboard.setBounds(width * 1150 / 1440, 0, width * 290 / 1440, height * 274 / 900);
		backgroundPanel_ForLeaderboard.setBackground(Color.DARK_GRAY);
	}
	
	public void initializeDeckOfTransportationCounters()
	{
		Logger.getGlobal().info("Initializing deck of transportation counters");
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		
		JPanel panel = panelForDeckOfTransportationCounters;
		panel.setBounds(width*1205/1440, height*285/900, width*80/1440, height*75/900);
		panel.setOpaque(false);
		//panel.setBorder(whiteLine);
		boardGame_Layers.add(panel, 0);
	}
	
	public void initializeFaceUpTransportationCounters()
	{
		Logger.getGlobal().info("Initializing face up transportation counters");
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		JPanel panel2 = new JPanel();
		panel2.setBounds(width*1315/1440, height*290/900, width*70/1440, height*65/900);
		panel2.setOpaque(false);
		//panel2.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[0] = panel2;
		boardGame_Layers.add(panel2, 0);
		
		JPanel panel3 = new JPanel();
		panel3.setBounds(width*1210/1440, height*385/900, width*70/1440, height*65/900);
		panel3.setOpaque(false);
		//panel3.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[1] = panel3;
		boardGame_Layers.add(panel3, 0);
		
		JPanel panel4 = new JPanel();
		panel4.setBounds(width*1315/1440, height*385/900, width*70/1440, height*65/900);
		panel4.setOpaque(false);
		//panel4.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[2] = panel4;
		boardGame_Layers.add(panel4, 0);
		
		JPanel panel5 = new JPanel();
		panel5.setBounds(width*1210/1440, height*480/900, width*70/1440, height*65/900);
		panel5.setOpaque(false);
		//panel5.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[3] = panel5;
		boardGame_Layers.add(panel5, 0);
		
		JPanel panel6 = new JPanel();
		panel6.setBounds(width*1315/1440, height*480/900, width*70/1440, height*65/900);
		panel6.setOpaque(false);
		//panel6.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[4] = panel6;
		boardGame_Layers.add(panel6, 0);
	}
	
	public void initializeTransportationCountersAndObstacle()
	{
		int xCoordinate = width*10/1440;
		
		// Transportation Counters
		for (int i = 0; i < 5; i++)
		{
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height*625/900, width*70/1440, height*65/900);
			panelForPlayerTransportationCounters[i] = panel;
			xCoordinate += width*100/1440;
			boardGame_Layers.add(panel, 0);
		}
		
		// Obstacle
		panelForObstacle.setOpaque(true);
		panelForObstacle.setBackground(Color.RED);
		panelForObstacle.setBounds(width*950/1440, height*624/900, width*80/1440, height*69/900);
		boardGame_Layers.add(panelForObstacle,-1);
	}

	public void initializeCardPanels() {
		int xCoordinate = width * 2 / 1440;

		for (int i = 0; i < 8; i++) {
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			//panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height * 690 / 900, width * 144 / 1440, height * 3 / 9);
			panelsForPlayerCards[i] = panel;
			xCoordinate += width * 144 / 1440;
			boardGame_Layers.add(panel, 0);
		}
	}

	@Override
	public void initializeLeaderboard() {
		List<Player> aPlayers = GameState.instance().getPlayers();

		backgroundPanel_ForLeaderboard.setLayout(new BoxLayout(backgroundPanel_ForLeaderboard, BoxLayout.Y_AXIS));
		backgroundPanel_ForLeaderboard.setAlignmentX(CENTER_ALIGNMENT);
		for (Player player : aPlayers) {
			backgroundPanel_ForLeaderboard.add(new ScoreBoardPanel(this, player, false));
			backgroundPanel_ForLeaderboard.add(Box.createRigidArea(new Dimension(0, 5)));
		}
	}

	@Override
	public void addImages()
	{
		backgroundPanel_ForMap.add(mapImage_BottomLayer);
		backgroundPanel_ForInformationCard.add(informationCardImage_TopLayer);
		panelForDeckOfTransportationCounters.add(GameState.instance().getCounterPile().getImage());

		drawTownPieces();
		notifyObservers();
	}

	@Override
	public void addPanelToScreen()
	{
		boardGame_Layers.add(backgroundPanel_ForRound, 0);
		boardGame_Layers.add(panelForDeckOfTransportationCounters,0);
		boardGame_Layers.add(backgroundPanel_ForTransportationCounters, -1);
		boardGame_Layers.add(backgroundPanel_ForMap, -1);
		boardGame_Layers.add(backgroundPanel_ForCards, -1);
		boardGame_Layers.add(backgroundPanel_ForInformationCard, -1);
		boardGame_Layers.add(backgroundPanel_ForFaceUpTransportationCounters, -1);
		boardGame_Layers.add(backgroundPanel_ForLeaderboard,-1);

		for (Town town: gameMap.getTownList()) {
			boardGame_Layers.add(town.getPanel(), 0);
			boardGame_Layers.add(town.getElfBootPanel(), 0);
		}
	}
	
	public void updateFaceUpTransportationCounters()
	{
		ArrayList<TransportationCounter> faceUpCounters = GameState.instance().getFaceUpCounters();

		// clear the previous counters from the screen
		for (JPanel panel : panelForFaceUpTransportationCounters) {
			if (panel != null) {
				panel.removeAll();
        		panel.repaint();
        		panel.revalidate();
			}
		}

		for (int i = 0; i < 5; i++) {
			JPanel panel = panelForFaceUpTransportationCounters[i];
			TransportationCounter counter = faceUpCounters.get(i);
			panel.add(counter.getDisplay());
			panel.repaint();
			panel.revalidate();
		}
	}
	
	public void updateTransportationCountersAndObstacle()
	{
		// remove a counter if it was already there
		for (JPanel panel : panelForPlayerTransportationCounters) {
			if (panel != null) {
				panel.removeAll();
				panel.repaint();
				panel.revalidate();
			}
		}

		List<CounterUnit> counters = GameManager.getInstance().getThisPlayer().getHand().getCounters();

		// draw the counters to the screen
		for (int c = 0; c < counters.size(); c++) {
			JPanel panel = panelForPlayerTransportationCounters[c];
			CounterUnit counter = counters.get(c);
			panel.add(counter.getDisplay());
			panel.repaint();
			panel.revalidate();
		}
		
		// Obstacle
		Obstacle o = GameManager.getInstance().getThisPlayer().getHand().getObstacle();

		if (o != null) {
			panelForObstacle.add(o.getDisplay());
		} else {
			panelForObstacle.removeAll();
		}

		panelForObstacle.repaint();
		panelForObstacle.revalidate();
	}

	public void updateCards() {
		// clear the previous cards from the screen
		for (JPanel panel : panelsForPlayerCards) {
			if (panel != null) {
				panel.removeAll();
				panel.repaint();
				panel.revalidate();
			}
		}

		List<CardUnit> myCards = GameManager.getInstance().getThisPlayer().getHand().getCards();

		// draw the cards to the screen
		for (int p = 0; p < myCards.size(); p++) {
			JPanel panel = panelsForPlayerCards[p];
			if (panel != null) {
				CardUnit card = myCards.get(p);
				panel.add(card.getDisplay());
				panel.repaint();
				panel.revalidate();
			}
		}
	}
}
