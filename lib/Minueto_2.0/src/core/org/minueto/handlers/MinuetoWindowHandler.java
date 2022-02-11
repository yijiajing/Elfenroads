/*
 * @(#)MinuetoWindowHandler.java 
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
 * The <code>MinuetoWindowHandler</code> is an interface for receiving a window event 
 * from a <code>MinuetoWindow</code> and handling it.
 * <p>
 * The class that will handle window events from <code>MinuetoWindow</code> must extend
 * the <code>MinuetoWindowHandler</code> interface. That class must be registered with
 * the <code>MinuetoEventQueue</code>.
 * <p>
 * An event is generated when the window is minimized/maximized, when it gets 
 * or loses focus, or when the user tries to quit the application. That event,
 * in turn, invokes the appropriate <code>handle</code> method.
  *
 * @author	Alexandre Denault
 * @version 1.1
 * @since 	Minueto 0.4
 * @see		org.minueto.MinuetoEventQueue
 * @see		org.minueto.window.MinuetoWindow
 **/ 
public interface MinuetoWindowHandler extends MinuetoFocusHandler {

	/**
	 * Invoked when the user tries to quit the application. (The user presses
	 * the X button in the top right corner of the window.)
	 **/
	public void handleQuitRequest();
	
	/**
	 * Invoked when the MinuetoWindow is minimized.
	 **/
	public void handleMinimizeWindow();
	
	/**
	 * Invoked when the MinuetoWindow is restored (from being minimized).
	 **/
	public void handleRestoreWindow();

}