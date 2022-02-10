/*
 * @(#)main.java
 *
 * Fire in the Sky - A Minueto Demo
 * Copyright (c) 2004 McGill University
 * 3480 University Street, Montreal, Quebec H3A 2A7
 
 * Fire in the sky is a demo of the Minueto graphic API. More information on
 * Minueto can be found at http://minueto.cs.mcgill.ca .
 
 * This game is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This game is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
 
package org.minueto.sample.fireinthesky;

import org.minueto.window.MinuetoFrame;
import org.minueto.window.MinuetoFullscreen;
import org.minueto.window.MinuetoWindow;


public class FireInTheSky {

	private final static int SCREEN_WIDTH = 800;
	private final static int SCREEN_HEIGHT = 600;

	public static void main(String[] args) {

		MinuetoWindow window;
		ImageStore imageStore;
		MusicStore musicStore;

		Shooter myShooter;
		TitleScreen myTitleScreen;

		try {
			window = new MinuetoFullscreen(SCREEN_WIDTH, SCREEN_HEIGHT,32);
		} catch (Exception e) {
			window = new MinuetoFrame(SCREEN_WIDTH, SCREEN_HEIGHT, false);
		}
		
		imageStore = new ImageStore();
		musicStore = new MusicStore();

		while(true) {			
			
			myTitleScreen = new TitleScreen(window, imageStore, musicStore);
			myTitleScreen.gameLoop();
			
			myShooter = new Shooter(window, imageStore, musicStore);
			myShooter.gameLoop();			
		
		}		
	}	
}



