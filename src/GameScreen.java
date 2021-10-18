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
	private JFrame screen;
	private JPanel mainPanel;
	private Border blackline;
	
	GameScreen (JFrame mainFrame)
	{
		//Testing with JPanel
		JPanel panel = new JPanel();
		panel.setBackground(Color.RED);
		panel.setBounds(0, 0, 1000, 500);
		mainFrame.add(panel);
		
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
