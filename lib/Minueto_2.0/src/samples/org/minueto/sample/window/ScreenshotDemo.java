package org.minueto.sample.window;
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
import org.minueto.MinuetoFileException;
import org.minueto.MinuetoStopWatch;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.image.MinuetoFont;
import org.minueto.image.MinuetoImage;
import org.minueto.image.MinuetoText;
import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoWindow;

/**
 * Demonstrates capturing a screenshot of your MinuetoWindow
 * 
 * Click on the screen to save a screenshot to a file
 * 
 * @author Michael A. Hawker
 * @since 1.1
 * @version 1.0
 **/
public class ScreenshotDemo implements MinuetoMouseHandler {
		
	private MinuetoWindow window;
	private MinuetoEventQueue eventQueue;
	
	private int filenum = 1;
	
	public ScreenshotDemo() {		
		MinuetoFont fontArial16;				// Font used to write framerate.
		MinuetoImage imageFramerate;			// Text use to display framerate.
		MinuetoStopWatch mswWatch;			// Timer used to calculate framerate.
		MinuetoColor color[] = new MinuetoColor[5];	// Array of color.
		
		Random ranNumGenerator;				// Our random number generator.
		
		double dFrameRate;					// Current framerate.
		int numberLines;					// Current number of lines drawn on screen.
		int frameCount;					// Used to calculate the framerate
		
		int i,j,k,x,y,c;					// Temporary integers.
		
		// Create a 640 by 480 window.
		window = new MinuetoFrame(640, 480, true);
		
		// Setup Mouse Handler
		eventQueue = new MinuetoEventQueue();
		window.registerMouseHandler(this, eventQueue);
		
		// Init random number generator.
		ranNumGenerator = new Random();		
		
		// Init the timer.
		mswWatch = new MinuetoStopWatch();
		
		// Init the font.
		fontArial16 = new MinuetoFont("Arial",16,false, false);
		
		// Prepare the color array.
		color[0] = MinuetoColor.BLACK;
		color[1] = MinuetoColor.RED;
		color[2] = MinuetoColor.GREEN;
		color[3] = MinuetoColor.BLUE;
		color[4] = MinuetoColor.WHITE;
									
		// Init the framerate text (so it's not null on the first frames).		
		imageFramerate = new MinuetoText("FPS: 0",fontArial16,MinuetoColor.BLUE);

		// Start the timer.
		mswWatch.start();
		
		// Get reday to count the FPS		
		frameCount = 0;
		
		// At first, we draw no lines
		numberLines = 0;
		
		// Show the game window.
		window.setVisible(true);
			
		// Game/Rendering loop	
		while(true) {

			while (eventQueue.hasNext()) {
				eventQueue.handle();
			}
		
			window.clear();
			
			// For every lines we are supposed to draw:
			for(i = 0; i < numberLines; i++) {
				j = ranNumGenerator.nextInt(640); //Starting X of the line.
				k = ranNumGenerator.nextInt(480); //Starting Y of the line.
				x = ranNumGenerator.nextInt(640); //Ending X of the line.
				y = ranNumGenerator.nextInt(480); //Ending Y of the line.
				c = ranNumGenerator.nextInt(5);	// Random color of the line.
				window.drawLine(color[c], j, k, x, y); // Draw the line.
			}
			
			// Count this frame
			frameCount++;
			// We update the framerate every thread frames.
			if (frameCount == 20) {
				mswWatch.stop();
				
				// Using the timer, we calculate how much time was needed to
				// draw the 20 frames. This gives us our framterate.
				dFrameRate =  (double)20000/(double)mswWatch.getTime();
				// If our FPS is > 31, then we add some lines.
				if (dFrameRate > 31.0) numberLines = numberLines + (int)Math.round(Math.abs(dFrameRate - 30.0));
				// If our FPS is < 29, then we remove some lines.
				if (dFrameRate < 29.0) numberLines--;
				
				//We build the image that will display the framerate and the line count.
				imageFramerate = new MinuetoText("FPS: " + dFrameRate + " Lines: " + numberLines ,fontArial16,MinuetoColor.BLUE);
				// And we start counting the frame agains ...
				frameCount = 0;
				// ...after having properly reset the counter.
				mswWatch.reset();
				mswWatch.start();

			}
			
			// We display the framerate and the line count
			window.draw(imageFramerate, 0, 0);
			
			// Render all graphics in the back buffer.			
			window.render();
		}
		
		
	}
	
	/**
	 * We need this to make our demo runnable from the command line.
	 **/
	public static void main(String[] args) {
	
		@SuppressWarnings("unused")
		ScreenshotDemo main = new ScreenshotDemo();
	}

	public void handleMouseMove(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	public void handleMousePress(int x, int y, int button) {
		try {
			window.save("tempscreenshot"+filenum+".png");
		} catch (MinuetoFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		filenum += 1;
	}

	public void handleMouseRelease(int x, int y, int button) {
		// TODO Auto-generated method stub
		
	}
	
}

