import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.util.Collections;
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.Border;

import org.minueto.MinuetoTool;

public class GameScreen extends JPanel
{
	private JFrame mainFrame;
	private int width;
	private int height;
	
	private JLayeredPane boardGame_Layers = new JLayeredPane();
	
	private JLabel roundImage_TopLayer;
	private JLabel mapImage_BottomLayer;
	private JLabel informationCardImage_TopLayer;
	private JLabel deckOfTransportationCountersImage_TopLayer;
	
	private JPanel backgroundPanel_ForMap = new JPanel();
	private JPanel backgroundPanel_ForRound = new JPanel();
	private JPanel backgroundPanel_ForObstacle = new JPanel();
	private JPanel backgroundPanel_ForCards = new JPanel();
	private JPanel backgroundPanel_ForTransportationCounters = new JPanel();
	private JPanel backgroundPanel_ForInformationCard = new JPanel();
	private JPanel backgroundPanel_ForDeckOfTransportationCounters = new JPanel();
	
	private JPanel[] panelForPlayerTransportationCounters = new JPanel[5];
	private JPanel[] panelForPlayerCards = new JPanel[8];
	private JPanel[] panelForDeckOfTransportationCounters = new JPanel[6];
	private JPanel panelForObstacle = new JPanel();
	
	GameScreen (JFrame frame)
	{
		// Get dimensions of the full screen
		mainFrame = frame;
		width = mainFrame.getWidth();
		height = mainFrame.getHeight();
		
		// Set Bounds for entire Board Game screen and do the initialization of the structure for the UI
		boardGame_Layers.setBounds(0,0,width,height);	
		initializeMapImage();
		initializeRoundCardImage(1);
		initializeTransportationCountersAndObstacle();
		initializeBackgroundPanels();
		initializeCards();
		initializeInformationCardImage();
		initializeDeckOfTransportationCounters();
		initializeDeckOfTransportationCountersImage();

		// Add the images to their corresponding JPanel
		backgroundPanel_ForMap.add(mapImage_BottomLayer);		
		backgroundPanel_ForRound.add(roundImage_TopLayer);
		backgroundPanel_ForInformationCard.add(informationCardImage_TopLayer);
		panelForDeckOfTransportationCounters[0].add(deckOfTransportationCountersImage_TopLayer);
		
		// Add the JPanels to the main JLayeredPane with their corresponding layer
		addPanelToScreen();
		
		// Add the entire structure of the UI to the main screen
		mainFrame.add(boardGame_Layers);
	}
	
	public void addPanelToScreen()
	{
		boardGame_Layers.add(backgroundPanel_ForRound, 0);
		addDeckOfTransportationCounters();
		addTransportationCountersAndObstacle();
		addCards();
		boardGame_Layers.add(backgroundPanel_ForTransportationCounters, -1);
		boardGame_Layers.add(backgroundPanel_ForMap, -1); 
		boardGame_Layers.add(backgroundPanel_ForObstacle,-1);
		boardGame_Layers.add(backgroundPanel_ForCards, -1);
		boardGame_Layers.add(backgroundPanel_ForInformationCard, -1);
		boardGame_Layers.add(backgroundPanel_ForDeckOfTransportationCounters, -1);
	}
	
	public void addDeckOfTransportationCounters()
	{	
		for (JPanel panel : panelForDeckOfTransportationCounters)
		{
			boardGame_Layers.add(panel, 0);
		}
	}
	
	public void addCards()
	{	
		for (JPanel panel : panelForPlayerCards)
		{
			boardGame_Layers.add(panel, 0);
		}
	}
	
	public void addTransportationCountersAndObstacle()
	{
		// Transportation Counters
		for (JPanel panel : panelForPlayerTransportationCounters)
		{
			panel.setOpaque(false);
			boardGame_Layers.add(panel, 0);
		}
		
		// Obstacle
		boardGame_Layers.add(panelForObstacle,0);
	}
	
	public void initializeBackgroundPanels()
	{
		// Set Bounds for background Player Transportation Counter zone
		backgroundPanel_ForTransportationCounters.setBounds(width*0/1440, height*565/900, width*983/1440, height*70/900);
		backgroundPanel_ForTransportationCounters.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Obstacle zone
		backgroundPanel_ForObstacle.setBackground(Color.RED);
		backgroundPanel_ForObstacle.setBounds(width*984/1440, height*565/900, width*80/1440, height*70/900);
		
		// Set Bounds for background Image zone
		backgroundPanel_ForMap.setBounds(width*0/1440, height*0/900, width*1064/1440, height*564/900);
		backgroundPanel_ForMap.setBackground(Color.BLUE);
		
		// Set Bounds for background Round zone
		backgroundPanel_ForRound.setBounds(width*932/1440, height*34/900, width*86/1440, height*130/900);
		backgroundPanel_ForRound.setOpaque(false);
		
		// Set Bounds for background Cards zone
		backgroundPanel_ForCards.setBounds(width*0/1440, height*566/900, width*1064/1440, height*333/900);
		backgroundPanel_ForCards.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Information zone
		backgroundPanel_ForInformationCard.setBounds(width*1065/1440, height*565/900, width*375/1440, height*335/900);
		backgroundPanel_ForInformationCard.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Face Up Transportation Counter zone
		backgroundPanel_ForDeckOfTransportationCounters.setBounds(width*1065/1440, height*275/900, width*375/1440, height*289/900);
		backgroundPanel_ForDeckOfTransportationCounters.setBackground(Color.DARK_GRAY);
	}
	
