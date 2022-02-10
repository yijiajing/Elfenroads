package org.minueto.sample.swing.launch;

import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class LauncherDemo extends JFrame {
	
	private static final long serialVersionUID = -4259257259443495779L;
	
	private JPanel mainPanel;
	private JButton startGame, exitGame;

	public LauncherDemo() {
		
		// Setup our Window with a nice Title
		this.setTitle("My Game");
		this.setLocation(200, 200);
		
		// Let's create a new Panel to put things in
		mainPanel = new JPanel();
		
		// We need some layout guidelines though.
		// For some visual information, see here:
		// http://java.sun.com/docs/books/tutorial/uiswing/layout/visual.html
		FlowLayout flow = new FlowLayout();
		flow.setAlignment(FlowLayout.CENTER);
		mainPanel.setLayout(flow);
		mainPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				
		// Let's add some buttons
		startGame = new JButton("Start Game");
        // Make an Action
        startGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	setVisible(false);
            	
            	Thread workerThread = new Thread(new Worker());
            	workerThread.start();
            	
            	setVisible(false);
            }
        });
		mainPanel.add(startGame);

		exitGame = new JButton("Exit Game");
        // Make an Action
        exitGame.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	// Let's just exit
            	System.exit(0);
            }
        });
		mainPanel.add(exitGame);
	
		// And add it to our frame
		this.add(mainPanel);
		
		// Make sure we finalize our layout
		this.pack();
		
		this.setVisible(true);
	}
	
	
	
	public static void main(String[] args) {
		new LauncherDemo();
	}
	
}


class Worker implements Runnable {

	public void run() {

		GameWindow gameWindow = new GameWindow();		
		gameWindow.start();				
	}
	
}