package panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;

import java.util.List;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.swing.*;
import javax.swing.border.Border;

import domain.*;
import enums.RoundPhaseType;
import enums.TravelCardType;
import networking.GameState;
import org.minueto.MinuetoTool;
import utils.GameRuleUtils;

/**
 * A Singleton class that represents the main screen in which the board game is played
 */
public class GameScreen extends JPanel implements Serializable
{
	private static GameScreen INSTANCE; // Singleton instance 

	private JFrame mainframe;
	public static Integer width;
	public static Integer height;
	
	private JLayeredPane boardGame_Layers;

	private Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
	
	private JLabel roundImage_TopLayer;
	private JLabel mapImage_BottomLayer;
	private JLabel informationCardImage_TopLayer;
	
	private final JPanel backgroundPanel_ForMap = new JPanel();
	private final JPanel backgroundPanel_ForRound = new JPanel();
	private final JPanel backgroundPanel_ForObstacle = new JPanel();
	private final JPanel backgroundPanel_ForCards = new JPanel();
	private final JPanel backgroundPanel_ForTransportationCounters = new JPanel();
	private final JPanel backgroundPanel_ForInformationCard = new JPanel();
	private final JPanel backgroundPanel_ForDeckOfTransportationCounters = new JPanel();
	private final JPanel backgroundPanel_ForLeaderboard = new JPanel();

	private final JPanel[] panelForPlayerTransportationCounters = new JPanel[5];
	private final JPanel[] panelForPlayerCards = new JPanel[8];
	private final JPanel[] panelForFaceUpTransportationCounters = new JPanel[5];
	private final JPanel panelForDeckOfTransportationCounters = new JPanel();
	private final JPanel panelForObstacle = new JPanel();

	private ArrayList<ObserverPanel> observerPanels = new ArrayList<>();

	private GameMap gameMap;

	private static String prevMessage;

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

	public void updateAll() {
		addTransportationCountersAndObstacle(); // updates the player's counter area
		addCards(); // update's the player's cards
		addFaceUpTransportationCounters(); // updates the face-up transportation counters
		notifyObservers(); // updates elf boots and town pieces
		updateLeaderboard();
	}

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
		initializeEndTurnButton();

		initializeMenuButton();
		intializeMenu();

