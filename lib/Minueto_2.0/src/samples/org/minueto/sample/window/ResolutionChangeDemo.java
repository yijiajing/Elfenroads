package org.minueto.sample.window;
/**
 * @(#)ResolutionChangeDemo.java        1.00 15/09/2004
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
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoFullscreen;
import org.minueto.window.MinuetoWindow;

/**
 * Sample that allows you to create MinuetoWindows in different resolution.
 *
 * Note that this demo is unstable when run on Java 1.4.* because of a
 * bug in the JVM.
 **/
public class ResolutionChangeDemo implements MinuetoKeyboardHandler {
	
	private MinuetoWindow window;			// The Minueto window
	private MinuetoEventQueue eventQueue;			// The Minueto queue that will hold
												// the events.
				
	public ResolutionChangeDemo() {
		
		MinuetoFont fontArial14;						 // Font used to draw on the screen.
		MinuetoText[] imageText = new MinuetoText[14]; // Instructions drawn on the screen.

		// Build the event queue.
		eventQueue = new MinuetoEventQueue();		
		// Create a 640 by 480 window
		initWindow(640, 480, 32, false, true);
				
		// Register the keyboard handler with the event queue.
		window.registerKeyboardHandler(this, eventQueue);

		// Initialize an Arial font of size 14, not bold and not italic.
		fontArial14 = new MinuetoFont("Arial",14,false, false);
		
		// Build images of the demo instructions.
		imageText[0] = new MinuetoText("Press the following keys to change the resolution." ,fontArial14,MinuetoColor.BLUE);
		imageText[1] = new MinuetoText("1 : 400x300 Windowed - Bordeless" ,fontArial14,MinuetoColor.BLUE);
		imageText[2] = new MinuetoText("2 : 640x480 Windowed" ,fontArial14,MinuetoColor.BLUE);
		imageText[3] = new MinuetoText("3 : 800x600 Windowed" ,fontArial14,MinuetoColor.BLUE);
		imageText[4] = new MinuetoText("4 : 1024x768 Windowed" ,fontArial14,MinuetoColor.BLUE);
		imageText[5] = new MinuetoText("5 : 1280x1024 Windowed" ,fontArial14,MinuetoColor.BLUE);
		imageText[6] = new MinuetoText("6 : 1600x1200 Windowed" ,fontArial14,MinuetoColor.BLUE);
		imageText[7] = new MinuetoText("7 : 640x480 Fullscreen" ,fontArial14,MinuetoColor.BLUE);
		imageText[8] = new MinuetoText("8 : 800x600 Fullscreen" ,fontArial14,MinuetoColor.BLUE);
		imageText[9] = new MinuetoText("9 : 1024x768 Fullscreen" ,fontArial14,MinuetoColor.BLUE);
		imageText[10] = new MinuetoText("0 : 1280x1024 Fullscreen" ,fontArial14,MinuetoColor.BLUE);
		imageText[11] = new MinuetoText("Press 'q' to quit." ,fontArial14,MinuetoColor.BLUE);
		imageText[12] = new MinuetoText("Note that this demo is unstable  in fullscreen mode when run" ,fontArial14,MinuetoColor.BLUE);
		imageText[13] = new MinuetoText("on Java 1.4.* because of a bug in the JVM." ,fontArial14,MinuetoColor.BLUE);
		 	
		// Show the game window.
		window.setVisible(true);
				
		// Game/rendering loop
		while(true) {
				
			// Clear the window.
			window.clear();
		
			// Draw the instructions.						
			for (int i = 0; i < 14; i++) {
				window.draw(imageText[i], 5, (i*15)+5);	
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
	 * Create a new MinuetoWindow at a different resolution.
	 **/
	public void initWindow(int width, int height, int iColorDepth, boolean bFullscreen, boolean border) {
				
		if (window != null) {
			window.close();
			window = null;
		}
		
		if (bFullscreen) {
			window = new MinuetoFullscreen(width, height, iColorDepth);
		} else {
			window = new MinuetoFrame(width, height, border);
		}
		
		window.registerKeyboardHandler(this, eventQueue);
		window.setVisible(true);
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		ResolutionChangeDemo recExample = new ResolutionChangeDemo();
	}
	
	/**
	 * Handle the keyboard input.
	 **/
	public void handleKeyPress(int value) {
		
		switch(value) {
			
			case MinuetoKeyboard.KEY_1:
				initWindow(400,300,32,false, false);
				break;
			case MinuetoKeyboard.KEY_2:
				initWindow(640,480,32,false, true);
				break;
			case MinuetoKeyboard.KEY_3:
				initWindow(800,600,32,false, true);
				break;
			case MinuetoKeyboard.KEY_4:
				initWindow(1024,768,32,false, true);
				break;
			case MinuetoKeyboard.KEY_5:
				initWindow(1280,1024,32,false, true);
				break;
			case MinuetoKeyboard.KEY_6:
				initWindow(1600,1200,32,false, true);
				break;
			case MinuetoKeyboard.KEY_0:
				initWindow(1280,1024,32,true, true);
				break;
			case MinuetoKeyboard.KEY_7:
				initWindow(640,480,32,true, true);
				break;
			case MinuetoKeyboard.KEY_8:
				initWindow(800,600,32,true, true);
				break;
			case MinuetoKeyboard.KEY_9:
				initWindow(1024,768,32,true, true);
				break;
			case MinuetoKeyboard.KEY_Q:
				System.exit(0);
				break;
			default:
				// Ignore input
		}
	}
	
	public void handleKeyRelease(int value) {
		
		// Ignore input
	}	
	
	public void handleKeyType(char keyChar) {
		
		// Ignore input			
	}
	
}
