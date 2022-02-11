/*
 * @(#)MinuetoEvent.java        1.00 15/09/2004
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
 * <code>MinuetoEvent</code> describes the interface for an Event
 * message. These event message are stored in a 
 * <code>MinuetoEventQueue</code>.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.3
 * @see		MinuetoEventQueue
 **/
public interface MinuetoEvent {
	
		/**
		 * This method is called when an event is processed. 
		 * Most often, this method is called by the 
		 * <code>MinuetoEventQueue</code> where the message is
		 * stored.
		 */
      	public void handle();

}


