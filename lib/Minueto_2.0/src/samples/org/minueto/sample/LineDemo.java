package org.minueto.sample;
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
 
import org.minueto.MinuetoColor;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that draws a few lines.
 **/
public class LineDemo {
		
	public LineDemo() {

		MinuetoWindow window;			// The Minueto window
				
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		
			// Show the game window.
		window.setVisible(true);
				
		// Game/rendering loop
		while(true) {
		
			// Clear the window.
			window.clear();
		
			// Draw 10 horizontal lines and 10 vertical lines.
			for(int i = 0; i< 11; i++) {	
						
				window.drawLine(MinuetoColor.BLUE, 5+(i*60), 5, 5+(i*60), 405 );
				window.drawLine(MinuetoColor.GREEN, 5, 5+(i*40), 605, 5+(i*40) );				
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
		LineDemo main = new LineDemo();
	}
}

