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
import java.util.Stack;

import javax.swing.*;
import javax.swing.border.Border;

import domain.*;
import org.minueto.MinuetoTool;

public class GameScreen extends JPanel implements Serializable
{
	private JFrame mainFrame;
	private Integer width;
	private Integer height;
	
	private JLayeredPane boardGame_Layers;

	private Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
	
	private JLabel roundImage_TopLayer;
	private JLabel mapImage_BottomLayer;
	private JLabel informationCardImage_TopLayer;
	private JLabel deckOfTransportationCountersImage_TopLayer;
	private ElfBoot elfBoot1;
	private ElfBoot elfBoot2;
	
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

	private GameMap gameMap;

	private JTable leaderboard = new JTable();
	
	private Deck transportationCountersToDraw;
	//private String filepathToRepo = "/Users/nicktriantos/Desktop/f2021-hexanome-12"; // change this depending on whose machine we are using
	// private String filepathToRepo = "/Users/charlescouture/eclipse-workspace/COMP361";
	private String filepathToRepo = ".";
	private boolean myTurn;

	// TODO: change this on the other computer
	private String otherPlayerIP = "192.168.2.253"; // Nick's IP address

	
	// alternate constructor for networking demo
	public GameScreen (JFrame frame, boolean isTurn)
	{
		// layout is necessary for JLayeredPane to be added to the JPanel
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Get dimensions of the full screen
		mainFrame = frame;
		width = mainFrame.getWidth();
		height = mainFrame.getHeight();

		// Set Bounds for entire Board Game screen and do the initialization of the structure for the UI
		boardGame_Layers = new JLayeredPane();
		boardGame_Layers.setBounds(0,0,width,height);

		// initialize town and road panels
		gameMap = new GameMap(this);

		// initialize elf boots
		elfBoot1 = new ElfBoot("black", this.width, this.height, gameMap.getTown("Elvenhold").getPanel().getElfBootPanel(), 0);
		elfBoot2 = new ElfBoot("blue", this.width, this.height, gameMap.getTown("Elvenhold").getPanel().getElfBootPanel(), 1);

		initialization();

		// Add the images to their corresponding JPanel
		addImages();
		initializeListenerToDraw();
		
		// Add the JPanels to the main JLayeredPane with their corresponding layer
		addPanelToScreen();
		
		// Add the entire structure of the UI to the panel
		this.add(boardGame_Layers);

		// doing this for the demo
		myTurn = isTurn;
	}

	public GameScreen (JFrame frame)
	{
		// layout is necessary for JLayeredPane to be added to the JPanel
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		// Get dimensions of the full screen
		mainFrame = frame;
		width = mainFrame.getWidth();
		height = mainFrame.getHeight();

		// Set Bounds for entire Board Game screen and do the initialization of the structure for the UI
		boardGame_Layers = new JLayeredPane();
		boardGame_Layers.setBounds(0,0,width,height);

		// initialize town and road panels
		gameMap = new GameMap(this);

		// initialize elf boots
		elfBoot1 = new ElfBoot("black", this.width, this.height, gameMap.getTown("Elvenhold").getPanel().getElfBootPanel(), 0);
		elfBoot2 = new ElfBoot("blue", this.width, this.height, gameMap.getTown("Elvenhold").getPanel().getElfBootPanel(), 1);

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
		initializeCards();
		initializeInformationCardImage();
		initializeFaceUpTransportationCounters();
		initializeDeckOfTransportationCounters();
		initializeDeckOfTransportationCountersImage();
		initializeLeaderboard();
		initializeTransportationCounters();
		initializeLeaderboard();
	}
	
	public void initializeBackgroundPanels()
	{
		// Set Bounds for background domain.Player Transportation Counter zone
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
		
		backgroundPanel_ForLeaderboard.setBounds(width*1065/1440, height*0/900, width*375/1440, height*274/900);
		backgroundPanel_ForLeaderboard.setBackground(Color.DARK_GRAY);
	}
	
