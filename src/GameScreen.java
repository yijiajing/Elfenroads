import java.awt.Color;
import java.awt.Image;

import javax.swing.*;
import org.minueto.MinuetoTool;

public class GameScreen extends JPanel
{
	private JFrame MainFrame;
	
	private JLayeredPane BoardGame_Layers = new JLayeredPane();
	
	private JLabel RoundImage_Layer0;
	private JLabel MapImage_Layer1;
	
	private JPanel PanelForMap = new JPanel();
	private JPanel PanelForRound = new JPanel();
	
	GameScreen (JFrame mainFrame)
	{
		MainFrame = mainFrame;
		
		// Set Bounds for Board Game and initialize the images
		BoardGame_Layers.setBounds(0,0,1064,564);
		initializeMapImage();
		initializeRounCard(1);

		// Add the map image to its corresponding JPanel
		PanelForMap.setBounds(0, 0, 1064, 564);
		PanelForMap.setBackground(Color.BLUE);
		PanelForMap.add(MapImage_Layer1);
		updateScreen();
		
		// Add the Round image to its corresponding JPanel
		PanelForRound.setBounds(932, 39, 86, 94);
		PanelForRound.setBackground(Color.WHITE);
		PanelForRound.add(RoundImage_Layer0);
		updateScreen();
		
		// Add the elements on top of the Board Game
		BoardGame_Layers.add(PanelForRound, 0);
		BoardGame_Layers.add(PanelForMap, 1);
		updateScreen();
		
		// Add Board Game with Round on top of it to the main screen
		MainFrame.add(BoardGame_Layers);
		updateScreen();
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.BLUE);
		panel2.setBounds(0, 565, 983, 70);
		MainFrame.add(panel2);
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.GREEN);
		panel3.setBounds(984, 565, 80, 70);
		mainFrame.add(panel3);
	}
	
	public void initializeMapImage()
	{
		ImageIcon mapImage = new ImageIcon(getClass().getResource("map.png"));
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(1064, 564,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		MapImage_Layer1 = new JLabel(mapImage);
	}
	
	public void initializeRounCard(int round)
	{
		String image = "R" + String.valueOf(round) + ".png";
		ImageIcon RoundImage = new ImageIcon(getClass().getResource(image));
		Image Round = RoundImage.getImage();
		Image RoundResized = Round.getScaledInstance(90, 90,  java.awt.Image.SCALE_SMOOTH);
		RoundImage = new ImageIcon(RoundResized);
		RoundImage_Layer0 = new JLabel(RoundImage);
	}
	
	public void updateScreen()
	{
		repaint();
		revalidate();
	}
	
	public static void main(String[] args) 
	{
		JFrame game_screen = new JFrame("GameScreen");
		game_screen.setSize(MinuetoTool.getDisplayWidth() - 100, MinuetoTool.getDisplayHeight() - 100);
		
		game_screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		game_screen.add(new GameScreen(game_screen));
		game_screen.setVisible(true);
		
	}

}
