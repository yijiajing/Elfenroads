/*
 * @(#)MinuetoMouseListener.java        1.00 15/09/2004
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
import java.awt.event.MouseListener;
import java.util.Vector;

import org.minueto.MinuetoEventQueue;

/**
 * The <code>MinuetoMouseListener</code> is used by the 
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
public class MinuetoMouseListener implements MouseListener {
	
	private Vector<ListenerQueuePair> handlers;
		
	/**
	 * Creates a new <code>MinuetoMouseListener</code>.
	 */
	public MinuetoMouseListener() {
		
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
	 * Invoked when a mouse button has been pressed on a component.
	 */
	public void mousePressed(MouseEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add( new MousePressEvent (
					e.getX(),
					e.getY(),
					e.getButton(), 
					(MinuetoMouseHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when a mouse button has been released on a component.
	 */
	public void mouseReleased(MouseEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add( new MouseReleaseEvent (
					e.getX(),
					e.getY(),
					e.getButton(), 
					(MinuetoMouseHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when the mouse button has been clicked (pressed and 
	 * released) on a component.
	 */
	public void mouseClicked(MouseEvent e) {
		// Not used			
	}
	
	/**
	 * Invoked when the mouse enters a component.
	 */
	public void mouseEntered(MouseEvent e) {
		// Not used
	}
	
	/**
	 * Invoked when the mouse exits a component.
	 */
	public void mouseExited(MouseEvent e) {
		// Not used
	}
	
}
