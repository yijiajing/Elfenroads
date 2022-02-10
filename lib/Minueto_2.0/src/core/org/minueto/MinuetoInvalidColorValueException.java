/*
 * @(#)MinuetoInvalidColorValueException.java
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
 * Thrown by constructors of the <code>MinuetoColor</code> class to indicate that 
 * an invalid color value was passed as a parameter.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 		MinuetoColor
 **/
public class MinuetoInvalidColorValueException extends RuntimeException {

	private static final long serialVersionUID = 1;
	
	/**
	 * Constructs a <code>MinuetoInvalidColorValueException</code> with null as its error message string.
    **/
	public MinuetoInvalidColorValueException() {
		super();
	}
	
	/**
	 * Constructs a <code>MinuetoInvalidColorValueException</code>, saving a reference to the error 
	 * message string s for later retrieval by the getMessage method.
	 *
	 * @param s String indicating the detailed message.
	 **/
	public MinuetoInvalidColorValueException(String s) {
		super(s);
	}
		
}
