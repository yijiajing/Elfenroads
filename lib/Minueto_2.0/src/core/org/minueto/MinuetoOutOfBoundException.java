/*
 * @(#)MinuetoOutOfBoundException.java
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
 * Thrown by the <code>draw</code> methods of the Minueto package to indicate that 
 * that the user tried to draw something out of the screen or image.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 **/
public class MinuetoOutOfBoundException extends RuntimeException {

	private static final long serialVersionUID = 1;

	/**
	 * Constructs a <code>MinuetoOutOfBoundException</code> with null as its error message string.
    **/
	public MinuetoOutOfBoundException() {
		super();
	}
	
	/**
	 * Constructs a <code>MinuetoOutOfBoundException</code>, saving a reference to the error 
	 * message string s for later retrieval by the <code>getMessage</code> method.
	 *
	 * @param s String indicating the detailed message.
	 **/
	public MinuetoOutOfBoundException(String s) {
		super(s);
	}
	
	/**
	 * Constructs a <code>MinuetoOutOfBoundException</code>, saving a detailed description
	 * of the error, including the erroneous value and the maximum/minimum possible
	 * values. That message string s can be later retrieved by the <code>getMessage</code> 
	 * method.
	 *
	 * @param valueFalse <code>int</code> denoting the wrong value.
	 * @param valueMin <code>int</code> denoting the minimum possible value.
	 * @param valueMax <code>int</code> denoting the maximum possible value.
	 **/
	public MinuetoOutOfBoundException( int valueFalse, int valueMin, int valueMax ) {
	
		super("You tried to draw at " + valueFalse + 
				". However, you can only draw between " + 
				valueMin + " and " + (valueMax) + ".");
	}	
}