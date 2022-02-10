package org.minueto.sample.swing.panel;

/**
 * @(#)RectangleDemo.java        1.00 15/09/2004
 *
 * Minueto - The Game Development Framework 
 * Copyright (c) 2004 McGill University
 * 3480 University Street, Montreal, Quebec H3A 2A7
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 **/

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Random;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.MinuetoStopWatch;
import org.minueto.MinuetoTool;
import org.minueto.handlers.MinuetoFocusHandler;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoPanel;

/**
 * Example of running Minueto inside a Swing Frame utilizing the new MinuetoPanel.
 * 
 * @author Michael A. Hawker
 * @since 1.1.5
 * @version 1.0
 * @see org.minueto.window.MinuetoPanel
 **/
public class PanelDemo {

	private JDesktopPane desktop;
	private JFrame mainFrame;
	private OutputWindow output;

	private boolean isRunning = true;
	private int number = 1;
	
	private Vector<JInternalFrame> hidden = new Vector<JInternalFrame>();

	public PanelDemo() {

		GraphicsDevice				grdDevice;
		GraphicsConfiguration		grcConfiguration;
		GraphicsEnvironment 		greEnvironment;

		greEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		grdDevice = greEnvironment.getDefaultScreenDevice();
		grcConfiguration = grdDevice.getDefaultConfiguration();

		this.mainFrame = new JFrame(grcConfiguration); 

		this.mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.mainFrame.setResizable(true);
		this.mainFrame.setFocusTraversalKeysEnabled(false);
		this.mainFrame.setSize(MinuetoTool.getDisplayWidth() - 100, 
				MinuetoTool.getDisplayHeight() - 100);

		this.mainFrame.setJMenuBar(createMenu());

		this.desktop = new JDesktopPane();
		this.desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
		desktop.setBackground(new Color(192, 192, 192));


		//JPanel contentPane = new JPanel();
		//contentPane.setLayout(new BorderLayout());
		//contentPane.add(desktop, BorderLayout.CENTER);
		//contentPane.setBackground(new Color(192, 192, 192));

		this.mainFrame.setContentPane(desktop);
		this.mainFrame.setTitle("MinuetoPanel Interface Demo");
		
		output = new OutputWindow();
		desktop.add(output);
	}

