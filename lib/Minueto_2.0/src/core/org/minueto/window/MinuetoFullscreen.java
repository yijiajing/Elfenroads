/*
 * @(#)MinuetoFullscreen.java 
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

package org.minueto.window;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;

import org.minueto.MinuetoZeroNegativeException;
import org.minueto.image.ImageTools;
import org.minueto.image.MinuetoImage;

/**
 * The <code>MinuetoFullscreen</code> class allows you to create a full screen window. 
 * This window is your main drawing surface (your canvas). The class is a singleton (only
 * one instance of <code>MinuetoFullscreen</code> can exist at any given time. This class 
 * is a subclass of <code>MinuetoWindow</code>. As such, all the drawing functionnality 
 * is found in the super class.
 *<p>
 * When a <code>MinuetoWindow</code> is created, it is invisible. The <code>setVisible</code>
 * method must be used toshow the window (make it visible).
 * <p>
 * The size of the <code>MinuetoWindow</code> must be given to the construtor.
 * Once a <code>MinuetoWindow</code> is created, it cannot be resized. To display
 * a MinuetoWindow of different size or attribute, the current window must be 
 * closed using the <code>close</code> method and a new <code>MinuetoWindow</code>
 * must be created. Please note that JRE 1.4.2 is known to crash when switch often between
 * fullscreen and windowed mode.
 * <p>
 * Minueto draws to the screen using a double-buffering strategy. In other words, 
 * there are two drawing surfaces : your window itself and an off screen surface.
 * All your drawing occurs on the off screen surface. The content of the off 
 * screen buffer is drawn to the screen when the <code>render</code> method is invoked.
 * <p>
 * 
 * @author Alexandre Denault
 * @version 1.0
 * @since  Minueto 1.0
 * @see   org.minueto.window.MinuetoWindow
 * @see   org.minueto.window.MinuetoBaseWindow
 * @see   org.minueto.image.MinuetoImage 
 */
public class MinuetoFullscreen extends MinuetoBaseWindow implements MinuetoWindow {

	private GraphicsDevice graphicDevice;
	private JFrame		  jFrame;

	/**
	 * Creates a MinuetoFullscreen of the specified size and color depth. You should use a standard 
	 * resolutions (such as 640x480, 800x600, 1024x768, 1280x1024, etc) since not all video cards 
	 * will allow you to use arbitrary resolutions.
	 *
	 * @param width <code>int</code> denoting the width of the desired resolution.
     * @param height <code>int</code> denoting the height of the desired resolution.
     * @param iColorDepth <code>int</code> denoting the colordepth of the desired resolution.
     * 
     * @throws MinuetoWindowAlreadyInitializedException if a MinuetoFullscreen was already created. 
     * @throws MinuetoUnsupportedDisplayModeException if the desired display mode is not available. 
     * @throws MinuetoZeroNegativeException if the window size is invalid.
	 */
	public MinuetoFullscreen(int width, int height, int iColorDepth) {
		
		super(width, height);

		GraphicsEnvironment 	graphicEnvironment;

		DisplayMode newDisplayMode;
		
		this.visible = false;
		
		graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		/* Minueto uses the default graphic environment. We can assume this because
		 * Minueto does not yet support dual display configuration. */
		graphicDevice = graphicEnvironment.getDefaultScreenDevice();
		graphicConfiguration = graphicDevice.getDefaultConfiguration();
				
		/* Our window is actually a JFrame that we use as a painting surface.*/
		this.jFrame = new JFrame(graphicConfiguration);

		this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.jFrame.setTitle("Minueto");
		this.jFrame.setResizable(false);
		this.jFrame.setFocusTraversalKeysEnabled(false);

		this.jFrame.setUndecorated(true);
		this.jFrame.getContentPane().setLayout(null);

		/* Minueto does its own painting, so we need to disable the Swing stuff. */
		this.jFrame.setIgnoreRepaint(true);

		try {

			/* Tries to find a display mode closest to what was requested. */
			newDisplayMode = new DisplayMode( width, height, iColorDepth,
			                          DisplayMode.REFRESH_RATE_UNKNOWN);

			this.graphicDevice.setFullScreenWindow(null);

			/* So we try to switch to fullscreen mode ... */
			this.graphicDevice.setFullScreenWindow(this.jFrame);
			/* ... and then the display mode ... */
			this.graphicDevice.setDisplayMode(newDisplayMode);
			/* ... and hope it works. */
		} catch(Exception e) {

			/* If we got here, the change to fullscreen obviously didnt work.
			 * We set the display mode back to window (if it hasn't switched 
			 * back automaticly ) and throw an exception. Don't be suprise if
			 * this doesn't work yet on Linux, it hasn't been implemented 
			 * yet. :-) */
			this.graphicDevice.setFullScreenWindow(null);
			this.jFrame.dispose();

			throw new MinuetoUnsupportedDisplayModeException();
		}
		
		this.jFrame.addKeyListener(this.getKeyListener());
		this.jFrame.addMouseListener(this.getMouseListener());
		this.jFrame.addMouseMotionListener(this.getMouseMotionListener());
		this.jFrame.addWindowListener(this.getWindowListener());
		this.jFrame.addMouseWheelListener(this.getMinuetoMouseWheelListener());
		this.jFrame.addFocusListener(this.getFocusListener());

		ImageTools.setGraphicConfiguration(graphicConfiguration);
		
		WindowManager.getInstance().register(this, true);
	}
	
