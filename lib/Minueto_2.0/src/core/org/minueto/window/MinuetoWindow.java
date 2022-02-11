package org.minueto.window;

import org.minueto.MinuetoEventQueue;
import org.minueto.handlers.MinuetoFocusHandler;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.handlers.MinuetoMouseWheelHandler;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.image.MinuetoDrawingSurface;

/**
 * Interface describing the minimum functionality provided by all
 * implementation of Minueto.
 * 
 * @author Michael A. Hawker
 * @since 1.1
 * @version 1.0
 * @see 	org.minueto.window.MinuetoBaseWindow
 */
public interface MinuetoWindow extends MinuetoDrawingSurface {

	/**
	 * Draws the content of the backbuffer onto the window. The content of the 
	 * backbuffer is lost after this call.
	 *
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is 
	 * hidden (invisible) or has been closed.    
	 **/
	public abstract void render();

	/**
	 * Returns the top-left X screen location of the MinuetoWindow drawing surface
	 * 
	 * @since 1.1
	 * @return <code>int</code> denoting the x position of the drawing surface
	 */
	public abstract int getPositionX();
	
	/**
	 * Returns the top-left Y screen location of the MinuetoWindow drawing surface
	 * 
	 * @since 1.1
	 * @return <code>int</code> denoting the y position of the drawing surface
	 */
	public abstract int getPositionY();

	/**
	 * Returns the number of frame renders per second. This is value 
	 * is updated every 50 frames.
	 *
	 * @since 0.6
	 *
	 * @return <code>int</code> denoting the number of frames drawn per second.
	 **/
	public abstract double getFrameRate();

	/**
	 * Caps the number of frames which can be rendered (display) per second. 
	 * The efficienty of the cap depends on the accuracy of the system timer.
	 * Calling this method with a value of 0 will disable the cap.
	 *
	 * @since 0.6
	 *
	 * @param value <code>double</code> denoting the desired number of frames to
	 * be displayed per second.
	 **/
	public abstract void setMaxFrameRate(double value);

	/**
	 * Assign a keyboard handler to the <code>MinuetoWindow</code> to process 
	 * keyboard input. An unlimited number of keyboard handlers can be registered
	 * with a window, but too many handlers may slow the application (because
	 * of the large number of events produced). 	 
	 *
	 * @param handler <code>MinuetoKeyboardHandler</code> to register with the 
	 * <code>MinuetoEventQueue</code>.
	 * @param queue <code>MinuetoEventQueue</code> that will store events..
	 * @since 0.8
	 **/
	public abstract void registerKeyboardHandler(
			MinuetoKeyboardHandler handler, MinuetoEventQueue queue);

	/**
	 * Assign a mouse handler to the <code>MinuetoWindow</code> to process 
	 * mouse input. An unlimited number of mouse handlers can be registered
	 * with a window, but too many handlers may slow the application (because
	 * of the large number of events produced). 
	 *
	 * @param handler <code>MinuetoMouseHandler</code> to register with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that will store events.
	 * @since 0.8
	 **/
	public abstract void registerMouseHandler(MinuetoMouseHandler handler,
			MinuetoEventQueue queue);

	/**
	 * Assign a window handler to the <code>MinuetoWindow</code> to process 
	 * window input. An unlimited number of window handlers can be registered
	 * with a window, but too many handlers may slow the application (because
	 * of the large number of events produced). 
	 *
	 * @param handler <code>MinuetoWindowHandler</code> to register with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that will store events.
	 * @since 0.8
	 **/
	public abstract void registerWindowHandler(MinuetoWindowHandler handler,
			MinuetoEventQueue queue);
	
	/**
	 * Assign a focus handler to the <code>MinuetoWindow</code> to process
	 * focus events.  An unlimited number of window handlers can be registered
	 * with a window, but too many handlers may slow the application (because
	 * of the large number of events produced).
	 * 
	 * @param handler <code>MinuetoFocusHandler</code> to register with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that will store events.
	 * @since 1.1
	 */
	public abstract void registerFocusHandler(MinuetoFocusHandler handler,
			MinuetoEventQueue queue);