	private JMenuBar createMenu() {

		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		JMenuItem newItem = new JMenuItem("New 320x240");
		newItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame frame = new InternalPanel(number, 320, 240);
				desktop.add(frame);
				frame.setVisible(true);
				number++;
			}
		});
		fileMenu.add(newItem);
		
		JMenuItem newItem2 = new JMenuItem("New 640x480");
		newItem2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame frame = new InternalPanel(number, 640, 480);
				desktop.add(frame);
				frame.setVisible(true);
				number++;
			}
		});
		fileMenu.add(newItem2);
		
		JMenuItem newItem3 = new JMenuItem("New 800x600");
		newItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame frame = new InternalPanel(number, 800, 600);
				desktop.add(frame);
				frame.setVisible(true);
				number++;
			}
		});
		fileMenu.add(newItem3);
		
		
				
		fileMenu.addSeparator();
		
		JMenuItem hideItem = new JMenuItem("Hide");
		hideItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame frame = desktop.getSelectedFrame();
				frame.setVisible(false);
				if (!(frame instanceof OutputWindow)) {
					hidden.add(frame);
				}
			}
		});
		fileMenu.add(hideItem);	
		
		JMenuItem closeItem = new JMenuItem("Close");
		closeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame frame = desktop.getSelectedFrame();
				if (frame instanceof OutputWindow) {
					frame.setVisible(false);
				} else {
					desktop.remove(frame);
					frame.dispose();
					frame = null;
					desktop.repaint();
				}
			}
		});
		fileMenu.add(closeItem);	
		
		fileMenu.addSeparator();
		
		JMenuItem showItem = new JMenuItem("Show All");
		showItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setVisible(true);
								
				for(JInternalFrame frame: hidden) {
					frame.setVisible(true);
				}
				hidden.clear();
			}
		});
		fileMenu.add(showItem);	
		
		JMenuItem outputItem = new JMenuItem("Show Output Window");
		outputItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				output.setVisible(true);
			}
		});
		fileMenu.add(outputItem);	
		
		fileMenu.addSeparator();

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Need more cleanup here
				System.exit(0);
			}
		});
		fileMenu.add(exitItem);

		JMenu helpMenu = new JMenu("Help");
		menuBar.add(helpMenu);

		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(mainFrame, "MinuetoPanel Interface Demo v1.0\nBy: Michael A. Hawker", "About", JOptionPane.PLAIN_MESSAGE);
			}		
		});
		helpMenu.add(aboutItem);

		return menuBar;

	}

	private class InternalPanel extends JInternalFrame implements MinuetoKeyboardHandler, MinuetoMouseHandler, MinuetoFocusHandler, Runnable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4327002156002972181L;

		private int id;
		
		private MinuetoPanel mwiPanel;
		private MinuetoEventQueue eventQueue;
		
		public InternalPanel(int number, int width, int height) {			
			super("Minueto Test Window " + number);
			id = number;
			
			createInternalFrame(width, height);
			
			Thread thread = new Thread(this);
			thread.start();
		}

		private void createInternalFrame(int width, int height) {

			JInternalFrame frameWindow = this;

			frameWindow.setResizable(true);
			frameWindow.setLayout(new FlowLayout());
			frameWindow.setMaximizable(true);
			frameWindow.setSize(new Dimension(width, height));

			// Register Window Listener to notify render loop that window has closed
			frameWindow.addInternalFrameListener(new InternalFrameListener() {
				public void internalFrameActivated(InternalFrameEvent arg0) {}			
				public void internalFrameClosed(InternalFrameEvent arg0) {}

				public void internalFrameClosing(InternalFrameEvent arg0) {
					isRunning = false;				
				}

				public void internalFrameDeactivated(InternalFrameEvent arg0) {}
				public void internalFrameDeiconified(InternalFrameEvent arg0) {}
				public void internalFrameIconified(InternalFrameEvent arg0) {}
				public void internalFrameOpened(InternalFrameEvent arg0) {}
			});

			frameWindow.addComponentListener(new ComponentListener() {
				public void componentHidden(ComponentEvent arg0) {}
				public void componentMoved(ComponentEvent arg0) {}

				public void componentResized(ComponentEvent arg0) {
					int width = getWidth() - getInsets().left - getInsets().right;
					int height = (int)getContentPane().getBounds().getHeight();
					output.println("Resize: " + width + ", " + height);
					mwiPanel.setSize(width, height);
				}

				public void componentShown(ComponentEvent arg0) {}
			});

			// Create a window.
			mwiPanel = new MinuetoPanel(width, height);

			// Create and register event queue
			eventQueue = new MinuetoEventQueue();
			/* 
			 * Note that you need to click in the MinuetoPanel to give it focus
			 * for it to receive keyboard input just like any other Swing component.
			 * 
			 * i.e. if you click in the textbox to type, the input will only be
			 * sent to the textbox and not the MinuetoPanel.  If you want input to
			 * resume to the MinuetoPanel, you need to click in it first.
			 */
			mwiPanel.registerKeyboardHandler(this, eventQueue);
			mwiPanel.registerMouseHandler(this, eventQueue);
			mwiPanel.registerFocusHandler(this, eventQueue);

			frameWindow.setContentPane(mwiPanel);

			frameWindow.pack();
		}

		/* (non-Javadoc)
		 * @see javax.swing.JComponent#setVisible(boolean)
		 */
		public void setVisible(boolean arg0) {
			// TODO Auto-generated method stub
			super.setVisible(arg0);
			
			// make our panel visible too
			if (mwiPanel != null) mwiPanel.setVisible(arg0);
		}

		public void run() {
			MinuetoFont fontArial16;				// Font used to write framerate.
			MinuetoImage imageFramerate;			// Text use to display framerate.
			MinuetoImage imageText;
			MinuetoStopWatch mswWatch;			// Timer used to calculate framerate.
			MinuetoColor color[] = new MinuetoColor[5];	// Array of color.

			Random ranNumGenerator;				// Our random number generator.

			double dFrameRate;					// Current framerate.
			int numberLines;					// Current number of lines drawn on screen.
			int frameCount;					// Used to calculate the framerate

			int i,j,k,x,y,c;					// Temporary integers.


			// Init random number generator.
			ranNumGenerator = new Random();		

			// Init the timer.
			mswWatch = new MinuetoStopWatch();

			// Init the font.
			fontArial16 = new MinuetoFont("Arial",16,false, false);

			// Prepare the color array.
			color[0] = MinuetoColor.BLACK;
			color[1] = MinuetoColor.RED;
			color[2] = MinuetoColor.GREEN;
			color[3] = MinuetoColor.BLUE;
			color[4] = MinuetoColor.WHITE;

			// Init the framerate text (so it's not null on the first frames).		
			imageFramerate = new MinuetoText("FPS: 0",fontArial16,MinuetoColor.BLUE);
			imageText = new MinuetoText("",fontArial16,MinuetoColor.WHITE);

			// Start the timer.
			mswWatch.start();

			// Get reday to count the FPS		
			frameCount = 0;

			// At first, we draw no lines
			numberLines = 0;

			// Game/Rendering loop	
			while(isRunning) {

				synchronized (mwiPanel) {
					if (mwiPanel.isVisible()) {
						
						// Handle all the events in the event queue.
						while (eventQueue.hasNext()) {
							eventQueue.handle();
						}
						
						mwiPanel.clear(MinuetoColor.BLACK);

						mwiPanel.drawLine(color[1], 0, 0, mwiPanel.getWidth()-1, 0);
						mwiPanel.drawLine(color[1], 0, mwiPanel.getHeight()-1, mwiPanel.getWidth()-1, mwiPanel.getHeight()-1);
						mwiPanel.drawLine(color[1], 0, 0, 0, mwiPanel.getHeight()-1);
						mwiPanel.drawLine(color[1], mwiPanel.getWidth()-1, 0, mwiPanel.getWidth()-1, mwiPanel.getHeight()-1);

						// For every lines we are supposed to draw:
						for(i = 0; i < numberLines; i++) {
							j = ranNumGenerator.nextInt(mwiPanel.getWidth()); //Starting X of the line.
							k = ranNumGenerator.nextInt(mwiPanel.getHeight()); //Starting Y of the line.
							x = ranNumGenerator.nextInt(mwiPanel.getWidth()); //Ending X of the line.
							y = ranNumGenerator.nextInt(mwiPanel.getHeight()); //Ending Y of the line.
							c = ranNumGenerator.nextInt(5);	// Random color of the line.
							mwiPanel.drawLine(color[c], j, k, x, y); // Draw the line.
						}

						// Count this frame
						frameCount++;
						// We update the framerate every thread frames.
						if (frameCount == 20) {
							mswWatch.stop();

							// Using the timer, we calculate how much time was needed to
							// draw the 20 frames. This gives us our framterate.
							dFrameRate =  (double)20000/(double)mswWatch.getTime();
							// If our FPS is > 31, then we add some lines.
							if (dFrameRate > 31.0) numberLines += (int)Math.round(Math.abs(dFrameRate - 30.0));
							// If our FPS is < 29, then we remove some lines.
							if (dFrameRate < 29.0) numberLines -= (int)Math.round(Math.abs(dFrameRate - 30.0));;
							
							if (numberLines > 200) { numberLines = 200; } 

							//We build the image that will display the framerate and the line count.
							imageFramerate = new MinuetoText("FPS: " + dFrameRate + " Lines: " + numberLines ,fontArial16,MinuetoColor.BLUE);

							imageText = new MinuetoText(""+mwiPanel.getWidth()+", "+mwiPanel.getHeight(),fontArial16,MinuetoColor.WHITE);
							// And we start counting the frame agains ...
							frameCount = 0;
							// ...after having properly reset the counter.
							mswWatch.reset();
							mswWatch.start();

						}

						// We display the framerate and the line count
						mwiPanel.draw(imageFramerate, 0, 0);
						mwiPanel.draw(imageText, mwiPanel.getWidth()/2-imageText.getWidth()/2, mwiPanel.getHeight()/2-imageText.getHeight()/2);

						// Render all graphics in the back buffer.			
						mwiPanel.render();
						
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}				
				}
				
				// Give some breathing space to the other threads
				Thread.yield();	
			}

			mwiPanel.close();
			dispose();
		}

		public void handleKeyPress(int value) {
			output.println(id + " Keyboard key " + value + " was pressed.");

			if (value == MinuetoKeyboard.KEY_Q) { System.exit(0); }
		}

		public void handleKeyRelease(int value) {
			output.println(id + " Keyboard key " + value + " was released.");		
		}

		public void handleKeyType(char keyChar) {

		}

		public void handleMouseMove(int x, int y) {	

		}

		public void handleMousePress(int x, int y, int button) {
			output.println(id + " Mouse click on button " + button + " detected at " + x + "," + y);		
		}

		public void handleMouseRelease(int x, int y, int button) {
			output.println(id + " Mouse release on button " + button + " detected at " + x + "," + y);	
		}

		public void handleGetFocus() {
			output.println(id + " Window got focus!");
		}

		public void handleLostFocus() {
			output.println(id + " Window lost focus!");		
		}
	}
		
	private class OutputWindow extends JInternalFrame {
		
		private static final long serialVersionUID = -7381960907105493507L;
		
		JList list;
		DefaultListModel listModel;
		
		public OutputWindow() {
			super("Output Window");
			
			setResizable(true);
			setLayout(new FlowLayout());
			setMaximizable(true);
			setSize(new Dimension(800, 300));
			
	        listModel = new DefaultListModel();

	        //Create the list and put it in a scroll pane.
	        list = new JList(listModel);
	        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	        list.setSelectedIndex(0);
	        list.setVisibleRowCount(5);
	        list.setAutoscrolls(true);
	        JScrollPane listScrollPane = new JScrollPane(list);
	        this.setContentPane(listScrollPane);	        
	        this.setVisible(true);
		}
		
		public void println(String text) {
			listModel.addElement(text);
			int lastIndex = listModel.getSize() - 1;
			if (lastIndex >= 0) {
				list.ensureIndexIsVisible(lastIndex);
			}
		}
	}

	public void setVisible(boolean value) {
		mainFrame.setVisible(value);
	}

	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {

		PanelDemo main = new PanelDemo();
		main.setVisible(true);
	}

}