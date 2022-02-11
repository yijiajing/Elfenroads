package org.minueto.sample.image.transformation;
/**
 * @(#)ScaleDemo.java        1.00 15/09/2004
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
import org.minueto.image.MinuetoCircle;
import org.minueto.image.MinuetoImage;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that scales an image..
 *
 * Note that scale is not an efficient operation and the results should
 * be cached.
 **/
public class CropDemo {
	
	public CropDemo() {
		 
		MinuetoWindow window;			// The Minueto window
		
		MinuetoImage imageCircle;				// Original image
		MinuetoImage imageCircle2;
		MinuetoImage[] imageParts	= new MinuetoImage[4];
		
		int i = 0;						// Move factor		
	
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
		
		// Build our circle
		imageCircle = new MinuetoCircle(250,250,new MinuetoColor(255,255,0),true);
		imageCircle2 = new MinuetoCircle(250,250,new MinuetoColor(255,215,0),true);

		try {
			imageCircle.save("test.png");
		}
		catch ( MinuetoFileException mfe ) {}
		
		// Cut the circle into 4 components.
		imageParts[0] = imageCircle.crop(0,0,125,125);
		imageParts[1] = imageCircle.crop(124,0,125,125);
		imageParts[2] = imageCircle.crop(0,124,125,125);
		imageParts[3] = imageCircle.crop(124,124,125,125);
		
		// Show the game window.
		window.setVisible(true);
		
		// Game/rendering loop
		while(true) {
			
			// Clear the window.
			window.clear(MinuetoColor.BLUE);
			
			//Draw the middle circle
			window.draw(imageCircle2, 150, 150);
			
			// Draw the four parts of circles.
			window.draw(imageParts[0], 150-i/4, 150-i/4);
			window.draw(imageParts[1], 275+i/4, 150-i/4);
			window.draw(imageParts[2], 150-i/4, 275+i/4);
			window.draw(imageParts[3], 275+i/4, 275+i/4);
			
			// Render all graphics in the back buffer.
			window.render();
			
			// Change the move factor. Makes the demo dynamic.
			i = (i+1)%200;
		}		
							
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		CropDemo scaExemple = new CropDemo();
	}
	
}
