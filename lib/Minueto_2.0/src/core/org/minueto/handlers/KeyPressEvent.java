/*
 * @(#)MinuetoKeyPressEvent.java        1.00 15/09/2004
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

import org.minueto.MinuetoEvent;

class KeyPressEvent implements MinuetoEvent
{

	private	int value;    
	
	private MinuetoKeyboardHandler handler;
	
	KeyPressEvent(int value, MinuetoKeyboardHandler handler) {
		
		this.value = value;
        
		this.handler = handler;	
	}
	
   	public void handle() {
   	
   		handler.handleKeyPress(this.value);
   	}

}

// end of file MinuetoKeyPressEvent.
