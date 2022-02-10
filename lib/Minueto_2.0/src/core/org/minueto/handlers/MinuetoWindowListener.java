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

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.minueto.MinuetoEventQueue;

/**
 * The <code>MinuetoWindowListener</code> is used by the 
 * MinuetoWindows to capture window events. Under normal
 * situation, you do not need to create this class.
 * <p> 
 * However, if you want to use a MinuetoEventQueue to
 * capture window events from a Swing window, you
 * can create an instance of this class and register
 * it with the component.
 * 
 * @author Alexandre Denault
 *
 */
public class MinuetoWindowListener implements WindowListener, 
InternalFrameListener {
	
	private Vector<ListenerQueuePair> handlers;	
	
	/**
	 * Creates a new <code>MinuetoWindowListener</code>.
	 */
	public MinuetoWindowListener() {
		
		this.handlers = new Vector<ListenerQueuePair>();
	}
	
	/**
	 * Register a <code>MinuetoWindowHandler</code> handler 
	 * with this listener.
	 * 
	 * @param handler <code>MinuetoWindowHandler</code> to register.
	 * @param queue <code>MinuetoEventQueue</code> to send events to.
	 */
	public void register(MinuetoWindowHandler handler, MinuetoEventQueue queue) {
		
		this.handlers.add(new ListenerQueuePair(handler, queue));
	}
	
	/**
	 * Unregisters a <code>MinuetoWindowHandler</code> from this
	 * listener.
	 * 
	 * @param handler <code>MinuetoWindowHandler</code> to unregister.
	 * @param queue <code>MinuetoEventQueue</code> where events where
	 *  stored.
	 */
	public void unregister(MinuetoWindowHandler handler, MinuetoEventQueue queue) {
		
		this.handlers.remove(new ListenerQueuePair(handler, queue));
	}
	
	/**
	 * Unregisters all <code>MinuetoWindowHandler</code> from
	 * this listener.
	 */
	public void clear() {
		
		this.handlers.clear();
	}
	
	/**
	 * Invoked when the Window is set to be the active Window. 
	 * Only a Frame or a Dialog can be the active Window. The 
	 * native windowing system may denote the active Window 
	 * or its children with special decorations, such as a 
	 * highlighted title bar. The active Window is always 
	 * either the focused Window, or the first Frame or 
	 * Dialog that is an owner of the focused Window.
	 */
	public void windowActivated(WindowEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new GetFocusEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when a window has been closed as the 
	 * result of calling dispose on the window.
	 */
	public void windowClosed(WindowEvent e) {
		// Not used
	}
	
	/**
	 * Invoked when the user attempts to close the window 
	 * from the window's system menu. If the program does 
	 * not explicitly hide or dispose the window while 
	 * processing this event, the window close operation 
	 * will be cancelled.
	 */
	public void windowClosing(WindowEvent e) {
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new QuitRequestEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when a Window is no longer the active 
	 * Window. Only a Frame or a Dialog can be the active 
	 * Window. The native windowing system may denote the 
	 * active Window or its children with special decorations, 
	 * such as a highlighted title bar. The active Window is 
	 * always either the focused Window, or the first Frame 
	 * or Dialog that is an owner of the focused Window.
	 */
	public void windowDeactivated(WindowEvent e) {

		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new LostFocusEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when a window is changed from a minimized to a 
	 * normal state.
	 */
	public void windowDeiconified(WindowEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new RestoreWindowEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when a window is changed from a normal to a minimized 
	 * state. For many platforms, a minimized window is displayed 
	 * as the icon specified in the window's iconImage property.
	 */
	public void windowIconified(WindowEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {	
		 
			pair.getQueue().add(new MinimizeWindowEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked the first time a window is made visible.
	 */
	public void windowOpened(WindowEvent e) {
		//Not used
	}
	
	/**
	 * Invoked when an internal frame is activated.
	 * @param e
	 */
	public void internalFrameActivated(InternalFrameEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new GetFocusEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when an internal frame has been closed.
	 * 
	 * @param e
	 */
	public void internalFrameClosed(InternalFrameEvent e) {
		// Not used
	}
	
	/**
	 * Invoked when an internal frame is in the process of 
	 * being closed. The close operation can be overridden at this point.
	 * @param e
	 */
	public void internalFrameClosing(InternalFrameEvent e) {

		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new QuitRequestEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when an internal frame is de-activated.
	 * @param e
	 */
	public void internalFrameDeactivated(InternalFrameEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new LostFocusEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when an internal frame is de-iconified.
	 * @param e
	 */
	public void internalFrameDeiconified(InternalFrameEvent e) {
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new RestoreWindowEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when an internal frame is iconified.
	 * @param e
	 */
	public void internalFrameIconified(InternalFrameEvent e) {
		
		for(ListenerQueuePair pair: this.handlers) {
			
			pair.getQueue().add(new MinimizeWindowEvent(
					(MinuetoWindowHandler)pair.getListener() ));
		}
	}
	
	/**
	 * Invoked when a internal frame has been opened.
	 * @param e
	 */
	public void internalFrameOpened(InternalFrameEvent e) {
		// Not used
	}
}

