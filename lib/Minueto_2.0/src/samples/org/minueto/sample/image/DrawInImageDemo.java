package org.minueto.sample.image;
/**
 * @(#)ImageDemo.java        1.00 15/09/2004
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
import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that demos some of the internal drawing functions in MinuetoImage.
 **/
public class DrawInImageDemo {
		
	public DrawInImageDemo() {

		MinuetoWindow window;			// The Minueto window
		MinuetoImage imageSurface;			// Stores the drawing surface.

		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
				
		// Create a drawing surface
		imageSurface = new MinuetoImage(640, 480);
		imageSurface.clear(MinuetoColor.WHITE);
		
		// Draw on the surface
		imageSurface.drawRectangle(MinuetoColor.BLUE, 10, 10, 100, 100);
		imageSurface.drawCircle(MinuetoColor.BLUE, 250, 10, 100);
		imageSurface.drawPolygon(MinuetoColor.BLUE, new int[]{10, 300, 100,300, 50, 400} );
		
		// Show the game window.
		window.setVisible(true);
				
		// Game/rendering loop
		while(true) {
		
			// Draw the chessboard.
			window.draw(imageSurface,0,0);
			
			// Render all graphics in the back buffer.
			window.render();
		}		
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		DrawInImageDemo main = new DrawInImageDemo();
	}
}

