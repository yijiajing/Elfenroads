package org.minueto.handlers;

/**
 * 
 * @author Michael A. Hawker
 * @since 1.1
 */
public interface MinuetoFocusHandler {

	/** 
	 * Invoked when the MinuetoWindow receives the focus.
	 **/
	public void handleGetFocus();

	/**
	 * Invoked when the MinuetoWindow loses the focus.
	 **/
	public void handleLostFocus();

}