	public synchronized void setVisible(boolean value) {

		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		/* We record if the screen is visible or not. This is important to
		 * remember because we need to know the visibility when resize if 
		 * called. */
		this.visible = value;

		/* Set the window to the proper size. */
		//this.jfrFrame.setSize(this.width, this.height);

		if (value == false) {

			/* Hides the window. */
			this.jFrame.setVisible(false);

		} else {

			
			/* Show the window */
			this.jFrame.setVisible(true);
			
			this.jFrame.setSize(	this.getWidth(), 
									this.getHeight());

			/* We need to initialized the double-buffer mode the first time we show the window */
			if (graphics2D == null) {

				/* Initialize the bufferstrategy for the frame, thus allowing us
				* to double buffer the screen. 
				* Note: The buffer strategy can only be created once the window
				* is visible.*/
				this.jFrame.createBufferStrategy(2);
				this.bufferStrategy = this.jFrame.getBufferStrategy();
			
				/* Create the Graphics Pointer we will need to draw on the screen.
				* This pointer needs to be recreated everytime we render (draw) the
				* screen. */
				this.graphics2D = (Graphics2D)this.bufferStrategy.getDrawGraphics();
			}
		}
	}
			
	/**
	 * Close the window and free any ressource still in use by the window.
	 * Once executed, this window can no longuer be used. Any
	 * further invocation of a method from this object will result in a 
	 * 
	 * <code>MinuetoWindowInvalidStateException</code>.
	 */
	public synchronized void close() {

		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		this.setVisible(false);

		this.graphicDevice.setFullScreenWindow(null);
		
		this.graphics2D.dispose();
		this.graphics2D = null;	
		
		this.jFrame.dispose();
		this.jFrame = null;	
		
		this.getKeyListener().clear();
		this.getMouseListener().clear();
		this.getMouseMotionListener().clear();
		this.getWindowListener().clear();
		
		WindowManager.getInstance().unregister(this);
	}
	
	/**
	 * Shows or hides the mouse cursor.
	 *
	 * @param visible <code>boolean</code> denoting if the window should be visible or not.
	 *
	 * @since 0.4.6
	 *
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window has been closed.  
	 */
	public synchronized void setCursorVisible(boolean visible)
	{

		// Code to hide the cursor taken from
		// http://www.rgagnon.com/javadetails/java-0440.html

		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		if(visible == false) {

			this.jFrame.setCursor(this.jFrame.getToolkit().createCustomCursor(										
										ImageTools.getBufferedImage(new MinuetoImage(3,3)),
			                            new Point(0, 0), "null"));

		} else {

			this.jFrame.setCursor(null);
		}

	}
	
	/**
	 * Change the title of the window.
	 *
	 * @param strTitle <code>String</code> identifying the desired title of 
	 * the screen.
	 *
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is hidden (invisible) or has been closed.
	 */
	public synchronized void setTitle(String strTitle) {

		if (strTitle == null) {
			new NullPointerException();
		}
		
		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		this.jFrame.setTitle(strTitle);
	}

	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionX()
	 */
	public int getPositionX() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return 0;
	}
	
	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionY()
	 */
	public int getPositionY() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return 0;
	}

	@Override
	public boolean isClosed() {

		return this.jFrame == null;
	}

}
