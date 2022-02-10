/*
 * @(#)MinuetoTool.java
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

import java.awt.Dimension;
import java.awt.Toolkit;

/**
 * The <code>MinuetoTool</code> class provides various tools that don't particularly
 * fit into other Minueto class. 
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 **/
 
public class MinuetoTool {
	
	/**
	 * Returns the width of the current display (desktop) in pxels. In fullscreen
	 * mode, this value corresponds to the width of the <code>MinuetoWindow</code>.
	 * 
	 * @return <code>int</code> denoting the width of the current display.
	 **/
	public static int getDisplayWidth() {
		
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return screen.width;
	}
	
	/**
	 * Returns the height of the current display (desktop) in pxels. In fullscreen
	 * mode, this value corresponds to the height of the <code>MinuetoWindow</code>.
	 *
	 * @return <code>int</code> denoting the height of the current display.
	 **/
	public static int getDisplayHeight() {
	
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		return screen.height;
	}
	
	/**
	 * Returns true if the operating system is Windows.
	 * 
	 * @return <code>boolean</code> indicating if the OS is Windows
	 */
	public static boolean isWindows() {
		
		String os = System.getProperty("os.name").toLowerCase();
		
		return (os.indexOf("windows") > -1);
	}
	
	/**
	 * Returns true if the operating system is Linux.
	 * 
	 * @return <code>boolean</code> indicating if the OS is Linux
	 */
	public static boolean isLinux() {
		
		String os = System.getProperty("os.name").toLowerCase();
		
		return (os.indexOf("linux") > -1);
	}
	
	/**
	 * Returns true if the operating system is MacOS.
	 * 
	 * @return <code>boolean</code> indicating if the OS is MacOS
	 */
	public static boolean isMac() {
		
		String os = System.getProperty("os.name").toLowerCase();
		
		return (os.indexOf("mac") > -1);
	}
	

	

		
}