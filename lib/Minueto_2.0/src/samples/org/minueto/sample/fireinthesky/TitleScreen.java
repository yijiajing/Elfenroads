/*
 * @(#)TitleScreen.java
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

import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.window.MinuetoWindow;

class TitleScreen implements MinuetoKeyboardHandler {

	private MinuetoWindow window;
	private MinuetoEventQueue eventQueue;
	
	private ImageStore imageStore;
	private MusicStore musicStore;
	
	private boolean exit;
	
	private boolean display;
	
	public TitleScreen(MinuetoWindow window, ImageStore imageStore, MusicStore musicStore) {
		
		this.window = window;
		this.imageStore = imageStore;
		this.musicStore = musicStore;
		
		this.eventQueue = new MinuetoEventQueue();
		this.window.registerKeyboardHandler(this, this.eventQueue);
				
		this.display = true;
		this.exit = false;
	}
	
	public void gameLoop() {
	
		this.window.setVisible(true);
		this.window.setCursorVisible(false);
		
		this.musicStore.stopMidi("attack");
		this.musicStore.playMidi("intro");		
		
		while(this.exit == false) {		
		
		
			while(this.eventQueue.hasNext()) {
				this.eventQueue.handle();
			}
			
			this.window.draw(this.imageStore.getImage(ImageStore.TITLE_BG), 0, 0);			
			this.window.draw(this.imageStore.getImage(ImageStore.TITLE_SHADOW), 13, 13);
			this.window.draw(this.imageStore.getImage(ImageStore.TITLE), 10, 10);
			
			if (this.display == true) {
				
				this.window.draw(this.imageStore.getImage(ImageStore.PRESS_START_SHADOW), 250, 500);			
				this.window.draw(this.imageStore.getImage(ImageStore.PRESS_START), 248, 498);
				this.display = false;
			} else {
				this.display = true;
			}
						
			this.window.render();
			
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				
			}
			
		}
		
		//this.musicStore.stopMusic();		
	}
	
	public void handleKeyPress(int value) {

		switch (value) {

		case MinuetoKeyboard.KEY_SPACE:
			this.exit = true;
			break;
		case  MinuetoKeyboard.KEY_ESC:
			System.exit(-1);
			break;
		default:
			System.out.println(value);
		}
	}


	public void handleKeyRelease(int value) {
	
	}

	public void handleKeyType(char keyChar) {
		// TODO Auto-generated method stub
		
	}
}