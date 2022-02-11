package org.minueto.sample.swing.launch;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

public class GameWindow  implements MinuetoWindowHandler {

	// The event queue
	private MinuetoEventQueue eventQueue;		// The Minueto queue that will hold

	// Window to draw stuff to
	private MinuetoWindow window;			// The Minueto window

	// Font used to draw on the screen
	private MinuetoFont font;
	
	// Images of text
	private MinuetoImage imageNormalText[] = new MinuetoImage[8];			
	private MinuetoImage imageAAText[] = new MinuetoImage[8];
	
	// Should be keep the window open
	private boolean open;
	
	public GameWindow() {
		
		this.window = new MinuetoFrame(800, 600, true);
	}
	
	private void initialize() {
		// Build the event quue.
		eventQueue = new MinuetoEventQueue();
		
		// Register the window handler with the event queue.
		this.window.registerWindowHandler(this, eventQueue);		
		
		// Build the images of the text
		for (int i = 0; i < 8; i++) {
			int fontSize = i * 4 + 10;
			font = new MinuetoFont("Arial",fontSize,false, false);
			imageNormalText[i] = new MinuetoText("Arial normal " + fontSize  ,font,MinuetoColor.WHITE);
			imageAAText[i] = new MinuetoText("Arial anti-aliased " + fontSize ,font,MinuetoColor.WHITE, true);		
		}
		
		this.window.setVisible(true);
		this.window.setTitle("Minueto From Swing");
	}

	public void start() {		
		initialize();
		
		// Keep window open
		open = true;
		
		// Game/rendering loop
		while(open) {
		
			int n = 5;
			// Clear the window.
			this.window.clear(MinuetoColor.BLACK);
			
			// Draw the text on the screen.
			for (int i = 0; i < 8; i++) {
				this.window.draw(imageNormalText[i], 0, n);			
				this.window.draw(imageAAText[i], 320, n);			
				n = n + imageNormalText[i].getHeight() + 3;
			}
			
			// Handle all the events in the event queue.
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}
			
			// Render all graphics in the back buffer.
			this.window.render();
		}	
		
		// Close our window
		this.window.close();
	}

	public void handleGetFocus() {
		// TODO Auto-generated method stub
		
	}

	public void handleLostFocus() {
		// TODO Auto-generated method stub
		
	}

	public void handleMinimizeWindow() {
		// TODO Auto-generated method stub
		
	}

	public void handleQuitRequest() {
		// We receive an event about closing the window
		// This'll stop our thread in run()
		this.open = false;		
	}

	public void handleRestoreWindow() {
		// TODO Auto-generated method stub
		
	}
}