	public void initializeDeckOfTransportationCounters()
	{
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		
		JPanel panel1 = new JPanel();
		panel1.setBounds(width*1170/1440, height*290/900, width*70/1440, height*60/900);
		panel1.setOpaque(false);
		panel1.setBorder(whiteLine);
		panelForDeckOfTransportationCounters = panel1;
	}
	
	public void initializeFaceUpTransportationCounters()
	{
		Border whiteLine = BorderFactory.createLineBorder(Color.WHITE);
		JPanel panel2 = new JPanel();
		panel2.setBounds(width*1280/1440, height*290/900, width*70/1440, height*60/900);
		panel2.setOpaque(false);
		panel2.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[0] = panel2;
		
		JPanel panel3 = new JPanel();
		panel3.setBounds(width*1170/1440, height*390/900, width*70/1440, height*60/900);
		panel3.setOpaque(false);
		panel3.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[1] = panel3;
		
		JPanel panel4 = new JPanel();
		panel4.setBounds(width*1280/1440, height*390/900, width*70/1440, height*60/900);
		panel4.setOpaque(false);
		panel4.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[2] = panel4;
		
		JPanel panel5 = new JPanel();
		panel5.setBounds(width*1170/1440, height*490/900, width*70/1440, height*60/900);
		panel5.setOpaque(false);
		panel5.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[3] = panel5;
		
		JPanel panel6 = new JPanel();
		panel6.setBounds(width*1280/1440, height*490/900, width*70/1440, height*60/900);
		panel6.setOpaque(false);
		panel6.setBorder(whiteLine);
		panelForFaceUpTransportationCounters[4] = panel6;
	}
	
	public void initializeLeaderboard()
	{	
		String[][] players = 
		{
				{"1", "0"},
				{"2", "0"},
				{"3", "0"},
				{"4", "0"},
				{"5", "0"},
				{"6", "0"},
		};
		String[] titles = {"PLAYERS", "POINTS"};
		
		leaderboard = new JTable (players, titles);
		leaderboard.setRowHeight(height*40/900);
		
		backgroundPanel_ForLeaderboard.setLayout(new BorderLayout());
		backgroundPanel_ForLeaderboard.add(leaderboard.getTableHeader(), BorderLayout.PAGE_START);
		backgroundPanel_ForLeaderboard.add(leaderboard, BorderLayout.CENTER);
		
		backgroundPanel_ForLeaderboard.add(leaderboard);
	}
	
	public void initializeCards()
	{
		int xCoordinate = width*2/1440;

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
		ImageIcon mapImage = new ImageIcon(filepathToRepo + "/assets/sprites/map.png");
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(width*1064/1440, height*564/900,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		mapImage_BottomLayer = new JLabel(mapImage);
	}
	
	public void initializeRoundCardImage(int round)
	{
		String image = filepathToRepo + "R" + String.valueOf(round) + ".png";
		ImageIcon roundImage = new ImageIcon(filepathToRepo + "/assets/sprites/R1.png");
		Image Round = roundImage.getImage();
		Image RoundResized = Round.getScaledInstance(width*90/1440, height*130/900,  java.awt.Image.SCALE_SMOOTH);
		roundImage = new ImageIcon(RoundResized);
		roundImage_TopLayer = new JLabel(roundImage);
	}
	
	public void initializeInformationCardImage()
	{
		ImageIcon gridImage = new ImageIcon(filepathToRepo + "/assets/sprites/grid.png");
		Image grid = gridImage.getImage();
		Image gridResized = grid.getScaledInstance(width*360/1440, height*325/900,  java.awt.Image.SCALE_SMOOTH);
		gridImage = new ImageIcon(gridResized);
		informationCardImage_TopLayer = new JLabel(gridImage);
	}
	
	public void initializeDeckOfTransportationCountersImage()
	{
		ImageIcon gridImage = new ImageIcon(filepathToRepo + "/assets/sprites/M08.png");
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
		elfBoot1.getCurSpotInPanel().add(elfBoot1.getImage());
		elfBoot2.getCurSpotInPanel().add(elfBoot2.getImage());
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

			// add the JPanels for every spot on every elf boot panel
			for (int spot = 0; spot < 6; spot++) {
				boardGame_Layers.add(town.getPanel().getElfBootPanel().getSpotByNumber(spot), 0);
			}
		}
	}
	
