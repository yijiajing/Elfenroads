package org.minueto.sample.image.text;

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
 
import java.util.Random;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoRectangle;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that benchmarks the number of lines a computer can draw without
 * going under 30 frames per second. The demo will add/remove lines until
 * a stable fps of around 30 is reached.
 **/

public class FramerateDemo implements MinuetoKeyboardHandler {
	
	private MinuetoWindow window;
	private MinuetoEventQueue eventQueue;
	private int frameRate = 20;
	
	public FramerateDemo() {
		 		
		MinuetoFont fontArial16;				// Font used to write framerate.
		MinuetoImage imageFramerate;			// Text use to display framerate.
		
		MinuetoColor color[] = new MinuetoColor[5];	// Array of color
		MinuetoImage imageSquare[] = new MinuetoImage[20];
		
		int aCoordX[] = new int[20];
		int aCoordY[] = new int[20];
		
		Random ranNumGenerator;				// Our random number generator.		
				
		int randomNumber;
		
		// Create a 640 by 480 window.
		window = new MinuetoFrame(640, 480, true);
		
		eventQueue = new MinuetoEventQueue(); 
		
		// Init random number generator.
		ranNumGenerator = new Random();		
				
		// Init the font.
		fontArial16 = new MinuetoFont("Arial",16,false, false);
		
		// Prepare the color array.
		color[0] = MinuetoColor.BLACK;
		color[1] = MinuetoColor.RED;
		color[2] = MinuetoColor.GREEN;
		color[3] = MinuetoColor.BLUE;
		color[4] = MinuetoColor.WHITE;
									
		for (int i = 0; i < 20; i++) {
			imageSquare[i] = new MinuetoRectangle( 
				ranNumGenerator.nextInt(99)+1 , 
				ranNumGenerator.nextInt(99)+1 , 
				color[ranNumGenerator.nextInt(5)],
				true );
				
			aCoordX[i] = ranNumGenerator.nextInt(560);
			aCoordY[i] = ranNumGenerator.nextInt(400);
			
		}
		
		this.window.setMaxFrameRate(frameRate);

		this.window.registerKeyboardHandler(this, this.eventQueue);
		
		// Show the game window.
		window.setVisible(true);
					
		// Game/Rendering loop	
		while(true) {
		
			this.window.clear();
			
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}
			
			randomNumber = ranNumGenerator.nextInt(20);
			
			// For every lines we are supposed to draw:
			for(int i = 0; i < 20; i++) {
				this.window.draw(imageSquare[i], aCoordX[(i+randomNumber)%20], aCoordY[(i+randomNumber*2+1)%20]);
			}
			
			imageFramerate = new MinuetoText("Max FPS: " + frameRate + " Current FPS: " + window.getFrameRate(), fontArial16, MinuetoColor.BLUE);
			
			// We display the framerate and the line count
			this.window.draw(imageFramerate, 0, 0);
			
			// Render all graphics in the back buffer.			
			this.window.render();
			
			Thread.yield();
		}
		
		
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		FramerateDemo main = new FramerateDemo();
	}
	
	public void handleKeyPress(int value) {
		
		
	}
	
	public void handleKeyType(char keyChar) {
		
		
	}
	
	public void handleKeyRelease(int value) {
		
		switch (value) {
			case MinuetoKeyboard.KEY_UP:
			
				this.frameRate++;
				window.setMaxFrameRate(frameRate);
				break;
				
			case MinuetoKeyboard.KEY_DOWN:
			
				this.frameRate--;
				if (this.frameRate < 1) this.frameRate = 1;
				window.setMaxFrameRate(frameRate);
				break;
			
			case MinuetoKeyboard.KEY_LEFT:
			
				this.frameRate = this.frameRate - 10;
				if (this.frameRate < 1) this.frameRate = 1;
				window.setMaxFrameRate(frameRate);
				break;
				
			case MinuetoKeyboard.KEY_RIGHT:
			
				this.frameRate = this.frameRate + 10;				
				window.setMaxFrameRate(frameRate);
				break;
				
			default:
				
		}
	}
			
}
