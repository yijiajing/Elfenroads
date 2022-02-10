/*
 * @(#)MinuetoMouseMotionListener.java        1.00 15/09/2004
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

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import org.minueto.MinuetoEventQueue;

/**
 * The <code>MinuetoMouseMotionListener</code> is used by the 
 * MinuetoWindows to capture mouse input. Under normal
 * situation, you do not need to create this class.
 * <p> 
 * However, if you want to use a MinuetoEventQueue to
 * capture mouse events from a Swing component, you
 * can create an instance of this class and register
 * it with the component.
 * 
 * @author Alexandre Denault
 *
 */
public class MinuetoMouseMotionListener implements MouseMotionListener {
	
	private Vector<ListenerQueuePair> handlers;
	
	/**
	 * Creates a new <code>MinuetoMouseMotionListener</code>.
	 */
	public MinuetoMouseMotionListener() {
		
		this.handlers = new Vector<ListenerQueuePair>();
	}
	
	/**
	 * Register a <code>MinuetoMouseHandler</code> handler 
	 * with this listener.
	 * 
	 * @param handler <code>MinuetoMouseHandler</code> to register.
	 * @param queue <code>MinuetoEventQueue</code> to send events to.
	 */
	public void register(MinuetoMouseHandler handler, MinuetoEventQueue queue) {
		
		this.handlers.add(new ListenerQueuePair(handler, queue));
	}
	
	/**
	 * Unregisters a <code>MinuetoMouseHandler</code> from this
	 * listener.
	 * 
	 * @param handler <code>MinuetoMouseHandler</code> to unregister.
	 * @param queue <code>MinuetoEventQueue</code> where events where
	 *  stored.
	 */
	public void unregister(MinuetoMouseHandler handler, MinuetoEventQueue queue) {
		
		this.handlers.remove(new ListenerQueuePair(handler, queue));
	}
	
	/**
	 * Unregisters all <code>MinuetoMouseHandler</code> from
	 * this listener.
	 */
	public void clear() {
		
		this.handlers.clear();
	}
	
	/**
	 * Invoked when the mouse cursor has been moved onto a component 
	 * but no buttons have been pushed.
	 */
	public void mouseMoved(MouseEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add( new MouseMoveEvent (
					e.getX(),
					e.getY(),
					(MinuetoMouseHandler)pair.getListener() ));
		}
		
	}
	
	/**
	 * Invoked when a mouse button is pressed on a component 
	 * and then dragged. MOUSE_DRAGGED events will continue 
	 * to be delivered to the component where the drag originated 
	 * until the mouse button is released (regardless of whether 
	 * the mouse position is within the bounds of the component).
	 */
	public void mouseDragged(MouseEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add( new MouseMoveEvent (
					e.getX(),
					e.getY(),
					(MinuetoMouseHandler)pair.getListener() ));
		}
	}
	
}

