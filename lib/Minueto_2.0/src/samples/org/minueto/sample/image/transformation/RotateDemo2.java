package org.minueto.sample.image.transformation;
/**
 * @(#)RotateDemo2.java        1.00 01/07/2005
 *
 * Edited By: Michael A. Hawker
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
 * Sample that scales and flips an image..
 *
 * Note that scale is not an efficient operation and the results should
 * be cached.
 **/
public class RotateDemo2 {
	
	public RotateDemo2() {
		 
		MinuetoWindow window;			// The Minueto window
		
		MinuetoImage imageHex;				// Original image		
		MinuetoImage imageRotate;				// Rotated image
 	
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		
		// Load our hex image.
		try {
			imageHex = new MinuetoImageFile("images/WoodHex.png");			
		} catch (MinuetoFileException e) {
			System.out.println("Could not load tank file");
			System.exit(0);
			return;
		}

		// Rotate Image 180 degrees
		imageRotate = imageHex.rotate(Math.PI);
		
		// Show the game window.
		window.setVisible(true);
		
		// Game/rendering loop
		while(true) {
		
			// Clear the window to BLACK.
			window.clear(MinuetoColor.BLACK);
			
			// Draw the standard hexes.
			window.draw(imageHex, 50, 50);
			window.draw(imageHex, 50 + 29, 50 + 51);

			// Draw Regular and Rotated hexes.
			window.draw(imageHex, 200, 50);
			window.draw(imageRotate, 200 + 29, 50 + 51);
			
			// Render all graphics in the back buffer.
			window.render();
		}		
							
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		RotateDemo2 rd2Exemple = new RotateDemo2();
	}
	
}