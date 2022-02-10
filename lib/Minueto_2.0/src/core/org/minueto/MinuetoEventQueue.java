/*
 * @(#)MinuetoEventQueue.java
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

import java.util.LinkedList;

import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.handlers.MinuetoWindowHandler;

/**
 * The <code>MinuetoEventQueue</code> to stores Minueto related events such as
 * keyboard input, mouse input and window event. Events can be processed
 * by registering the proper event handler and calling the <code>handle</code> method.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.3
 * @see 		MinuetoKeyboardHandler
 * @see		MinuetoMouseHandler
 * @see		MinuetoWindowHandler
 */
public class MinuetoEventQueue {
	
	private	LinkedList<MinuetoEvent> eventList;
		
	/**
	 * Create an empty event queue.
	 **/
	public MinuetoEventQueue() {
		
		this.eventList = new LinkedList<MinuetoEvent>();
	}
	
	/**
	 * Add an event to the event queue. This method should only be called by
	 * events inside the Minueto package.
	 *
	 * @param event <code>MinuetoEvent</code> to be added to the queue.
	 **/
	public void add(MinuetoEvent event) {
   	
		if (event == null) new NullPointerException();
		
		synchronized(this.eventList) {
			this.eventList.add((MinuetoEvent)event);
		}
   	}

	/**
	 * Return true if the queue holds another event.
	 *
	 * @return <code>boolean</code> value indicating if the queue is not empty.
	 **/
   public boolean hasNext() {
   	
		/* Check the size of the linklist to see if another event
		 * is in the queue. */
	   synchronized(this.eventList) {
		   	if (this.eventList.size() > 0) {
		   		return true;
		   	}
	   }
   	
	   return false;
   	
   }

	/**
	 * Handle the next event using the appropriate handler.
	 *
	 * @throws <code>MinuetoEventQueueEmptyException</code> if the event queue is empty.
	 **/
   public synchronized void handle() {
   		
	   	MinuetoEvent event;
	   	
			/* If the queue is empty, throw an exception. */
	   	if (this.hasNext() == false) throw new MinuetoEventQueueEmptyException();
	 
			/* Get the next event, remove it from the list and handle it. */
	   	synchronized(this.eventList) {
	   		event = (MinuetoEvent)this.eventList.getFirst();
	   		this.eventList.removeFirst();
	   	}
	   	event.handle();   		   		
   }
		
}
