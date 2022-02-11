/*
 * @(#)ShooterKeyboard.java
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

import org.minueto.handlers.MinuetoKeyboard;
import org.minueto.handlers.MinuetoKeyboardHandler;

public class ShooterKeyboard implements MinuetoKeyboardHandler {

	private boolean keyUp;
	private boolean keyDown;
	private boolean keyLeft;
	private boolean keyRight;
	private boolean keySpace;
	
	public boolean isKeyUp() {
	
		return keyUp;
	}
	
	public boolean isKeyDown() {
	
		return keyDown;
	}
	
	public boolean isKeyLeft() {
	
		return keyLeft;
	}
	
	public boolean isKeyRight() {
	
		return keyRight;
	}
	
	public boolean isKeySpace() {
	
		return keySpace;
	}
	
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
		case MinuetoKeyboard.KEY_SPACE:
			this.keySpace = true;
			break;
		case  MinuetoKeyboard.KEY_ESC:
			System.exit(-1);
			break;
		default:
			System.out.println(value);
		}
	}


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
		case MinuetoKeyboard.KEY_SPACE:
			this.keySpace = false;
			break;
		default:
			System.out.println(value);
		}
	}

	public void handleKeyType(char keyChar) {
		// TODO Auto-generated method stub
		
	}

}