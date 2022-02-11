/*
 * @(#)MinuetoFrame.java 
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

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.minueto.MinuetoOutOfBoundException;
import org.minueto.image.ImageTools;
import org.minueto.image.MinuetoImage;

/**
 * The <code>MinuetoPanel</code> class allows you to create a window (not fullscreen). 
 * This window is your main drawing surface (your canvas). This class is a subclass of 
 * <code>MinuetoWindow</code>. As such, all the drawing functionnality is found in the
 * super class.
 *<p>
 * When a <code>MinuetoWindow</code> is created, it is invisible. The <code>setVisible</code>
 * method must be used toshow the window (make it visible).
 * <p>
 * The size of the <code>MinuetoWindow</code> must be given to the construtor.
 * Once a <code>MinuetoWindow</code> is created, it cannot be resized. To display
 * a MinuetoWindow of different size, the current window must be closed using the 
 * <code>close</code> method and a new <code>MinuetoWindow</code> must be created. 
 * <p>
 * Minueto draws to the screen using a double-buffering strategy. In other words, 
 * there are two drawing surfaces : your window itself and an off screen surface.
 * All your drawing occurs on the off screen surface. The content of the off 
 * screen buffer is drawn to the screen when the <code>render</code> method is invoked.
 * <p>
 * 
 * @author Alexandre Denault
 * @version 1.0
 * @since  Minueto 0.4
 * @see   org.minueto.window.MinuetoWindow
 * @see   org.minueto.window.MinuetoBaseWindow
 * @see   org.minueto.image.MinuetoImage
 */
public class MinuetoFrame extends MinuetoBaseWindow implements MinuetoWindow {
	
	private JFrame		  jFrame;
	private JPanel        jPanel;
	private Canvas        canvas;	
	