	public void initializeDeckOfTransportationCounters()
	{
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		
		JPanel panel1 = new JPanel();
		panel1.setBounds(width*1170/1440, height*290/900, width*70/1440, height*60/900);
		panel1.setOpaque(false);
		panel1.setBorder(whiteLine);
		panelForDeckOfTransportationCounters[0] = panel1;
		
		JPanel panel2 = new JPanel();
		panel2.setBounds(width*1280/1440, height*290/900, width*70/1440, height*60/900);
		panel2.setOpaque(false);
		panel2.setBorder(whiteLine);
		panelForDeckOfTransportationCounters[1] = panel2;
		
		JPanel panel3 = new JPanel();
		panel3.setBounds(width*1170/1440, height*390/900, width*70/1440, height*60/900);
		panel3.setOpaque(false);
		panel3.setBorder(whiteLine);
		panelForDeckOfTransportationCounters[2] = panel3;
		
		JPanel panel4 = new JPanel();
		panel4.setBounds(width*1280/1440, height*390/900, width*70/1440, height*60/900);
		panel4.setOpaque(false);
		panel4.setBorder(whiteLine);
		panelForDeckOfTransportationCounters[3] = panel4;
		
		JPanel panel5 = new JPanel();
		panel5.setBounds(width*1170/1440, height*490/900, width*70/1440, height*60/900);
		panel5.setOpaque(false);
		panel5.setBorder(whiteLine);
		panelForDeckOfTransportationCounters[4] = panel5;
		
		JPanel panel6 = new JPanel();
		panel6.setBounds(width*1280/1440, height*490/900, width*70/1440, height*60/900);
		panel6.setOpaque(false);
		panel6.setBorder(whiteLine);
		panelForDeckOfTransportationCounters[5] = panel6;
	}
	
	public void initializeCards()
	{
		int xCoordinate = width*2/1440;
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		for (int i = 0; i < 8; i++)
		{
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height*637/900, width*130/1440, height*260/900);
			panelForPlayerCards[i] = panel;
			xCoordinate += width*133/1440;
		}
	}
	
	public void initializeTransportationCountersAndObstacle()
	{
		int xCoordinate = width*10/1440;
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		
		// Transportation Counters
		for (int i = 0; i < 5; i++)
		{
			JPanel panel= new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height*570/900, width*70/1440, height*60/900);
			panelForPlayerTransportationCounters[i] = panel;
			xCoordinate += width*200/1440;
		}
		
		// Obstacle
		panelForObstacle.setOpaque(false);
		panelForObstacle.setBorder(whiteLine);
		panelForObstacle.setBounds(width*989/1440, height*570/900, width*70/1440, height*60/900);
	}
	
	public void initializeMapImage()
	{
		ImageIcon mapImage = new ImageIcon("/Users/charlescouture/eclipse-workspace/COMP361/assets/sprites/map.png");
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(width*1064/1440, height*564/900,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		mapImage_BottomLayer = new JLabel(mapImage);
	}
	
	public void initializeRoundCardImage(int round)
	{
		String image = "R" + String.valueOf(round) + ".png";
		ImageIcon roundImage = new ImageIcon("/Users/charlescouture/eclipse-workspace/COMP361/assets/sprites/R1.png");
		Image Round = roundImage.getImage();
		Image RoundResized = Round.getScaledInstance(width*90/1440, height*130/900,  java.awt.Image.SCALE_SMOOTH);
		roundImage = new ImageIcon(RoundResized);
		roundImage_TopLayer = new JLabel(roundImage);
	}
	
	public void initializeInformationCardImage()
	{
		ImageIcon gridImage = new ImageIcon("/Users/charlescouture/eclipse-workspace/COMP361/assets/sprites/grid.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*360/1440, height*325/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		informationCardImage_TopLayer = new JLabel(gridImage);
	}
	
	public void initializeDeckOfTransportationCountersImage()
	{
		ImageIcon gridImage = new ImageIcon("/Users/charlescouture/eclipse-workspace/COMP361/assets/sprites/M08.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*67/1440, height*52/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		deckOfTransportationCountersImage_TopLayer = new JLabel(gridImage);
	}

	public void initializeTransportationCounters()
	{
		// we have 8 of each counter in the game
		Stack<TransportationCounter> toAddToDeck = new Stack<TransportationCounter>();

		for (int i = 1; i <= 8; i++)
		{
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.DRAGON));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.GIANTPIG));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.UNICORN));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.TROLLWAGON));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.ELFCYCLE));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.MAGICCLOUD));
		}

		Deck counters = new Deck(toAddToDeck);

		// now we have intialized the counters and their images

		JButton drawButton = new DrawButton(counters);
		drawButton.addActionListener(new DrawListener(counters, panelForPlayerTransportationCounters));
		mainFrame.add(drawButton);
		// check if the panel is full already

	}
	
	public static void main(String[] args) 
	{
		JFrame game_screen = new JFrame("GameScreen");
		
		game_screen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
		game_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game_screen.add(new GameScreen(game_screen));
		game_screen.setVisible(true);
	}

}
