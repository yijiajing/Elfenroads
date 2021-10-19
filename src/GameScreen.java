package testing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import org.minueto.MinuetoTool;

public class GameScreen extends JPanel
{
	GameScreen (JFrame mainFrame)
	{
		// Prepare the map image
		ImageIcon mapImage = new ImageIcon(getClass().getResource("map.png"));
		Image map = mapImage.getImage();
		Image mapResized = map.getScaledInstance(1000, 500,  java.awt.Image.SCALE_SMOOTH);
		mapImage = new ImageIcon(mapResized);
		JLabel mapComponent = new JLabel(mapImage);
		
		// Add the map image to the correct JPanel
		JPanel panel1 = new JPanel();
		panel1.add(mapComponent);
		panel1.setBackground(Color.BLUE);
		panel1.setBounds(0, 0, 1000, 500);
		revalidate();
		mainFrame.add(panel1);	
		
		JPanel panel2 = new JPanel();
		panel2.setBackground(Color.BLUE);
		panel2.setBounds(0, 501, 920, 70);
		mainFrame.add(panel2);
		
		JPanel panel3 = new JPanel();
		panel3.setBackground(Color.GREEN);
		panel3.setBounds(921, 501, 80, 70);
		mainFrame.add(panel3);		
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
