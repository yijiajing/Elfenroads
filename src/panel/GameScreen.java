package panel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.List;

import java.util.ArrayList;

import java.util.Stack;

import javax.swing.*;
import javax.swing.border.Border;

import enums.Colour;
import enums.CounterType;
import domain.*;
import networking.GameState;
import org.minueto.MinuetoTool;

/**
 * A Singleton class that represents the main screen in which the board game is played
 */
public class GameScreen extends JPanel implements Serializable
{
	private static GameScreen INSTANCE; // Singleton instance 

	private JFrame mainframe;
	private Integer width;
	private Integer height;
	
	private JLayeredPane boardGame_Layers;

	private Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
	
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
	private JPanel backgroundPanel_ForLeaderboard = new JPanel();

	private JPanel[] panelForPlayerTransportationCounters = new JPanel[5];
	private JPanel[] panelForPlayerCards = new JPanel[8];
	private JPanel[] panelForFaceUpTransportationCounters = new JPanel[5];
	private JPanel panelForDeckOfTransportationCounters = new JPanel();
	private JPanel panelForObstacle = new JPanel();

	private ArrayList<ObserverPanel> observerPanels = new ArrayList<>();

	private GameMap gameMap;

	private JTable leaderboard = new JTable();

	private boolean myTurn;

	// TODO: change this on the other computer
	private String otherPlayerIP = "192.168.2.253"; // Nick's IP address

	private GameScreen (JFrame frame)
	{
		// layout is necessary for JLayeredPane to be added to the JPanel
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Get dimensions of the full screen
		mainframe = frame;
		width = mainframe.getWidth();
		height = mainframe.getHeight();

		// Set Bounds for entire Board Game screen and do the initialization of the structure for the UI
		boardGame_Layers = new JLayeredPane();
		boardGame_Layers.setBounds(0,0,width,height);

		// initialize town and road panels
		gameMap = GameMap.init(this);
	}

	/**
	 * @return the Singleton instance of the GameScreen 
	 */
	public static GameScreen getInstance() {
		return INSTANCE; 
	}

	public static GameScreen init(JFrame frame) {
		if (INSTANCE == null) {
			INSTANCE = new GameScreen(frame);
		}
		return INSTANCE;
	}

	/**
	 * Draws all of the UI components to the screen
	 */
	public void draw() {
		initialization();

		// Add the images to their corresponding JPanel
		addImages();
		initializeListenerToDraw();

		// Add the JPanels to the main JLayeredPane with their corresponding layer
		addPanelToScreen();

		// Add the entire structure of the UI to the panel
		this.add(boardGame_Layers);
	}

	public void update(JPanel panel)
	{
		panel.repaint();
		panel.revalidate();
	}


	public void initialization()
	{
		initializeMapImage();
		initializeRoundCardImage(1);
		initializeTransportationCountersAndObstacle();
		initializeBackgroundPanels();
		initializeCardPanels();
		initializeInformationCardImage();
		initializeFaceUpTransportationCounters();
		initializeDeckOfTransportationCounters();
		initializeDeckOfTransportationCountersImage();
		initializeLeaderboard();
		initializeLeaderboard();
	}

	
	public void initializeBackgroundPanels()
	{
		// Set Bounds for background Player's Transportation Counter zone
		backgroundPanel_ForTransportationCounters.setBounds(width*0/1440, height*623/900, width*1075/1440, height*70/900);
		backgroundPanel_ForTransportationCounters.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Obstacle zone
		backgroundPanel_ForObstacle.setBackground(Color.RED);
		backgroundPanel_ForObstacle.setBounds(width*1070/1440, height*623/900, width*80/1440, height*70/900);
		
		// Set Bounds for background Image zone
		backgroundPanel_ForMap.setBounds(width*0/1440, height*0/900, width*1150/1440, height*625/900);
		backgroundPanel_ForMap.setBackground(Color.BLUE);
		
		// Set Bounds for background Round zone
		backgroundPanel_ForRound.setBounds(width*1000/1440, height*34/900, width*86/1440, height*130/900);
		backgroundPanel_ForRound.setOpaque(false);
		
		// Set Bounds for background Cards zone
		backgroundPanel_ForCards.setBounds(width*0/1440, height*690/900, width*1150/1440, height*3/9);
		backgroundPanel_ForCards.setBackground(Color.WHITE);
		
		// Set Bounds for background Information zone
		backgroundPanel_ForInformationCard.setBounds(width*1150/1440, height*565/900, width*290/1440, height*330/900);
		backgroundPanel_ForInformationCard.setBackground(Color.WHITE);
		
		// Set Bounds for background Face Up Transportation Counter zone
		backgroundPanel_ForDeckOfTransportationCounters.setBounds(width*1150/1440, height*275/900, width*290/1440, height*289/900);
		backgroundPanel_ForDeckOfTransportationCounters.setBackground(Color.DARK_GRAY);
		
		backgroundPanel_ForLeaderboard.setBounds(width*1150/1440, height*0/900, width*290/1440, height*274/900);
		backgroundPanel_ForLeaderboard.setBackground(Color.DARK_GRAY);
	}
	
