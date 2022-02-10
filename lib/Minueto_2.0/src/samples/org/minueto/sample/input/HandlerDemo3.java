package org.minueto.sample.input;
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
 
import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.MinuetoOutOfBoundException;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.handlers.MinuetoMouseWheelHandler;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;


/**
 * Sample of how the use the MouseMove event.
 *
 * This demo will draw a crosshare that will follow the movement of the mouse
 * cursor.Users can press the  'q' key to quit and the 's' key to make a 
 * screenshot.
 **/
public class HandlerDemo3 implements MinuetoKeyboardHandler, MinuetoMouseHandler, MinuetoMouseWheelHandler {
	
	private MinuetoWindow window;			// The Minueto window
	private MinuetoEventQueue eventQueue;			// The Minueto queue that will hold
												// the events.

	private int lastPositionX;					// Last known x position of the cursor
	private int lastPositionY;					// Last known y position of the cursor
	
	public HandlerDemo3() {
	
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		// Build the event queue.
		eventQueue = new MinuetoEventQueue();
		
		// Register the keyboard handler with the event queue.
		window.registerKeyboardHandler(this, eventQueue);
		// Register the mouse handler with the event queue.
		window.registerMouseHandler(this, eventQueue);
		// Register the mouse wheel handler with the event queue.
		window.registerMouseWheelHandler(this, eventQueue);

		// Set the max framerate
		window.setMaxFrameRate(30);
		
		// Show the game window.
		window.setVisible(true);

		// Initialize the last known positions.
		lastPositionX = 0;
		lastPositionY = 0;
				
		while(true) {
		
			// Clear the window
			window.clear();
						
			// Draw the crosshare.
			try {			
				window.drawLine(MinuetoColor.BLUE, lastPositionX-10, 
					lastPositionY, lastPositionX+10, lastPositionY);
				window.drawLine(MinuetoColor.BLUE, lastPositionX, 
					lastPositionY-10, lastPositionX, lastPositionY+10);			
			
			} catch (MinuetoOutOfBoundException e) {
				// This happends when we draw offscreen.			
			}
			
			
			// Handle all the events in the event queue.
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}
			
			// Render all graphics in the back buffer.
			window.render();
			
		}
		
		
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		HandlerDemo3 main = new HandlerDemo3();
	}
	
	/**
	 * Handle the keyboard input.
	 **/
	public void handleKeyPress(int value) {
		
		switch(value) {
		
			// If the user presses Q, we quit	
			case MinuetoKeyboard.KEY_Q:
				
				System.exit(0);
				break;

			default:

				// Ignore input
		}
	}
	
	public void handleKeyRelease(int value) {
		
		
	}
	
	public void handleKeyType(char keyChar) {
		
		// Not used, input ignored
	}
	
	public void handleMousePress(int x, int y, int button) {
	
		// Not used, input ignored
	}
	
	public void handleMouseRelease(int x, int y, int button) {
				
		// Not used, input ignored
	}
	
	/**
	 * Record the last know position of the cursor.
	 **/
	public void handleMouseMove(int x, int y) {		
	
		this.lastPositionX = x;
		this.lastPositionY = y;
		
	}

	public void handleMouseWheelRotate(int rotate) {
		System.out.println(rotate);
		
	}
	
}
