/*
 * @(#)MinuetoKeyboardHandler.java
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
 */

package org.minueto.handlers;


/**
 * The <code>MinuetoKeyboardHandler</code> is an interface for receiving a keyboard  
 * event (press, release) from a <code>MinuetoWindow</code> and handling it.
 * <p>
 * In other words, the class that will handle keyboard input from <code>MinuetoWindow</code> must extend
 * this interface. In turn, that class must be registered with
 * the <code>MinuetoEventQueue</code>.
 * <p>
 * An event is generated everytime the keyboard is pressed and released. 
 * When the handle method on the <code>MinuetoEventQueue</code> is invoked, that
 * event is processed and the appropriate method in <code>MinuetoKeyboardHandler</code>
 * is invoked.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 	org.minueto.handlers.MinuetoKeyboard
 * @see		org.minueto.MinuetoEventQueue
 **/ 
public interface MinuetoKeyboardHandler {
	
	/** 
	 * Invoked when a keyboard key is pressed. The identity of the key
	 * that was pressed is passed throught the method argument. Events of this type are
	 * well suited for the controls of a game.
	 * <p>
	 * The <code>MinuetoKeyboard</code> class stores the constants used to identify the
	 * different keys on the keyboard.
	 *
	 * @param value <code>int</code> denoting which key was pressed.
     * @see	 MinuetoKeyboard
	 **/
	public void handleKeyPress(int value);
	
	/** 
	 * Invoked when a keyboard key is released. The identity of the key
	 * that was released is passed throught the method argument. Events of this type are
	 * well suited for the controls of a game.
	 * <p>
	 * The <code>MinuetoKeyboard</code> class stores the constants used to identify the
	 * different keys on the keyboard.
	 *
	 * @param value <code>int</code> denoting which key was released.
     * @see	 MinuetoKeyboard
	 **/
	public void handleKeyRelease(int value);
	
	/**
	 * Invoked when a keyboard key was typed. The identity of the key
	 * that was released is passed throught the method argument. Events of this type are 
	 * well suited for typing text.
	 * <p>	  
	 *
     * @param keyChar <code>char</code> denoting the key in this event.
	 * @see	 MinuetoKeyboard
	 **/
	public void handleKeyType(char keyChar);
}