		updateAll();
	}

	
	public void initializeBackgroundPanels()
	{
		// Set Bounds for background Player's Transportation Counter zone
		backgroundPanel_ForTransportationCounters.setBounds(width*0/1440, height*623/900, width*900/1440, height*70/900);
		backgroundPanel_ForTransportationCounters.setBackground(Color.DARK_GRAY);
		
		// Set Bounds for background Obstacle zone
		backgroundPanel_ForObstacle.setBackground(Color.RED);
		backgroundPanel_ForObstacle.setBounds(width*900/1440, height*623/900, width*80/1440, height*70/900);
		
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
		
		JPanel panel = panelForDeckOfTransportationCounters;
		panel.setBounds(width*1210/1440, height*290/900, width*70/1440, height*65/900);
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
	
	public void initializeLeaderboard()
	{
		List<Player> aPlayers = GameState.instance().getPlayers();
		
		backgroundPanel_ForLeaderboard.setLayout(new BoxLayout(backgroundPanel_ForLeaderboard, BoxLayout.Y_AXIS));
		backgroundPanel_ForLeaderboard.setAlignmentX(CENTER_ALIGNMENT);
		for (Player player:aPlayers) {
			backgroundPanel_ForLeaderboard.add(new ScoreBoardPanel(this, player));
			backgroundPanel_ForLeaderboard.add(Box.createRigidArea(new Dimension(0,5)));
		}

	}
	
	//delete and re-initialize scoreboards 
	public void updateLeaderboard() {
		backgroundPanel_ForLeaderboard.removeAll();
		this.initializeLeaderboard();
		backgroundPanel_ForLeaderboard.revalidate();
		backgroundPanel_ForLeaderboard.repaint();
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
			boardGame_Layers.add(panel, 0);
		}
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
		panelForObstacle.setOpaque(false);
		//panelForObstacle.setBorder(whiteLine);
		panelForObstacle.setBounds(width*900/1440, height*625/900, width*70/1440, height*65/900);
		boardGame_Layers.add(panelForObstacle,0);
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
		ImageIcon roundImage = new ImageIcon("./assets/sprites/R" + round + ".png");
		Image Round = roundImage.getImage();
		Image RoundResized = Round.getScaledInstance(width*90/1440, height*130/900,  java.awt.Image.SCALE_SMOOTH);
		roundImage = new ImageIcon(RoundResized);
		roundImage_TopLayer = new JLabel(roundImage);
		backgroundPanel_ForRound.removeAll();
		backgroundPanel_ForRound.add(roundImage_TopLayer);
	}
	
	public void initializeInformationCardImage()
	{
		ImageIcon gridImage = new ImageIcon("./assets/sprites/grid.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*290/1440, height*325/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		informationCardImage_TopLayer = new JLabel(gridImage);
	}

	public void initializeEndTurnButton() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(width*1000/1440, height*625/900, width*100/1440, height*65/900);
		buttonPanel.setOpaque(false);
		boardGame_Layers.add(buttonPanel);

		JButton endTurn = new EndTurnButton();
		endTurn.setBounds(width*1000/1440, height*625/900, width*100/1440, height*65/900);
		buttonPanel.add(endTurn);
	}

	public void initializeMenuButton() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBounds(width*1000/1440, height*625/900+30, width*100/1440, height*65/900);
		buttonPanel.setOpaque(false);
		boardGame_Layers.add(buttonPanel);

		JButton menu = new MenuButton();
		menu.setBounds(width*1000/1440, height*625/900+30, width*100/1440, height*65/900);
		buttonPanel.add(menu);
	}

	public void intializeMenu(){
		JMenu menu, submenu;
    	JMenuItem i1, i2, i3, i4, i5, i6, i7;
		JMenuBar mb = new JMenuBar();
        menu = new JMenu("Menu");
        submenu = new JMenu("Rules");

        i1 = new JMenuItem("Save");
        i2 = new JMenuItem("Load");
        i3 = new JMenuItem("Chat");
        i4 = new JMenuItem("Elfenland");
        i5 = new JMenuItem("Elfengold");
		i6 = new JMenuItem("Exit to menu");
		i7 = new JMenuItem("Exit to desktop");

		i4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
                    try {
                        
                        File myFile = new File("./assets/rules/Elfengold Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
				
			}});

		i5.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
                    try {
                        
                        File myFile = new File("./assets/rules/Elfengold Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
				
			}});

		i4.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
                    try {
                        
                        File myFile = new File("./assets/rules/Elfengold Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
				
			}});

		i5.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if (Desktop.isDesktopSupported()) {
                    try {
                        
                        File myFile = new File("./assets/rules/Elfengold Rules.pdf");
                        Desktop.getDesktop().open(myFile);
                    } catch (IOException ex) {
                        // no application registered for PDFs
                    }
                }
				
			}});

        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        submenu.add(i4);
        submenu.add(i5);
        menu.add(submenu);
        mb.add(menu);
        mainframe.setJMenuBar(mb);
        
	}

	public void addImages()
	{
		backgroundPanel_ForMap.add(mapImage_BottomLayer);
		backgroundPanel_ForInformationCard.add(informationCardImage_TopLayer);
		panelForDeckOfTransportationCounters.add(GameState.instance().getCounterPile().getImage());

		drawTownPieces();

		notifyObservers();
	}

	public void drawTownPieces() {

		// put town pieces on every town except for Elvenhold
		for (Town t : GameMap.getInstance().getTownList()) {
			if (!t.getName().equalsIgnoreCase("Elvenhold")) {
				t.initializeTownPieces();
			}
		}
	}

	public void addPanelToScreen()
	{
		boardGame_Layers.add(backgroundPanel_ForRound, 0);
		boardGame_Layers.add(panelForDeckOfTransportationCounters,0);
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
	
	public void addCards()
	{
		// clear the previous cards from the screen
		for (JPanel panel : panelForPlayerCards) {
			if (panel != null) {
				panel.removeAll();
				panel.repaint();
				panel.revalidate();
			}
		}

		List<CardUnit> myCards = GameManager.getInstance().getThisPlayer().getHand().getCards();

		// draw the cards to the screen
		for (int p = 0; p < myCards.size(); p++) {
			JPanel panel = panelForPlayerCards[p];
			CardUnit card = myCards.get(p);
			panel.add(card.getDisplay());
			panel.repaint();
			panel.revalidate();
		}
	}
	
	public void addTransportationCountersAndObstacle()
	{
		// remove a counter if it was already there
		for (JPanel panel : panelForPlayerTransportationCounters) {
			if (panel != null) {
				panel.removeAll();
				panel.repaint();
				panel.revalidate();
			}
		}

		List<TransportationCounter> counters = GameManager.getInstance().getThisPlayer().getHand().getCounters();

		// draw the counters to the screen
		for (int c = 0; c < counters.size(); c++) {
			JPanel panel = panelForPlayerTransportationCounters[c];
			TransportationCounter counter = counters.get(c);
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

	public static void main(String[] args) 
	{
		GameScreen game_screen = GameScreen.init(new JFrame());
		
		game_screen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
		game_screen.draw();
		game_screen.setVisible(true);
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

	public GameMap getGameMap() {
		return gameMap;
	}

	public static void displayMessage(String message)
	{
		prevMessage = message;
		JOptionPane.showMessageDialog(null, message);
	}

	public static String getPrevMessage()
	{
		return prevMessage;
	}
}
