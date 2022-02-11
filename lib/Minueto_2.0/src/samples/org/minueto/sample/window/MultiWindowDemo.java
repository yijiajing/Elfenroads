package org.minueto.sample.window;
/**
 * @(#)HandlerDemo2.java        1.00 15/09/2004
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
 
import java.util.Random;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;


/**
 * Sample of multiple window functionnality. This demo is just a multi
 * window version of HandlerDemo2.
 *
 * This demo will print events to the console. Users can press the 
 * 'q' key to quit.
 **/

public class MultiWindowDemo extends Thread  {
		
	String name;
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
				
		MultiWindowDemo win1 = new MultiWindowDemo("Windows 1");
		win1.start();
		
		MultiWindowDemo win2 = new MultiWindowDemo("Windows 2");
		win2.start();
				
		MultiWindowDemo win3 = new MultiWindowDemo("Windows 3");
		win3.start();		
	}
	
	public MultiWindowDemo(String name) {
		this.name = name; 
	
	}
	
	public void run() {
		
		@SuppressWarnings("unused")
		WindowForDemo win1 = new WindowForDemo(this.name);
	}
}

class WindowForDemo implements MinuetoKeyboardHandler,
									 MinuetoMouseHandler,
									 MinuetoWindowHandler {
	
	MinuetoFrame window;			// The Minueto window		
	MinuetoEventQueue eventQueue;			// The Minueto queue that will hold
										// the events.
	
	boolean closing;
	
	String name;
												
	public WindowForDemo(String name) {
			
		MinuetoFont fontArial19;				// Font used to draw on the screen.
		MinuetoImage imageText, imageText2;		// Instructions drawn on the screen.
		MinuetoImage imageBackground;			// Background for this demo
		
		Random random = new Random();
		
		this.name = name;
		
		this.closing = false;
		
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		// Build the event queue.
		eventQueue = new MinuetoEventQueue();
		
		// Register the keyboard handler with the event queue.
		window.registerKeyboardHandler(this, eventQueue);
		// Register the mouse handler with the event queue.
		window.registerMouseHandler(this, eventQueue);
		// Register the window handler with the event queue.
		window.registerWindowHandler(this, eventQueue);

		// Disable the exit on close
		window.exitOnClose(false);
		
		// Initialize an Arial font of size 19, not bold and not italic.
		fontArial19 = new MinuetoFont("Arial",19,false, false);
		
		// Build the background for this demo.
		imageBackground = new MinuetoRectangle(400, 300, new MinuetoColor(0,0,128), false);
		imageBackground.drawLine(new MinuetoColor(0,0,128), 0,0,399,299);
		imageBackground.drawLine(new MinuetoColor(0,0,128), 0,299,399,0);
		
		// Build images of the demo instructions.
		imageText = new MinuetoText("Event messages are printed in the console window." ,fontArial19,MinuetoColor.BLUE);
		imageText2 = new MinuetoText("Press 'q' to quit." ,fontArial19,MinuetoColor.BLUE);

		// Show the game window.
		window.setVisible(true);
				
		window.setTitle(this.name);
		window.setWindowPosition(random.nextInt(100),random.nextInt(100));
		
		// Game/rendering loop
		while(true) {
		
			// Clear the window.
			window.clear();
			
			// Draw the background.
			window.draw(imageBackground, 0, 0);
			
			// Draw the instructions.						
			window.draw(imageText, 0, 0);
			window.draw(imageText2, 0, 30);
			
			// Handle all the events in the event queue.
			while (eventQueue.hasNext()) {
				eventQueue.handle();
				if (closing) return;
			}
			
			// Render all graphics in the back buffer.
			window.render();
			
			// Give some breathing space to the other threads
			Thread.yield();			
		}		
	}
	
	/**
	 * Print out the key values when the user presses a key. Constants to
	 * identify key values are available in the MinuetoKeyboard class.
	 **/
	public void handleKeyPress(int value) {
		
		System.out.println(this.name + ": Keyboard key " + value + " was pressed.");
		
		switch(value) {
		
			case MinuetoKeyboard.KEY_Q:
				this.closing = true;
				window.close();
				break;
			case MinuetoKeyboard.KEY_W:
				window.setCursorVisible(true);
				break;
			case MinuetoKeyboard.KEY_E:
				window.setCursorVisible(false);
				break;
			case MinuetoKeyboard.KEY_A:
				window.registerMouseHandler(this, eventQueue);
				break;
			case MinuetoKeyboard.KEY_S:
				window.unregisterKeyboardHandler(this, eventQueue);
				break;
		}

	}
	
	/**
	 * Print out the key values when the user releases a key. Constants to
	 * identify key values are available in the MinuetoKeyboard class.
	 **/
	public void handleKeyRelease(int value) {
		
		System.out.println(this.name + ": Keyboard key " + value + " was released.");
	}
	
	/**
	 * Not used. Mostly used to implement typing stuff, such as text box.
	 */
	public void handleKeyType(char keyChar) {
		
		
	}
	
	/**
	 * Print out the mouse button values and mouse location when the user 
	 * presses a mouse button. Constants to identify mouse button values are 
	 * available in the MinuetoMouse class.
	 **/
	public void handleMousePress(int x, int y, int button) {
	
		System.out.println(this.name + ": Mouse click on button " + button + " detected at " + x + "," + y);	
	}
	
	/**
	 * Print out the mouse button values and mouse location when the user 
	 * releases a mouse button. Constants to identify mouse button values are 
	 * available in the MinuetoMouse class.
	 **/
	public void handleMouseRelease(int x, int y, int button) {
		
		System.out.println(this.name + ": Mouse release on button " + button + " detected at " + x + "," + y);
	}
	
	/**
	 * This method is called to handle mouse move events. We are not printing
	 * any message since a LOT of these events are generated when the mouse
	 * is moved.
	 **/
	public void handleMouseMove(int x, int y) {		
	
		// Not going to print on this event.
	}

	/**
	 * Prints out a message when the MinuetoWindow gets the focus.
	 **/
	public void handleGetFocus() {
	
		System.out.println(this.name + ": Window got focus.");
	}
	
	/**
	 * Prints out a message when the MinuetoWindow loses the focus.
	 **/
	public void handleLostFocus() {
	
		System.out.println(this.name + ": Window lost focus.");
	}
	
	/**
	 * Prints out a message when the user tries to quit the application (by
	 * pressing the X in the top right corner, pressing Alt-F4, etc).
	 **/
	public void handleQuitRequest() {
	
		System.out.println(this.name + ": Window got a quit request.");
		this.closing = true;
		window.close();
	}
	
	/**
	 * Prints out a message when the user minimizes the window.
	 **/
	public void handleMinimizeWindow() {
	
		System.out.println(this.name + ": Window was minimized.");
	}
	
	/** 
	 * Prints out a message when the user restores a window (from a minimize).
	 **/
	public void handleRestoreWindow() {
	
		System.out.println(this.name + ": Window was restore.");
	}
		
}
