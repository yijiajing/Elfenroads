/*
 * @(#)MinuetoKeyListener.java        1.00 15/09/2004
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

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import org.minueto.MinuetoEventQueue;

/**
 * The <code>MinuetoKeyListener</code> is used by the 
 * MinuetoWindows to capture keyboard input. Under normal
 * situation, you do not need to create this class.
 * <p> 
 * However, if you want to use a MinuetoEventQueue to
 * capture keyboard events from a Swing component, you
 * can create an instance of this class and register
 * it with the component.
 * 
 * @author Alexandre Denault
 *
 */
public class MinuetoKeyListener implements KeyListener {

    private Vector<ListenerQueuePair> handlers;

    /**
     * Creates a new <code>MinuetoKeyListener</code>.
     */
    public MinuetoKeyListener() {

        this.handlers = new Vector<ListenerQueuePair>();
    }
    
    /**
     * Register a <code>MinuetoKeyboardHandler</code> handler 
     * with this listener.
     * 
     * @param handler <code>MinuetoKeyboardHandler</code> to register.
     * @param queue <code>MinuetoEventQueue</code> to send events to.
     */
    public void register(MinuetoKeyboardHandler handler, MinuetoEventQueue queue) {
    	
    	this.handlers.add(new ListenerQueuePair(handler, queue));
    }

    /**
     * Unregisters a <code>MinuetoKeyboardHandler</code> from this
     * listener.
     * 
     * @param handler <code>MinuetoKeyboardHandler</code> to unregister.
     * @param queue <code>MinuetoEventQueue</code> where events where
     *  stored.
     */
    public void unregister(MinuetoKeyboardHandler handler, MinuetoEventQueue queue) {
    
    	this.handlers.remove(new ListenerQueuePair(handler, queue));
    }
    
    /**
     * Unregisters all <code>MinuetoKeyboardHandler</code> from
     * this listener.
     */
    public void clear() {
    	
    	this.handlers.clear();
    }
    
    /**
     * Invoked when a key has been typed. See the class description for 
     * KeyEvent for a definition of a key typed event.
     */
    public void keyPressed(KeyEvent e) {

		for(ListenerQueuePair pair: this.handlers) {
    		
    		pair.getQueue().add(new KeyPressEvent(
    				e.getKeyCode(), 
    				(MinuetoKeyboardHandler)pair.getListener()));
    	}
    }

    /**
     * Invoked when a key has been pressed. See the class description 
     * for KeyEvent for a definition of a key pressed event.
     */
    public void keyReleased(KeyEvent e){

        KeyEvent nextPress = (KeyEvent)Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent(KeyEvent.KEY_PRESSED);

        if ((nextPress == null) || (nextPress.getWhen() != e.getWhen()) || (nextPress.getKeyCode() != e.getKeyCode()))
        {
    		for(ListenerQueuePair pair: this.handlers) {
        		             	
        		pair.getQueue().add(new KeyReleaseEvent(
        				e.getKeyCode(), 
        				(MinuetoKeyboardHandler)pair.getListener()));
        	}
        }
    }

    /**
     * Invoked when a key has been released. See the class description for 
     * KeyEvent for a definition of a key released event.
     */
    public void keyTyped(KeyEvent e)   {
    	
		for(ListenerQueuePair pair: this.handlers) {
    		
    		pair.getQueue().add(new KeyTypedEvent(    				
    				e.getKeyChar(), 
    				(MinuetoKeyboardHandler)pair.getListener()));
    	}
    }

}
