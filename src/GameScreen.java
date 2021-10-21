import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
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
	
	private JPanel backgroundPanel_ForMap = new JPanel();
	private JPanel backgroundPanel_ForRound = new JPanel();
	private JPanel backgroundPanel_ForObstacle = new JPanel();
	private JPanel backgroundPanel_ForCards = new JPanel();
	private JPanel backgroundPanel_ForTransportation_Counters = new JPanel();
	
	private JPanel[] panelForPlayerTransportationCounters = new JPanel[5];
	private JPanel[] panelForPlayerCards = new JPanel[8];
	private JPanel panelForObstacle = new JPanel();
	
	GameScreen (JFrame frame)
	{
		// Get dimensions of the full screen
		mainFrame = frame;
		width = mainFrame.getWidth();
		height = mainFrame.getHeight();
		
		// Set Bounds for entire Board Game screen do the initialization of the structure for the UI
		boardGame_Layers.setBounds(0,0,width,height);	
		initializeMapImage();
		initializeRoundCardImage(1);
		initializeTransportationCountersAndObstacle();
		initializeBackgroundPanels();
		initializeCards();

		// Add the images to their corresponding JPanel
		backgroundPanel_ForMap.add(mapImage_BottomLayer);		
		backgroundPanel_ForRound.add(roundImage_TopLayer);
		
		// Add the JPanels to the main JLayeredPane with their corresponding layer
		addPanelToScreen();
		
		// Add the entire structure of the UI to the main screen
		mainFrame.add(boardGame_Layers);
	}
	
	public void addPanelToScreen()
	{
		boardGame_Layers.add(backgroundPanel_ForRound, 0);
		addTransportationCountersAndObstacle();
		addCards();
		boardGame_Layers.add(backgroundPanel_ForTransportation_Counters, -1);
		boardGame_Layers.add(backgroundPanel_ForMap, -1); 
		boardGame_Layers.add(backgroundPanel_ForObstacle,-1);
		boardGame_Layers.add(backgroundPanel_ForCards, -1);
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
		// Set Bounds for background Transportation Counter zone
		backgroundPanel_ForTransportation_Counters.setBounds(0, 565, 983, 70);
		backgroundPanel_ForTransportation_Counters.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Obstacle zone
		backgroundPanel_ForObstacle.setBackground(Color.RED);
		backgroundPanel_ForObstacle.setBounds(984, 565, 80, 70);
		
		// Set Bounds for background Image zone
		backgroundPanel_ForMap.setBounds(0, 0, 1064, 564);
		backgroundPanel_ForMap.setBackground(Color.BLUE);
		
		// Set Bounds for background Round zone
		backgroundPanel_ForRound.setBounds(932, 34, 86, 130);
		backgroundPanel_ForRound.setOpaque(false);
		
		// Set Bounds for background Cards
		backgroundPanel_ForCards.setBounds(0, 566, 1064, 333);
		backgroundPanel_ForCards.setBackground(Color.DARK_GRAY);
	}
	
	public void initializeCards()
	{
		int xCoordinate = 2;
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		for (int i = 0; i < 8; i++)
		{
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, 637, 130, 260);
			panelForPlayerCards[i] = panel;
			xCoordinate += 133;
		}
	}
	
	public void initializeTransportationCountersAndObstacle()
	{
		int xCoordinate = 10;
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		
		// Transportation Counters
		for (int i = 0; i < 5; i++)
		{
			JPanel panel= new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, 570, 70, 60);
			panelForPlayerTransportationCounters[i] = panel;
			xCoordinate += 200;
		}
		
		// Obstacle
		panelForObstacle.setOpaque(false);
		panelForObstacle.setBorder(whiteLine);
		panelForObstacle.setBounds(989, 570, 70, 60);
	}
	
	public void initializeMapImage()
	{
		ImageIcon mapImage = new ImageIcon(getClass().getResource("map.png"));
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(1064, 564,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		mapImage_BottomLayer = new JLabel(mapImage);
	}
	
	public void initializeRoundCardImage(int round)
	{
		String image = "R" + String.valueOf(round) + ".png";
		ImageIcon roundImage = new ImageIcon(getClass().getResource(image));
		Image Round = roundImage.getImage();
		Image RoundResized = Round.getScaledInstance(90, 130,  java.awt.Image.SCALE_SMOOTH);
		roundImage = new ImageIcon(RoundResized);
		roundImage_TopLayer = new JLabel(roundImage);
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
