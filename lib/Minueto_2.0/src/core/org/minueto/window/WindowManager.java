/*
 * @(#)WindowManager.java 
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

package org.minueto.window;

import java.util.Vector;

class WindowManager {

	private static WindowManager instance = new WindowManager();
	
	private Vector<MinuetoWindow> windowList;
	
	private WindowManager() {
		
		windowList = new Vector<MinuetoWindow>();
	}
	
	public static WindowManager getInstance() {
		
		return instance;
	}
	
	public void register(MinuetoWindow window, boolean singleton) {
	
		if (singleton) {
			if (windowList.size() != 0) {
				throw new MinuetoWindowAlreadyInitializedException();
			}
		}
		
		windowList.add(window);	
	}
	
	public void unregister(MinuetoWindow window) {
		
		windowList.remove(window);
	}
	
}