	public void initializeDeckOfTransportationCounters()
	{
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		
		JPanel panel1 = new JPanel();
		panel1.setBounds(width*1210/1440, height*290/900, width*70/1440, height*60/900);
		panel1.setOpaque(false);
		panel1.setBorder(whiteLine);
		panelForDeckOfTransportationCounters = panel1;
	}
	
	public void initializeFaceUpTransportationCounters()
	{
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		JPanel panel2 = new JPanel();
		panel2.setBounds(width*1315/1440, height*290/900, width*70/1440, height*60/900);
		panel2.setOpaque(false);
		panel2.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[0] = panel2;
		
		JPanel panel3 = new JPanel();
		panel3.setBounds(width*1210/1440, height*390/900, width*70/1440, height*60/900);
		panel3.setOpaque(false);
		panel3.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[1] = panel3;
		
		JPanel panel4 = new JPanel();
		panel4.setBounds(width*1315/1440, height*390/900, width*70/1440, height*60/900);
		panel4.setOpaque(false);
		panel4.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[2] = panel4;
		
		JPanel panel5 = new JPanel();
		panel5.setBounds(width*1210/1440, height*490/900, width*70/1440, height*60/900);
		panel5.setOpaque(false);
		panel5.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[3] = panel5;
		
		JPanel panel6 = new JPanel();
		panel6.setBounds(width*1315/1440, height*490/900, width*70/1440, height*60/900);
		panel6.setOpaque(false);
		panel6.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[4] = panel6;
	}
	
	public void initializeLeaderboard()
	{	
//		String[][] players = 
//		{
//				{"1", "0"},
//				{"2", "0"},
//				{"3", "0"},
//				{"4", "0"},
//				{"5", "0"},
//				{"6", "0"},
//		};
		
		List<Player> aPlayers = GameState.instance().getPlayers();
		
		String[][] playerScores = new String [aPlayers.size()][2];
		for (int i = 0; i < playerScores.length; i++){
			playerScores[i][0] = String.valueOf(i+1);
			playerScores[i][1] = String.valueOf(aPlayers.get(i).getScore());
		}
		
		String[] titles = {"PLAYERS", "POINTS"};
		
		leaderboard = new JTable (playerScores, titles);
		leaderboard.setRowHeight(height*40/900);
		
		backgroundPanel_ForLeaderboard.setLayout(new BorderLayout());
		backgroundPanel_ForLeaderboard.add(leaderboard.getTableHeader(), BorderLayout.PAGE_START);
		backgroundPanel_ForLeaderboard.add(leaderboard, BorderLayout.CENTER);
		
		backgroundPanel_ForLeaderboard.add(leaderboard);
	}
	
	public void initializeCardPanels()
	{
		int xCoordinate = width*2/1440;

		for (int i = 0; i < 8; i++)
		{
			JPanel panel = new JPanel();
			panel.setOpaque(false);
			//panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height*690/900, width*144/1440, height*3/9);
			panelForPlayerCards[i] = panel;
			xCoordinate += width*144/1440;
		}
	}
	
