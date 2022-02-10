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
 * The <code>MinuetoMouseWheelHandler</code> is an interface for receiving a mouse wheel event 
 * (rotate) from a <code>MinuetoWindow</code> and handling it.
 * <p>
 * In other words, the class that will handle mouse wheel input from <code>MinuetoWindow</code> 
 * must extend this interface. In turn, that class must be registered with
 * the <code>MinuetoEventQueue</code>.
 * <p>
 * An event is generated everytime the mouse wheel is turned. 
 * When the handle method on the <code>MinuetoEventQueue</code> is invoked, that
 * event is processed and the appropriate method in <code>MinuetoMouseWheelHandler</code>
 * is invoked.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see		org.minueto.handlers.MinuetoMouse
 * @see		org.minueto.MinuetoEventQueue
 **/ 
public interface MinuetoMouseWheelHandler {

	/** 
	 * Invoked when a mouse wheel is turned. The amount of rotation is passed throught 
	 * the method argument. <p>
	 * 
	 * Note: If the mouse wheel rotated towards the user (down) the value is positive. 
	 * If the mouse wheel rotated away from the user (up) the value is negative.
	 * <p>
	 *
	 * @param rotate <code>int</code> denoting how much the mouse wheel was turned.
	 **/
	public void handleMouseWheelRotate(int rotate);	
	

}