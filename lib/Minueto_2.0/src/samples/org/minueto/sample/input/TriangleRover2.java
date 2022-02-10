package org.minueto.sample.input;
/**
 * @(#)RotateDemo.java        1.00 15/09/2004
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
import org.minueto.MinuetoFileException;
import org.minueto.MinuetoStopWatch;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoImageFile;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Sample of a triangle vehicule that moves around a 2D Map. This sample is
 * a big more complicated than the others and covers a wide amount of topics.
 *
 * This sample is similar to TriangleRover, but uses loaded images for the 
 * background and the sprite.
 **/
public class TriangleRover2 implements MinuetoKeyboardHandler {
	
	private MinuetoWindow window;		// Our Window
	private MinuetoEventQueue eventQueue;		// Our Event Queue
	private MinuetoImage imageRover;			// The image of our rover
	private MinuetoImage imageBackground;		// The background of our image

	private boolean keyUp;					// Is the up key pressed?
	private boolean keyDown;				// Is the down key pressed?
	private boolean keyLeft;				// Is the left key pressed?
	private boolean keyRight;				// Is the right key pressed?
		
	public TriangleRover2() {
			
		MinuetoText imageFramerate;			// Image used to draw the framerate
		MinuetoStopWatch stopWatch;			// Timer used to find framerate
		MinuetoStopWatch stopWatchFrameLock;		// Timer used to lock application to
											// a particular frame rate.
		MinuetoFont fontArial19;				// Font used to write framerate
		MinuetoImage imageTempFloor;			// Temp image used to build background.
		MinuetoImage imageTempRover;
		
		int frameCount;					// Used to calculate framerate	
		double frameRate;					// Current framerate
		
		double positionX = 320;			// Current X position of rover
		double positionY = 240;			// Current Y position of rover
		
		double accelerationF = 0;			// Current forward acceleration
		double accelerationT = 0;			// Current backward acceleration
		
		double orientation = Math.PI / 2.0; // Current orientation
			
		// Build the event queue		
		eventQueue = new MinuetoEventQueue();
		// Build the minueto window
		window = new MinuetoFrame(640, 480, true);
		// Register the keyboard handler
		window.registerKeyboardHandler(this, eventQueue);
		
		// Load the image for the background and the rover.
		try {
			imageRover = new MinuetoImageFile("images/Rover.png");			
			imageTempFloor = new MinuetoImageFile("images/FloorTile.png");			
		} catch (MinuetoFileException e) {
			System.out.println("Could not load png files");
			System.exit(0);
			return;
		}
		
		// Prepare a blank image for the background.
		imageBackground = new MinuetoImage(640,480);
		
		// Draw the floor tile over the background. 
		for (int i = 0; i < 640; i = i + 50) {
			for(int j = 0; j < 480; j = j + 50) {
				imageBackground.draw(imageTempFloor, i,j);				
			}
		}
		
		// Initialize the timer for both the framelock and the framerate
		stopWatch = new MinuetoStopWatch();
		stopWatchFrameLock = new MinuetoStopWatch();
		
		// Initialize the front
		fontArial19 = new MinuetoFont("Arial",19,false, false);
								
		// Build the framerate image					
		imageFramerate = new MinuetoText("FPS: 0",fontArial19, MinuetoColor.BLUE);

		// Start the timer for both the framelock and the framerate
		stopWatchFrameLock.start();
		stopWatch.start();
		frameCount = 0;
		
		// Show the window
		window.setVisible(true);
				
		while(true) {
		
			// Locks the framerate to 50 FPS. This helps use calibrate the speed
			// the rover. We can achieve the framelock by forcing the system
			// to wait 20 millisec between each update.
			while (stopWatchFrameLock.getTime() < 20) {
			
				try {
					Thread.sleep(2);
				} catch (Exception e) { };
				
			} 			
			
			// Reset and restart the framelock timer.
			stopWatchFrameLock.reset();
			stopWatchFrameLock.start();
			
			// Handle all events (keyboard input) in the event queue.
			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}
			
			// If the up or down key is pressed, adjust the foward acceleration.
			if (this.keyUp || this.keyDown) { 				
				if (this.keyUp) { accelerationF = 3.5; }
				if (this.keyDown) { accelerationF = -2.0; }
			} else { accelerationF = 0; }
			
			// If the left or right key is pressed, adjust the turning acceleration.
			if (this.keyLeft || this.keyRight) { 
				if (this.keyLeft) { accelerationT = accelerationT - 0.005; }
				if (this.keyRight) { accelerationT = accelerationT  + 0.005; }
			} else { accelerationT = 0; }
			
			// Adjust the orientation of the rover
			orientation = orientation + accelerationT;			
			
			// Move the rover in the x and y axis depending on orientation and speed.
			positionX = positionX + accelerationF * Math.cos(orientation);
			positionY = positionY + accelerationF * Math.sin(orientation);
			
			// Boud check (up and down)
			if (positionX < 24.0) { positionX = 24; orientation = orientation - (Math.PI / 2.0); }
			if (positionX > 616.0) { positionX = 616; orientation = orientation - (Math.PI / 2.0); }
			
			// Boud check (left and right)
			if (positionY < 24.0) { positionY = 24; orientation = orientation - (Math.PI / 2.0); }
			if (positionY > 456.0) { positionY = 456; orientation = orientation - (Math.PI / 2.0); }
											
			// Draw the background.	
			window.draw(imageBackground, 0, 0);
			
			// Draw the rover
			imageTempRover = imageRover.rotate(orientation+(Math.PI / 2.0));
			window.draw(imageTempRover, 
				(int)Math.round(positionX)-(imageTempRover.getWidth()/2), 
				(int)Math.round(positionY)-(imageTempRover.getHeight()/2));
			
			// Increment the framecount
			frameCount++;			
			// Update the framerate
			if (frameCount == 20) {
				stopWatch.stop();
				
				frameRate =  (double)20000/(double)stopWatch.getTime();
				
				imageFramerate = new MinuetoText("FPS: " + frameRate ,fontArial19,MinuetoColor.BLUE);
				frameCount = 0;
				stopWatch.reset();
				stopWatch.start();

			}
			
			// Draw the framerate
			window.draw(imageFramerate, 0, 0);
			
			// Draw the content of the backbuffer.
			window.render();
		}	
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		TriangleRover2 main = new TriangleRover2();
	}
	
	/**
	 * Handle the keyboard output.
	 **/
	public void handleKeyPress(int value) {
		
		switch (value) {
			case MinuetoKeyboard.KEY_UP:
				this.keyUp = true;
				break;
			case MinuetoKeyboard.KEY_LEFT:
				this.keyLeft = true;
				break;
			case MinuetoKeyboard.KEY_RIGHT:
				this.keyRight = true;
				break;
			case MinuetoKeyboard.KEY_DOWN:
				this.keyDown = true;
				break;
			case  MinuetoKeyboard.KEY_Q:
				System.exit(-1);
				break;
			default:
				System.out.println(value);
		}		
	}
	
	/**
	 * Handle the keyboard input. In the case of the program, it's important
	 * to keep track of the key release since we are tracking which key is 
	 * pressed and which has been released.
	 **/	
	public void handleKeyRelease(int value) {
		
		switch (value) {
			case MinuetoKeyboard.KEY_UP:
				this.keyUp = false;
				break;
			case MinuetoKeyboard.KEY_LEFT:
				this.keyLeft = false;
				break;
			case MinuetoKeyboard.KEY_RIGHT:
				this.keyRight = false;
				break;
			case MinuetoKeyboard.KEY_DOWN:
				this.keyDown = false;
				break;
			default:
				System.out.println(value);
		}
	}
	
	/**
	 * Ignored for this demo. Mostly used for text boxes.
	 */
	public void handleKeyType(char keyChar) {
		
		// Ignore input			
	}
	
}
