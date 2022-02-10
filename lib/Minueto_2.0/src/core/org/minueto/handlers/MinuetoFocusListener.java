/*
 * @(#)MinuetoWindowListener.java        1.00 15/09/2004
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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Vector;

import org.minueto.MinuetoEventQueue;

/**
 * The <code>MinuetoFocusListener</code> is used by the 
 * MinuetoWindow to capture focus events. Under normal
 * situation, you do not need to create this class.
 * <p> 
 * However, if you want to use a MinuetoEventQueue to
 * capture window events from a Swing window, you
 * can create an instance of this class and register
 * it with the component.
 * 
 * @author Michael A. Hawker
 * @since 1.1
 */
public class MinuetoFocusListener implements FocusListener {
	
	private Vector<ListenerQueuePair> handlers;
	
	/**
	 * Creates a new <code>MinuetoWindowListener</code>.
	 */
	public MinuetoFocusListener() {
		
		this.handlers = new Vector<ListenerQueuePair>();
	}
	
	/**
	 * Register a <code>MinuetoFocusHandler</code> handler 
	 * with this listener.
	 * 
	 * @param handler <code>MinuetoFocusHandler</code> to register.
	 * @param queue <code>MinuetoEventQueue</code> to send events to.
	 */
	public void register(MinuetoFocusHandler handler, MinuetoEventQueue queue) {
		
		this.handlers.add(new ListenerQueuePair(handler, queue));
	}
	
	/**
	 * Unregisters a <code>MinuetoFocusHandler</code> from this
	 * listener.
	 * 
	 * @param handler <code>MinuetoFocusHandler</code> to unregister.
	 * @param queue <code>MinuetoEventQueue</code> where events where
	 *  stored.
	 */
	public void unregister(MinuetoFocusHandler handler, MinuetoEventQueue queue) {
		
		this.handlers.remove(new ListenerQueuePair(handler, queue));
	}
	
	/**
	 * Unregisters all <code>MinuetoFocusHandler</code> from
	 * this listener.
	 */
	public void clear() {
		
		this.handlers.clear();
	}
	
	/**
	 * Involked when the window gains focus
	 */
	public void focusGained(FocusEvent arg0) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new GetFocusEvent(
					(MinuetoFocusHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Involked when the window losses its focus
	 */
	public void focusLost(FocusEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new LostFocusEvent(
					(MinuetoFocusHandler)pair.getListener() ));
		}
	}
}

