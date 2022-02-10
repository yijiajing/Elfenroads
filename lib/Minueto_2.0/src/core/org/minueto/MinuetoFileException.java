/*
 * @(#)MinuetoFileException.java
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

import java.io.IOException;
/**
 * Thrown by several methods of the Minueto package to indicate that 
 * a read or write file error has occured.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 		MinuetoEventQueue
 * @see 		MinuetoEventQueue#handle()
 **/
public class MinuetoFileException extends IOException {
	
	private static final long serialVersionUID = 1;
	
	/**
	 * Constructs a <code>MinuetoFileException</code> with null as its error message string.
    **/
	public MinuetoFileException() {
		super();
	}
	
	/**
	 * Constructs a <code>MinuetoFileException</code>, saving a reference to the error 
	 * message string s for later retrieval by the <code>getMessage</code> method.
	 *
	 * @param s String indicating the detailed message.
	 **/
	public MinuetoFileException(String s) {
		super(s);
	}

}