	public void addFaceUpTransportationCounters()
	{	
		for (JPanel panel : panelForFaceUpTransportationCounters)
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

	public void initializeTransportationCounters()
	{
		// we have 8 of each counter in the game
		Stack<TransportationCounter> toAddToDeck = new Stack<TransportationCounter>();

		for (int i = 1; i <= 8; i++)
		{
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.DRAGON, width*67/1440, height*52/900));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.GIANTPIG, width*67/1440, height*52/900));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.UNICORN, width*67/1440, height*52/900));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.TROLLWAGON, width*67/1440, height*52/900));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.ELFCYCLE, width*67/1440, height*52/900));
			toAddToDeck.push(new TransportationCounter(TransportationCounter.CounterType.MAGICCLOUD, width*67/1440, height*52/900));
		}

		transportationCountersToDraw = new Deck(toAddToDeck);
	}
	
	public void initializeListenerToDraw()
	{
		// here we will add a MouseListener to deckOfTransportationCountersImage_TopLayer

		deckOfTransportationCountersImage_TopLayer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TransportationCounter drawn = transportationCountersToDraw.draw(); // draw a counter
				for (int i = 0; i < 5; i++)
				{
					if (panelForPlayerTransportationCounters[i].getComponentCount() == 0) // if spot is empty, put card there
					{
						panelForPlayerTransportationCounters[i].add(drawn.getDisplay());
						mainFrame.repaint();
						mainFrame.revalidate();
						break;
					}
					// if we don't find an open spot, we do nothing. we can't put the counter anywhere.
				}
			}
		});
	}

	public void moveElfBoot1(JPanel newCurrentPanel)
	{
		// this is what we will use to update the game state based on the information sent over the network
		elfBoot1.getCurSpotInPanel().remove(elfBoot1.getImage());
		elfBoot1.getCurPanel().setSpotAvailability(elfBoot1.getCurSpotInPanel(), true);
		update(elfBoot1.getCurSpotInPanel());

		// now switch the current panel
		elfBoot1.setCurPanelAndSpot((ElfBootPanel) newCurrentPanel);
		elfBoot1.getCurSpotInPanel().add(elfBoot1.getImage());
		update(elfBoot1.getCurSpotInPanel());

	}

	public void setCurrentPanelOfElfBoot2(JPanel newCurrentPanel)
	{
		// this is what we will use to update the game state based on the information sent over the network
		elfBoot2.getCurSpotInPanel().remove(elfBoot2.getImage());
		elfBoot2.getCurPanel().setSpotAvailability(elfBoot2.getCurSpotInPanel(), true);
		update(elfBoot2.getCurSpotInPanel());

		// now switch the current panel
		elfBoot2.setCurPanelAndSpot((ElfBootPanel) newCurrentPanel);
		elfBoot2.getCurSpotInPanel().add(elfBoot2.getImage());
		update(elfBoot2.getCurSpotInPanel());

	}

	public static void main(String[] args) 
	{
		JFrame game_screen = new JFrame("panel.GameScreen");
		
		game_screen.setSize(MinuetoTool.getDisplayWidth(), MinuetoTool.getDisplayHeight());
		game_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game_screen.add(new GameScreen(game_screen, true));
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
	public void reverseTurn()
	{
		myTurn = !myTurn;
	}

	public ElfBoot getElfBoot1() { return this.elfBoot1; }

	public ElfBoot getElfBoot2() { return this.elfBoot2; }

	public int getWidth() { return this.width; }

	public int getHeight() { return this.height; }

	public void addElement(JPanel panel) {
		boardGame_Layers.add(panel);
		mainFrame.repaint();
		mainFrame.revalidate();
	}
}