	/**
	 * Assign a mousewheel  handler to the <code>MinuetoWindow</code> to process 
	 * mouse wheel input. An unlimited number of mouse wheel handlers can be registered
	 * with a window, but too many handlers may slow the application (because
	 * of the large number of events produced). 
	 *
	 * @param handler <code>MinuetoMouseWheelHandler</code> to register with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that will store events.
	 * @since 0.8
	 **/
	public abstract void registerMouseWheelHandler(
			MinuetoMouseWheelHandler handler, MinuetoEventQueue queue);

	/**
	 * Removes a keyboard handler from the <code>MinuetoWindow</code>. Note that
	 * both the correct handler and queue must be given to unregister the handler.
	 *
	 * @param handler <code>MinuetoKeyboardHandler</code> to unregister with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that stored the events.
	 * @since 1.0
	 **/
	public abstract void unregisterKeyboardHandler(
			MinuetoKeyboardHandler handler, MinuetoEventQueue queue);

	/**
	 * Removes a mouse handler from the <code>MinuetoWindow</code>. Note that
	 * both the correct handler and queue must be given to unregister the handler.
	 *
	 * @param handler <code>MinuetoMouseHandler</code> to unregister with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that stored the events.
	 * @since 1.0
	 **/
	public abstract void unregisterMouseHandler(MinuetoMouseHandler handler,
			MinuetoEventQueue queue);

	/**
	 * Removes a window handler from the <code>MinuetoWindow</code>. Note that
	 * both the correct handler and queue must be given to unregister the handler.
	 *
	 * @param handler <code>MinuetoWindowHandler</code> to unregister with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that stored the events.
	 * @since 1.0
	 **/
	public abstract void unregisterWindowHandler(MinuetoWindowHandler handler,
			MinuetoEventQueue queue);

	/**
	 * Removes a focus handler from the <code>MinuetoWindow</code>. Note that
	 * both the correct handler and queue must be given to unregister the handler.
	 *
	 * @param handler <code>MinuetoFocusHandler</code> to unregister with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that stored the events.
	 * @since 1.1
	 **/
	public abstract void unregisterFocusHandler(MinuetoFocusHandler handler,
			MinuetoEventQueue queue);
	
	/**
	 * Removes a mouse wheel handler from the <code>MinuetoWindow</code>. Note that
	 * both the correct handler and queue must be given to unregister the handler.
	 *
	 * @param handler <code>MinuetoMouseWheelHandler</code> to unregister with the 
	 * MinuetoWindow.
	 * @param queue <code>MinuetoEventQueue</code> that stored the events.
	 * @since 1.0
	 **/
	public abstract void unregisterMouseWheelHandler(
			MinuetoMouseWheelHandler handler, MinuetoEventQueue queue);

	/**
	 * Close the window and free any ressource still in use by the window.
	 * Once executed, this window can no longuer be used. Any
	 * further invocation of a method from this object will result in a 
	 * <code>MinuetoWindowInvalidStateException</code>.
	 */
	public abstract void close();

	/**
	 * Shows or hides the mouse cursor.
	 * 
	 * @param value <code>boolean</code> denoting if the window should be visible or not.
	 * @since 0.4.6
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is hidden 
	 *    (invisible) or has been closed.
	 */
	public abstract void setCursorVisible(boolean value);

	/**
	 * Change the title of the window.
	 * 
	 * @param title <code>String</code> identifying the desired title of the screen.
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is hidden 
	 *    (invisible) or has been closed.
	 */
	public abstract void setTitle(String title);

	/**
	 * Shows or hides the MinuetoWindow.
	 * 
	 * @param value <code>boolean</code> denoting if the window should be visible or not.
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is hidden 
	 *    (invisible) or has been closed.
	 */
	public abstract void setVisible(boolean value);

	
	/**
	 * Returns if the MinuetoWindow is visible or not.
	 * 
	 * @since 1.2
	 * @return <code>boolean</code> if Window is visible or not
	 */
	public abstract boolean isVisible();
	
	/**
	 * Returns if the MinuetoWindow is was closed.
	 * 
	 * @since 1.2
	 * @return <code>boolean</code> if Window was closed or not
	 */
	public abstract boolean isClosed();
	
}