/*
 * @(#)MinuetoMouseHandler.java        1.00 15/09/2004
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
 * The <code>MinuetoMouseHanlder</code> is an interface for receiving a mouse event 
 * (motion, click) from a <code>MinuetoWindow</code> and handling it.
 * <p>
 * In other words, the class that will handle mouse input from <code>MinuetoWindow</code> must extend
 * this interface. In turn, that class must be registered with
 * the <code>MinuetoEventQueue</code>.
 * <p>
 * An event is generated everytime the mouse is clicked or moved. 
 * When the handle method on the <code>MinuetoEventQueue</code> is invoked, that
 * event is processed and the appropriate method in <code>MinuetoMouseHandler</code>
 * is invoked.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see		org.minueto.handlers.MinuetoMouse
 * @see		org.minueto.MinuetoEventQueue
 **/ 
public interface MinuetoMouseHandler {

	/** 
	 * Invoked when a mouse button is pressed. The current position of the
	 * mouse pointer (relative to the window) and the identity of the
	 * button are passed throught the method arguments.
	 * <p>
	 * The <code>MinuetoMouse</code> class stores the constants used to identify the
	 * mouse buttons.
	 *
	 * @param x <code>int</code> denoting the X position of the mouse.
	 * @param y <code>int</code> denoting the Y position of the mouse.
	 * @param button <code>int</code> denoting which button was pressed.
	 * @see	 MinuetoMouse
	 **/
	public void handleMousePress(int x, int y, int button);	
	
	/** 
	 * Invoked when a mouse button is released. The current position of the
	 * mouse pointer (relative to the window) and the identity of the
	 * button are passed throught the method arguments.
	 * <p>
	 * The <code>MinuetoMouse</code> class stores the constants used to identify the
	 * mouse buttons.
	 *
	 * @param x <code>int</code> denoting the X position of the mouse.
	 * @param y <code>int</code> denoting the Y position of the mouse.
	 * @param button <code>int</code> denoting which button was released.
	 * @see	 MinuetoMouse
	 **/
	public void handleMouseRelease(int x, int y, int button);
	
	/** 
	 * Invoked when the mouse cursor is moved. The current position of the
	 * mouse pointer (relative to the window) are passed throught the 
	 * method arguments.
	 * <p>
	 * This method is invoked very often when the mouse is moved. To avoid performance
	 * impacts, avoid having expensive (slow) code in this method.
	 *
	 * @param x <code>int</code> denoting the X position of the mouse.
	 * @param y <code>int</code> denoting the Y position of the mouse.
	 * @see	 MinuetoMouse
	 **/
	public void handleMouseMove(int x, int y);
}
