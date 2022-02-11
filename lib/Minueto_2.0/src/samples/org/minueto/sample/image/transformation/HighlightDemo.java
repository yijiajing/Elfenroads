package org.minueto.sample.image.transformation;
/**
 * @(#)LoadingFileDemo.java        1.00 15/09/2004
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
import org.minueto.MinuetoFileException;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that scales an image..
 *
 * Note that scale is not an efficient operation and the results should
 * be cached.
 **/
public class HighlightDemo {
	
	public HighlightDemo() {
		 
		MinuetoWindow window;			// The Minueto window
		
		MinuetoImage imageTank;				// MinuetoImage used to store
											// the image we will load.		
		
		MinuetoImage highLight;				// MinuetoImage used to store
											// the highlight.
		
		double positionX;					// Position to draw the image.
		
		int offsetX;						// X offset for the highlight
		int offsetY;						// Y offset for the highlight
		
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		
		// Load our image.
		try {
			imageTank = new MinuetoImageFile("images/tank.png");			
		} catch (MinuetoFileException e) {
			System.out.println("Could not load tank file");
			System.exit(0);
			return;
		}
		
		highLight = imageTank.mask(MinuetoColor.YELLOW);		
		highLight = highLight.scale(1.2, 1.2);
		
		offsetX = (highLight.getWidth() - imageTank.getWidth()) / 2;
		offsetY = (highLight.getHeight() - imageTank.getHeight()) / 2;
		
		// Starting position of our image.
		positionX = 0;
		
		// Show the game window.
		window.setVisible(true);
		
		// Game/rendering loop
		while(true) {
		
			// Clear the window.
			window.clear(MinuetoColor.BLACK);
		
			// Move the image at every frame.
			positionX = positionX + 0.05;
			if (positionX > 639) {positionX = 0;}			
			
			// Draw the image.
			window.draw(highLight, (int)positionX-offsetX, 50-offsetY);			
			window.draw(imageTank, (int)positionX, 50);
			
			// Render all graphics in the back buffer.
			window.render();
		}		
							
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		HighlightDemo lfdExemple = new HighlightDemo();
	}
	
}
