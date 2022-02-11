/*
 * @(#)MinuetoStopWatch.java
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
 * The <code>MinuetoStopWatch</code> class is used to measure time. It functions are
 * similar to those found on a stopwatch. <code>MinuetoStopWatch</code> measures 
 * time in milliseconds. However, it is only more or less 20 milliseconds precise.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 **/
public class MinuetoStopWatch {
	
	private long start;
	private long stop;
	
	private boolean running;
	
	/**
	 * Create a MinuetoStopWatch and set its value to 0.
	 **/
	public MinuetoStopWatch() {
		
		this.reset();
	}
	
	/**
	 * Start the stopwatch. Once started, the stopwatch can be stopped using
	 * the <code>stop</code> or <code>reset</code> method. Additionnals invocation of the
	 * <code>start</code> method are ignored if the stopwatch is already running.
	 **/
	public void start() {
		
		if (this.running == false) {
			this.start = System.currentTimeMillis();
			//System.out.println("Debug starting " + this.lStart);
		}
		
		this.running = true;
	}
	
	/**
	 * Stop the stopwatch. If this method is invoked twice, the second call has
	 * no effect.
	 **/
	public void stop() {
		
		if (this.running == false) return;
		
		this.stop = System.currentTimeMillis();
		this.running = false;
		
		//System.out.println("Debug stopping " + this.lStop);
	}
	
	/**
	 * Reset the value of the stopwatch to 0. If the stopwatch was running, it 
	 * is stopped.
	 **/
	public void reset() {
		
		this.start = -1;
		this.stop = -1;
		this.running = false;
	}

	/**
	 * If the stopwatch is running, returns the time ellapse in millisecond between 
	 * the invocation of the <code>start</code> method and the invocation of this
	 * method. Otherwise, returns the time ellapse between invocation of the
	 * <code>start</code> and <code>stop</code> method.
	 *
	 * @return the current value of the stopwatch.
	 **/
	public long getTime() {
		
		long now;
		
		/* If the stopwatch is still running, returning the current
		 * value of the stopwatch is a little more tricky. We need
		 * to record the time at which the method was called and 
		 * compare it to the time it was started. */
		if (running == true) {
			now = System.currentTimeMillis();
			//System.out.println("Debug now " + lNow);
			return now - this.start;			
		} else {
			/* Otherwise, a null start time means the stopwatch was never
			 * started. */
			if (this.start == -1) {
				return 0;
			} else {
			/* If the stopwatch was stopped, then we compare start time to
			 * to stop time. */
				return this.stop - this.start;
			}
		}
	}
}
