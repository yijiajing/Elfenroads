package org.minueto.sample.image;
/**
 * @(#)CircleDemo.java        1.00 15/09/2004
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
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that draws Circles and Ellipses.
 *
 * Note that the Circles/Ellipses are created only once. This is much more
 * efficient then re-creating them everytime we want to draw them.
 **/
public class CircleDemo {
		
	public CircleDemo() {

		MinuetoWindow window;			// The Minueto window
		
		MinuetoImage[] imageCircles = new MinuetoImage[10];	// Stores 10 circles
		MinuetoImage[] imageEllipses = new MinuetoImage[10];	// Stores 10 ellipses
		
		
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		
		// Create our 10 circles and 10 ellipses.
		for(int i = 0; i< 10; i++) {
			
			imageCircles[i] = new MinuetoCircle((i+8)*2,new MinuetoColor(i*6, i*10, i*20), true);
			imageEllipses[i] = new MinuetoCircle((i+8)*2, (i+5)*5, new MinuetoColor(i*20, i*10, i*6), false);
		}
		
		// Show the game window.
		window.setVisible(true);
				
		// Game/rendering loop
		while(true) {
		
			// Clear the window.
			window.clear();
		
			// Draw our 10 circles and 10 ellipses.
			for(int i = 0; i< 10; i++) {	
						
				window.draw(imageCircles[i], (i*62)+5, 20);
				window.draw(imageEllipses[i], (i*62)+5, 200);
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
		CircleDemo main = new CircleDemo();
	}
}

