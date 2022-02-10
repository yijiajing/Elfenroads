package org.minueto.handlers;

import org.minueto.MinuetoEventQueue;

class ListenerQueuePair {

	private Object listener;
	private MinuetoEventQueue queue;
	
	public ListenerQueuePair(Object listener, MinuetoEventQueue queue) {
		
		this.listener = listener;
		this.queue = queue;
	}

	public Object getListener() {
		return listener;
	}

	public MinuetoEventQueue getQueue() {
		return queue;
	}
	
	public boolean equals(Object o) {
		
		if (! (o instanceof ListenerQueuePair) ) {
			return false;
		}
		
		ListenerQueuePair pair = (ListenerQueuePair)o;
		
		return ((pair.listener == this.listener) && (pair.queue == this.queue));
		
	}
	
	
}
