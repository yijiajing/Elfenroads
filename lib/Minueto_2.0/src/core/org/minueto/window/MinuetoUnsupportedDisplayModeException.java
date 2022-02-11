/**
 * @(#)MinuetoUnsupportedDisplayModeException.java
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
 **/

package org.minueto.window;

/**
 * Thrown by the <code>MinuetoFullscreen</code> constructor to indicate that the 
 * desired display mode is not available.
 * <p>
 * Please note that fullscreen mode is only available under Windows and MacOS X
 * (due to limitations on the JVM on some operating system). Also note that
 * some videocards only work with standard resolutions such as 400x300, 640x480,
 * 800x600, 1024x768, 1280x1024, 1600x1200, etc.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 	org.minueto.window.MinuetoFullscreen
 **/
public class MinuetoUnsupportedDisplayModeException extends RuntimeException {
	
	private static final long serialVersionUID = 1;
	
	/**
	 * Constructs a <code>MinuetoUnsupportedDisplayModeException</code> with null as its error message string.
    **/
	public MinuetoUnsupportedDisplayModeException() {
		super();
	}
	
	/**
	 * Constructs a <code>MinuetoUnsupportedDisplayModeException</code>, saving a reference to the error 
	 * message string s for later retrieval by the getMessage method.
	 *
	 * @param s String indicating the detailed message.
	 **/
	public MinuetoUnsupportedDisplayModeException(String s) {
		super(s);
	}
}