	public void initializeTransportationCountersAndObstacle()
	{
		int xCoordinate = width*10/1440;
		
		// Transportation Counters
		for (int i = 0; i < 5; i++)
		{
			JPanel panel= new JPanel();
			panel.setOpaque(false);
			panel.setBorder(whiteLine);
			panel.setBounds(xCoordinate, height*630/900, width*70/1440, height*60/900);
			panelForPlayerTransportationCounters[i] = panel;
			xCoordinate += width*200/1440;
		}
		
		// Obstacle
		panelForObstacle.setOpaque(false);
		panelForObstacle.setBorder(whiteLine);
		panelForObstacle.setBounds(width*1077/1440, height*630/900, width*70/1440, height*60/900);
	}
	
	public void initializeMapImage()
	{
		ImageIcon mapImage = new ImageIcon("./assets/sprites/map.png");
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(width*1140/1440, height*625/900,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		mapImage_BottomLayer = new JLabel(mapImage);
	}
	
	public void initializeRoundCardImage(int round)
	{
		ImageIcon roundImage = new ImageIcon("./assets/sprites/R1.png");
		Image Round = roundImage.getImage();
		Image RoundResized = Round.getScaledInstance(width*90/1440, height*130/900,  java.awt.Image.SCALE_SMOOTH);
		roundImage = new ImageIcon(RoundResized);
		roundImage_TopLayer = new JLabel(roundImage);
	}
	
	public void initializeInformationCardImage()
	{
		ImageIcon gridImage = new ImageIcon("./assets/sprites/grid.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*290/1440, height*325/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		informationCardImage_TopLayer = new JLabel(gridImage);
	}
	
	public void initializeDeckOfTransportationCountersImage()
	{
		ImageIcon gridImage = new ImageIcon("./assets/sprites/M08.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*67/1440, height*52/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		deckOfTransportationCountersImage_TopLayer = new JLabel(gridImage);
	}
	
	public void addImages()
	{
		backgroundPanel_ForMap.add(mapImage_BottomLayer);		
		backgroundPanel_ForRound.add(roundImage_TopLayer);
		backgroundPanel_ForInformationCard.add(informationCardImage_TopLayer);
		panelForDeckOfTransportationCounters.add(deckOfTransportationCountersImage_TopLayer);

		notifyObservers();
	}
	
	public void addPanelToScreen()
	{
		boardGame_Layers.add(backgroundPanel_ForRound, 0);
		boardGame_Layers.add(panelForDeckOfTransportationCounters,0);
		addFaceUpTransportationCounters();
		addTransportationCountersAndObstacle();
		addCards();
		boardGame_Layers.add(backgroundPanel_ForTransportationCounters, -1);
		boardGame_Layers.add(backgroundPanel_ForMap, -1); 
		boardGame_Layers.add(backgroundPanel_ForObstacle,-1);
		boardGame_Layers.add(backgroundPanel_ForCards, -1);
		boardGame_Layers.add(backgroundPanel_ForInformationCard, -1);
		boardGame_Layers.add(backgroundPanel_ForDeckOfTransportationCounters, -1);
		boardGame_Layers.add(backgroundPanel_ForLeaderboard,-1);

		for (Town town: gameMap.getTownList()) {
			boardGame_Layers.add(town.getPanel(), 0);
			boardGame_Layers.add(town.getPanel().getElfBootPanel(), 0);
		}
	}
	
	public void addFaceUpTransportationCounters()
	{
		ArrayList<TransportationCounter> faceUpCounters = GameState.instance().getFaceUpCounters();

		for (int i = 0; i < 5; i++) {
			JPanel panel = panelForFaceUpTransportationCounters[i];
			TransportationCounter counter = faceUpCounters.get(i);
			panel.add(counter.getImage());

			boardGame_Layers.add(panel, 0);
			panel.repaint();
			panel.revalidate();
		}
	}
	
	public void addCards()
	{
		// TODO: this code assumes that the player viewing the GUI is the black player
		// TODO: this is obviously wrong - we need to create a method to get the identity of the
		// TODO: player looking at this screen to determine which travel cards to show them
		List<CardUnit> myCards = GameState.instance().getPlayerByColour(Colour.BLACK).getHand().getCards();

		for (int p=0; p<panelForPlayerCards.length; p++)
		{
			JPanel panel = panelForPlayerCards[p];
			CardUnit card = myCards.get(p);
			panel.add(card.getImage());

			boardGame_Layers.add(panel, 0);
			panel.repaint();
			panel.revalidate();
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
	
	public void initializeListenerToDraw()
	{
		// here we will add a MouseListener to deckOfTransportationCountersImage_TopLayer

		deckOfTransportationCountersImage_TopLayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TransportationCounter drawn = GameState.instance().getCounterPile().draw(); // draw a counter
				for (int i = 0; i < 5; i++)
				{
					if (panelForPlayerTransportationCounters[i].getComponentCount() == 0) // if spot is empty, put card there
					{
						panelForPlayerTransportationCounters[i].add(drawn.getImage());
						mainframe.repaint();
						mainframe.revalidate();
						break;
					}
					// if we don't find an open spot, we do nothing. we can't put the counter anywhere.
				}
			}
		});
	}

	/*
	public void moveBlackBoot(JPanel newCurrentPanel)
	{
		ElfBoot blackBoot = gameState.getBootByColour(Colour.BLACK);

		// this is what we will use to update the game state based on the information sent over the network
		blackBoot.getCurPanel().remove(blackBoot.getImage());
		update(blackBoot.getCurPanel());

		// now switch the current panel
		blackBoot.setCurPanel((ElfBootPanel) newCurrentPanel);
		blackBoot.getCurPanel().add(blackBoot.getImage());
		update(blackBoot.getCurPanel());

	}

	public void setCurrentPanelOfBlueBoot(JPanel newCurrentPanel)
	{
		ElfBoot blueBoot = gameState.getBootByColour(Colour.BLUE);

		// this is what we will use to update the game state based on the information sent over the network
		blueBoot.getCurPanel().remove(blueBoot.getImage());
		update(blueBoot.getCurPanel());

		// now switch the current panel
		blueBoot.setCurPanel((ElfBootPanel) newCurrentPanel);
		blueBoot.getCurPanel().add(blueBoot.getImage());
		update(blueBoot.getCurPanel());

	}*/

	public static void main(String[] args) 
	{
		JFrame game_screen = new JFrame("GameScreen");
		
		game_screen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
		game_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game_screen.add(init(game_screen));
		game_screen.setVisible(true);
	}

	public void sendGameState(JPanel elfBootLocation) throws IOException
	{
		System.out.println("Sending the game state...");
		Socket connection = new Socket(otherPlayerIP, 4444); // start up the connection
		System.out.println("Outwards socket is up and running...");

		// send the Elf boot's new location
		JPanel toSend = elfBootLocation;
		OutputStream out = connection.getOutputStream();
		ObjectOutputStream payload = new ObjectOutputStream(out);
		payload.writeObject(toSend);
		System.out.println("Done writing the elf boot current location into payload...");
		payload.flush();
		System.out.println("Payload has been flushed.");
		connection.close();
		System.out.print("Done. connection has been closed.");
	}

	public void listen(int port) throws IOException
	{
		ServerSocket listener = new ServerSocket(port);
	}

	public boolean getMyTurn() {return myTurn;}

	public void setMyTurn(boolean pMyTurn) { this.myTurn = pMyTurn; }

	// only using this for the demo. See networking.NetworkDemoPlayer2 loop
	public void reverseTurn() {
		myTurn = !myTurn;
	}

	public void addElement(JPanel panel) {
		boardGame_Layers.add(panel);
		mainframe.repaint();
		mainframe.revalidate();
	}

	public void addObserverPanel(ObserverPanel pPanel) {
		this.observerPanels.add(pPanel);
	}

	public void notifyObservers() {
		for ( ObserverPanel observer : observerPanels ) {
			observer.updateView();
		}
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}
}
