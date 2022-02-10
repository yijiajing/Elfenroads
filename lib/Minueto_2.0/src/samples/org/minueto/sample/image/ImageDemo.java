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
import org.minueto.image.MinuetoRectangle;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that builds a chessboard using only built-in Minueto functions.
 **/
public class ImageDemo {
		
	public ImageDemo() {

		MinuetoWindow window;			// The Minueto window
		MinuetoImage imageChessBoard;			// Stores the chessboard.
		
		// Create a 640 by 480 window
		window = new MinuetoFrame(640, 480, true);
				
		// Build the chessboard.
		imageChessBoard = buildChessBoard(MinuetoColor.WHITE, MinuetoColor.BLACK);
		
		// Show the game window.
		window.setVisible(true);
				
		// Game/rendering loop
		while(true) {
		
			// Draw the chessboard.
			window.draw(imageChessBoard,100,20);
			
			// Render all graphics in the back buffer.
			window.render();
		}		
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		ImageDemo main = new ImageDemo();
	}
	
	/**
	 * Build a 400x440 chessboard using the two supplied colors.
	 **/
	private MinuetoImage buildChessBoard(MinuetoColor mcoFirstColor, MinuetoColor mcoSecondColor) {
		
		MinuetoImage imageTempChessBoard;		// Stores the temporary chessboard we are drawing.
		MinuetoImage imageAlternateSquare;	// Stores a tile of the second color.	
		
		// First, we make a board from the first color.
		imageTempChessBoard = new MinuetoRectangle( 440, 440, mcoFirstColor, true);
		// Then, we build ONE tile of the alternate color.
		imageAlternateSquare = new MinuetoRectangle( 55, 55, mcoSecondColor, true);
		
		// We draw the alternate tile at the appropriate places.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				
				if ((i%2) == 0) {
					if ((j%2) == 1) {
						imageTempChessBoard.draw(imageAlternateSquare, i*55, j*55);
					}
				} else {
					if ((j%2) == 0) {
						imageTempChessBoard.draw(imageAlternateSquare, i*55, j*55);
					}
				}				
			}
		}
		
		// And we return the image of the chessboard.
		return imageTempChessBoard;
		
	}
}

