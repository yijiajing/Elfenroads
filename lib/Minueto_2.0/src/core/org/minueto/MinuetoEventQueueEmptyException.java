/*
 * @(#)MinuetoEventQueueEmptyException.java
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

package org.minueto;

/**
 * Thrown by the <code>handle</code> method of the <code>MinuetoEventQueue</code> to indicate that 
 * the event queue is empty and there is no event to "handle".
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.3
 * @see 		MinuetoEventQueue
 **/
public class MinuetoEventQueueEmptyException extends RuntimeException {
	
	private static final long serialVersionUID = 1;
	
	/**
	 * Constructs a <code>MinuetoEventQueueEmptyException</code> with null as its error message string.
    **/
	public MinuetoEventQueueEmptyException() {
		super();
	}
	
	/**
	 * Constructs a <code>MinuetoEventQueueEmptyException</code>, saving a reference to the error 
	 * message string s for later retrieval by the <code>getMessage</code> method.
	 *
	 * @param s String indicating the detailed message.
	 **/
	public MinuetoEventQueueEmptyException(String s) {
		super(s);
	}
}