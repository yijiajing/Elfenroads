/**
 * @(#)HelloWorld.java        1.00 15/09/2004
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
 
package org.minueto.sample;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Your typical HelloWorld demo.
 **/
public class HelloWorld {
		
	public HelloWorld() {

		MinuetoWindow window;			// The Minueto window
		MinuetoEventQueue eventQueue;			// The Minueto queue that will hold
											// the events.
		MinuetoFont fontArial19;				// Font used to draw on the screen.
		MinuetoImage imageHelloWorld;			// Instructions drawn on the screen.
		
		
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		// Build the event queue.
		eventQueue = new MinuetoEventQueue();
		
		// Initialize an Arial font of size 19, not bold and not italic.
		fontArial19 = new MinuetoFont("Arial",19,false, false);
		
		// Build an image with the helloworld text.
		imageHelloWorld = new MinuetoText("HelloWorld" ,fontArial19,MinuetoColor.BLUE);
		
		// Show the game window.
		window.setVisible(true);
				
		// Game/rendering loop
		while(true) {
		
			// Clear the window.
			window.clear();
			
			// Draw the helloworld image centered on itself.			
			window.draw(imageHelloWorld, 320 - (imageHelloWorld.getWidth()/2), 240 - (imageHelloWorld.getHeight()/2));			
			
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
		HelloWorld main = new HelloWorld();
	}
}

