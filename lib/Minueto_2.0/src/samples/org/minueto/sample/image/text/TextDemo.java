package org.minueto.sample.image.text;
/**
 * @(#)TextDemo.java        1.00 07/01/2005
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
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * This demo illustrates the different between normal and anti-aliased text..
 **/
public class TextDemo {
		
	public TextDemo() {

		MinuetoWindow window;			// The Minueto window
		MinuetoEventQueue eventQueue;		// The Minueto queue that will hold
													// the events.
		MinuetoFont font;					// Font used to draw on the screen.
													// Images of text
		MinuetoImage imageNormalText[] = new MinuetoImage[8];			
		MinuetoImage imageAAText[] = new MinuetoImage[8];
		
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		// Build the event queue.
		eventQueue = new MinuetoEventQueue();
		
		// Build the images of the text
		for (int i = 0; i < 8; i++) {
			int fontSize = i * 4 + 10;
			font = new MinuetoFont("Arial",fontSize,false, false);
			imageNormalText[i] = new MinuetoText("Arial normal " + fontSize  ,font,MinuetoColor.WHITE);
			imageAAText[i] = new MinuetoText("Arial anti-aliased " + fontSize ,font,MinuetoColor.WHITE, true);
				
		}
		
		// Show the game window.
		window.setVisible(true);
				
		// Game/rendering loop
		while(true) {
		
			int n = 5;
			// Clear the window.
			window.clear(MinuetoColor.BLACK);
			
			// Draw the text on the screen.
			for (int i = 0; i < 8; i++) {
				window.draw(imageNormalText[i], 0, n);			
				window.draw(imageAAText[i], 320, n);			
				n = n + imageNormalText[i].getHeight() + 3;
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
		TextDemo main = new TextDemo();
	}
}

