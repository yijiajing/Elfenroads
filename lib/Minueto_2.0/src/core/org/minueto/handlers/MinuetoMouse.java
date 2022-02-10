/*
 * @(#)MinuetoMouse.java
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
 * The <code>MinuetoMouse</code> class contains the set of constants returned by the 
 * <code>MinuetoMouseHandler</code>. Instead pf evaluating mouse buttons as integer
 * values. the following constants should be used.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 		MinuetoMouseHandler
 **/
public class MinuetoMouse {

	/** The left mouse button. */
	public static final int MOUSE_BUTTON_LEFT = 1;
	/** The center mouse button. */
	public static final int MOUSE_BUTTON_MIDDLE = 2;
	/** The right mouse button. */
	public static final int MOUSE_BUTTON_RIGHT = 3;
	
	private MinuetoMouse() {
		/* This object should never be created. */
	}
}