	/**
	 * Creates a <code>MinuetoWindow</code> of the specified size. The second parameter 
	 * determines if the window should be bordeless or not. 
	 *  
	 * @param width <code>int</code> denoting the desired width of the window.
	 * @param height <code>int</code> denoting the desired height of the window.
	 * @param border <code>boolean</code> indicating if the window should be have borders or not. 	 
	 *
	 * @throws <code>MinuetoZeroNegativeException</code> if the window size is invalid.
	 */
	public MinuetoFrame(int width, int height, boolean border) {
		
		super(width, height);
		
		GraphicsDevice			graphicDevice;
		GraphicsEnvironment 	graphicEnvironment;
		
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
		this.jFrame.setUndecorated(!border);
		
		this.jFrame.addKeyListener(this.getKeyListener());
		this.jFrame.addMouseListener(this.getMouseListener());
		this.jFrame.addMouseMotionListener(this.getMouseMotionListener());
		this.jFrame.addWindowListener(this.getWindowListener());
		this.jFrame.addMouseWheelListener(this.getMinuetoMouseWheelListener());
		this.jFrame.addFocusListener(this.getFocusListener());
		
		this.jPanel = (JPanel)this.jFrame.getContentPane();
		
		this.jPanel.setLayout(null);
		this.jPanel.setPreferredSize(new Dimension(width, height));
		this.jPanel.setFocusTraversalKeysEnabled(false);
		
		this.jPanel.addKeyListener(this.getKeyListener());
		this.jPanel.addMouseListener(this.getMouseListener());
		this.jPanel.addMouseMotionListener(this.getMouseMotionListener());
		this.jPanel.addMouseWheelListener(this.getMinuetoMouseWheelListener());
		this.jPanel.addFocusListener(this.getFocusListener());
		
		this.canvas = new Canvas(graphicConfiguration);
		this.canvas.setIgnoreRepaint(true);
		this.canvas.setBounds( 0, 0, width, height);
		this.canvas.setFocusTraversalKeysEnabled(false);
		
		this.canvas.addKeyListener(this.getKeyListener());
		this.canvas.addMouseListener(this.getMouseListener());
		this.canvas.addMouseMotionListener(this.getMouseMotionListener());
		this.canvas.addMouseWheelListener(this.getMinuetoMouseWheelListener());
		this.canvas.addFocusListener(this.getFocusListener());
		
		this.jPanel.add(this.canvas);
		
		ImageTools.setGraphicConfiguration(graphicConfiguration);
		
		WindowManager.getInstance().register(this, false);
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
			
			this.jFrame.pack();
			
			/* We need to initialized the double-buffer mode the first time we show the window */
			if (graphics2D == null) {
				
				/* Initialize the bufferstrategy for the frame, thus allowing us
				 * to double buffer the screen. 
				 * Note: The buffer strategy can only be created once the window
				 * is visible.*/
				this.canvas.createBufferStrategy(2);
				this.bufferStrategy = this.canvas.getBufferStrategy();
				
				/* Create the Graphics Pointer we will need to draw on the screen.
				 * This pointer needs to be recreated everytime we render (draw) the
				 * screen. */
				this.graphics2D = (Graphics2D)this.bufferStrategy.getDrawGraphics();	
			}			
		}		
	}
	
	public synchronized void close() {
		//this.render();
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		this.setVisible(false);
		
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
	
	public synchronized void setTitle(String title) {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		if (title == null)
			new NullPointerException();
		if (this.visible == false)
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		
		this.jFrame.setTitle(title);
	}
	
	/**
	 * Returns the width of the current window. Please note that
	 * the width returned is the actual width of the window, not
	 * the requested width. This is important to take into acccount because
	 * operating systems such as Window add a thin border around every window.
	 * To properly center a window, the actual width of the window must
	 * be known.
	 *
	 * @return <code>int</code> denoting the real width of the window.
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is hidden (invisible) or has been closed.
	 */
	public synchronized int getWindowWidth() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return this.jFrame.getWidth();
	}
	
	/**
	 * Returns the height of the current window. Please note that
	 * the height returned is the actual height of the window, not
	 * the requested height.  This is important to take into acccount because
	 * operating systems such as Window add a thin border around every window.
	 * To properly center a window, the actual width of the window must
	 * be known.
	 *
	 * @return <code>int</code> denoting the real height of the window.
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is hidden (invisible) or has been closed.  
	 */
	public synchronized int getWindowHeight() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return this.jFrame.getHeight();
	}
	
	/**
	 * Returns the current X position of this window on the desktop. A value of
	 * 0 is returned if the window is in fullscreen mode.
	 */
	public synchronized int getWindowPositionX(){
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return this.jFrame.getX();
	}
	
	/**
	 * Returns the current Y position of this window. A value of 0 is returned
	 * if the window is in fullscreen mode.
	 */
	public synchronized int getWindowPositionY() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return this.jFrame.getY();
	}
	
	/**
	 * Moves the window to the desired location. This method is ignored if Minueto is
	 * fullscreen mode.
	 *
	 * @param x <code>int</code> denoting the X position where the window should be moved.
	 * @param y <code>int</code> denoting the Y position where the window should be moved.
	 *
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is hidden (invisible) or has been closed.
	 */
	public synchronized void setWindowPosition(int x, int y) {
		
		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		if ( x < 0 )
			throw new MinuetoOutOfBoundException(x, 0, 999999);
		if ( y < 0 )
			throw new MinuetoOutOfBoundException(y, 0, 999999);
		
		this.jFrame.setLocation(x, y);
	}
	
	/**
	 * By default, when a MinuetoWindow is closed by the operating system (click on 
	 * the X in the top right corner for example), the whole application is closed.
	 * This method allows you to change this behavior.
	 * 
	 * @param value <code>boolean</code> denoting if the whole application should be closed.
	 */
	public synchronized void exitOnClose(boolean value) {
		
		if(value) {
			this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else {
			this.jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}
	}
	
	
	public synchronized void setCursorVisible(boolean visible) {
		
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

	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionX()
	 */
	public int getPositionX() {
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return this.canvas.getLocationOnScreen().x;
	}
	
	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionY()
	 */
	public int getPositionY() {
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		return this.canvas.getLocationOnScreen().y;
	}
	
	@Override
	public boolean isClosed() {

		return this.jFrame == null;
	}
	
}
