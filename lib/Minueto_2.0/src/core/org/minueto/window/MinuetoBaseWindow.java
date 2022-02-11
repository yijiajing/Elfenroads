/*
 * @(#)MinuetoWindow.java 
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

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.MinuetoFileException;
import org.minueto.MinuetoOutOfBoundException;
import org.minueto.MinuetoStopWatch;
import org.minueto.handlers.MinuetoFocusHandler;
import org.minueto.handlers.MinuetoFocusListener;
import org.minueto.handlers.MinuetoKeyListener;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.handlers.MinuetoMouseListener;
import org.minueto.handlers.MinuetoMouseMotionListener;
import org.minueto.handlers.MinuetoMouseWheelHandler;
import org.minueto.handlers.MinuetoMouseWheelListener;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.handlers.MinuetoWindowListener;
import org.minueto.image.ImageTools;
import org.minueto.image.MinuetoImage;

/**
 * The <code>MinuetoBaseWindow</code> abstract defines the methods available
 * to all classes that create a Minueto window (or drawing surface).
 * 
 * This class is new in 1.1, but was originally MinuetoWindow. MinuetoWindow
 * is now an interface which defines all this functionality. This change was
 * done for MinuetoPanel to be both a MinuetoWindow and a JPanel
 * 
 * @author Alexandre Denault
 * @version 1.0
 * @since 1.1
 * @see org.minueto.window.MinuetoFrame
 * @see org.minueto.window.MinuetoFullscreen
 * @see org.minueto.window.MinuetoPanel
 */
abstract class MinuetoBaseWindow implements MinuetoWindow {

	/**
	 * Reference to the drawing object. This needs to be updated after a page
	 * flip.
	 */
	protected Graphics2D graphics2D;

	/** Allows us to do double buffering. */
	protected BufferStrategy bufferStrategy;

	protected GraphicsConfiguration graphicConfiguration;

	/** Width of the screen */
	private int width;

	/** Height of the screen */
	private int height;

	/** Boolean value indicating if the screen should be visible. */
	protected boolean visible;

	/** This JFrame is our drawing window/surface. */
	// protected JFrame jfrFrame;
	private double timeBetweenFrames;

	private double maxFrameRate;

	private double currentFrameRate;

	private int frameCount;

	private MinuetoStopWatch stopWatchMaxFrameRate;

	private MinuetoStopWatch stopWatchCurrentFrameRate;

	private MinuetoKeyListener keyListener;

	private MinuetoMouseListener mouseListener;

	private MinuetoMouseMotionListener mouseMotionListener;

	private MinuetoMouseWheelListener mouseWheelListener;

	private MinuetoWindowListener windowListener;
	
	private MinuetoFocusListener focusListener;

	protected MinuetoBaseWindow(int width, int height) {

		/* Save the new attributes of the window. */
		this.width = width;
		this.height = height;

		this.timeBetweenFrames = 0;
		this.maxFrameRate = 0;
		this.currentFrameRate = 0;
		this.frameCount = 0;

		this.stopWatchCurrentFrameRate = new MinuetoStopWatch();
		this.stopWatchMaxFrameRate = new MinuetoStopWatch();

		this.stopWatchCurrentFrameRate.start();
		this.stopWatchMaxFrameRate.start();

		this.keyListener = new MinuetoKeyListener();
		this.mouseListener = new MinuetoMouseListener();
		this.mouseMotionListener = new MinuetoMouseMotionListener();
		this.mouseWheelListener = new MinuetoMouseWheelListener();
		this.windowListener = new MinuetoWindowListener();
		this.focusListener = new MinuetoFocusListener();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#draw(org.minueto.image.MinuetoImage,
	 *      int, int)
	 */
	public synchronized void draw(MinuetoImage image, int x, int y) {
		if (image == null)
			throw new NullPointerException();
		
		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		/* Uses the graphicpointer to draw the bufferedimage on the backbuffer. */
		this.graphics2D.drawImage(
				ImageTools.getBufferedImage(image), x, y, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#drawLine(org.minueto.MinuetoColor,
	 *      int, int, int, int)
	 */
	public synchronized void drawLine(MinuetoColor color, int xStart,
			int yStart, int xStop, int yStop) {

		if (color == null)
			new NullPointerException();
		
		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		int width = this.width;
		int height = this.height;

		if ((xStart < 0) || (xStart >= width))
			throw new MinuetoOutOfBoundException(xStart, 0, width);
		if ((xStop < 0) || (xStop >= width))
			throw new MinuetoOutOfBoundException(xStop, 0, width);
		if ((yStart < 0) || (yStart >= height))
			throw new MinuetoOutOfBoundException(yStart, 0, height);
		if ((yStop < 0) || (yStop >= height))
			throw new MinuetoOutOfBoundException(yStop, 0, height);

		this.graphics2D.setColor(color.getAWTColor());

		this.graphics2D.drawLine(xStart, yStart, xStop, yStop);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#render()
	 */
	public synchronized void render() {
		int timePause;
		
		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		this.frameCount++;

		if (this.frameCount == 50) {
			this.stopWatchCurrentFrameRate.stop();
			this.currentFrameRate = 50000.0 / this.stopWatchCurrentFrameRate
					.getTime();
			this.stopWatchCurrentFrameRate.reset();
			this.stopWatchCurrentFrameRate.start();
			this.frameCount = 0;
		}

		if (this.maxFrameRate > 0) {

			timePause = (int) (this.timeBetweenFrames - this.stopWatchMaxFrameRate
					.getTime());

			while (timePause > 10) {

				try {
					Thread.sleep(timePause / 2);
				} catch (InterruptedException ie) {

				}

				timePause = (int) (this.timeBetweenFrames - this.stopWatchMaxFrameRate
						.getTime());
			}

			while (this.stopWatchMaxFrameRate.getTime() < this.timeBetweenFrames) {

				Thread.yield();
			}
		}

		/* This should allow our fonts to be nicer, I think. */
		// this.graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		// RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		/* Releases the graphic pointer. We wont be needing it anymore. */
		this.graphics2D.dispose();

		/* Copies the backbuffer to the frontbuffer. */
		this.bufferStrategy.show();

		/*
		 * Sync the display on some systems. (on Linux, this fxes event queue
		 * problems).
		 */
		Toolkit.getDefaultToolkit().sync();

		/* Get a new graphicpointer */
		this.graphics2D = (Graphics2D) this.bufferStrategy
				.getDrawGraphics();

		this.stopWatchMaxFrameRate.reset();
		this.stopWatchMaxFrameRate.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#getWidth()
	 */
	public synchronized int getWidth() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		return this.width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#getHeight()
	 */
	public synchronized int getHeight() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		return this.height;
	}
	
	/**
	 * This method should only be called internally to resize the internally
	 * representation of the width and height.
	 * 
	 * If not used with caution, many errors can occur.
	 * 
	 * Currently, it is only called by MinuetoPanel as it is the only resizable
	 * drawing display.
	 * 
	 * @author Michael A. Hawker
	 * @since 1.1.1
	 * 
	 * @param width
	 * @param height
	 */
	protected synchronized void resizeWindow(int width, int height) {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		this.width = width;
		this.height = height;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#clear()
	 */
	public synchronized void clear() {

		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		/* Clear the whole screen. */
		this.graphics2D.clearRect(0, 0, this.width, this.height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#clear(org.minueto.MinuetoColor)
	 */
	public synchronized void clear(MinuetoColor color) {

		if (color == null)
			new NullPointerException();
		
		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		this.graphics2D.setColor(color.getAWTColor());

		/* Clear the whole screen. */
		this.graphics2D.fillRect(0, 0, this.width, this.height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#getFrameRate()
	 */
	public synchronized double getFrameRate() {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		return this.currentFrameRate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#setMaxFrameRate(double)
	 */
	public synchronized void setMaxFrameRate(double value) {

		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		if (value < 1.0)
			value = 0;

		this.maxFrameRate = value;
		this.timeBetweenFrames = 1000.0 / value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#registerKeyboardHandler(org.minueto.handlers.MinuetoKeyboardHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void registerKeyboardHandler(MinuetoKeyboardHandler handler,
			MinuetoEventQueue queue) {

		this.keyListener.register(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#registerMouseHandler(org.minueto.handlers.MinuetoMouseHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void registerMouseHandler(MinuetoMouseHandler handler,
			MinuetoEventQueue queue) {

		this.mouseListener.register(handler, queue);
		this.mouseMotionListener.register(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#registerWindowHandler(org.minueto.handlers.MinuetoWindowHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void registerWindowHandler(MinuetoWindowHandler handler,
			MinuetoEventQueue queue) {

		this.windowListener.register(handler, queue);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#registerFocusHandler(org.minueto.handlers.MinuetoFocusHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void registerFocusHandler(MinuetoFocusHandler handler,
			MinuetoEventQueue queue) {
		this.focusListener.register(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#registerMouseWheelHandler(org.minueto.handlers.MinuetoMouseWheelHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void registerMouseWheelHandler(MinuetoMouseWheelHandler handler,
			MinuetoEventQueue queue) {

		this.mouseWheelListener.register(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#unregisterKeyboardHandler(org.minueto.handlers.MinuetoKeyboardHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void unregisterKeyboardHandler(MinuetoKeyboardHandler handler,
			MinuetoEventQueue queue) {

		this.keyListener.unregister(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#unregisterMouseHandler(org.minueto.handlers.MinuetoMouseHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void unregisterMouseHandler(MinuetoMouseHandler handler,
			MinuetoEventQueue queue) {

		this.mouseListener.unregister(handler, queue);
		this.mouseMotionListener.unregister(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#unregisterWindowHandler(org.minueto.handlers.MinuetoWindowHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void unregisterWindowHandler(MinuetoWindowHandler handler,
			MinuetoEventQueue queue) {

		this.windowListener.unregister(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#unregisterFocusHandler(org.minueto.handlers.MinuetoFocusHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void unregisterFocusHandler(MinuetoFocusHandler handler,
			MinuetoEventQueue queue) {

		this.focusListener.unregister(handler, queue);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#unregisterMouseWeelHandler(org.minueto.handlers.MinuetoMouseWheelHandler,
	 *      org.minueto.MinuetoEventQueue)
	 */
	public void unregisterMouseWheelHandler(MinuetoMouseWheelHandler handler,
			MinuetoEventQueue queue) {

		this.mouseWheelListener.unregister(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#close()
	 */
	public abstract void close();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#setCursorVisible(boolean)
	 */
	public abstract void setCursorVisible(boolean value);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#setTitle(java.lang.String)
	 */
	public abstract void setTitle(String title);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.window.MinuetoWindow#setVisible(boolean)
	 */
	public abstract void setVisible(boolean value);
	
	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#isVisible()
	 */
	public boolean isVisible() {
		return visible;
	}

	protected MinuetoKeyListener getKeyListener() {

		return keyListener;
	}

	protected MinuetoMouseListener getMouseListener() {

		return mouseListener;
	}

	protected MinuetoMouseMotionListener getMouseMotionListener() {

		return mouseMotionListener;
	}

	protected MinuetoWindowListener getWindowListener() {

		return windowListener;
	}
	
	protected MinuetoFocusListener getFocusListener() {
		
		return focusListener;
	}

	protected MinuetoMouseWheelListener getMinuetoMouseWheelListener() {

		return mouseWheelListener;
	}

	/**
	 * Saves the content of the <code>MinuetoWindow</code> to a PNG file. This
	 * operation is very slow since the content of the screen must be retrieved
	 * from the video(card) memory.
	 * 
	 * @author Alexandre Denault
	 * @param filename
	 *            <code>String</code> indicating the name of the target PNG
	 *            file.
	 * 
	 * @throws <code>MinuetoFileException</code> if it cannot write the file.
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is
	 *             hidden (invisible) or has been closed.
	 * 
	 * @since 1.1
	 * @see org.minueto.image.MinuetoDrawingSurface#save(java.lang.String)
	 */
	public void save(String filename) throws MinuetoFileException {
		
		if (filename == null) {
			new NullPointerException();
		}
		
		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		this.getScreenshot().save(filename);
	}

	/**
	 * Returns a <code>MinuetoImage</code> containing the image of the
	 * <code>MinuetoWindow</code>. This operation is very slow since the
	 * content of the screen must be retrieved from the video(card) memory.
	 * 
	 * @return <code>MinuetoImage</code> containing the image currently
	 *         displayed on-screen.
	 * @throws <code>MinuetoWindowInvalidStateException</code> if this window is
	 *             hidden (invisible) or has been closed.
	 **/
	protected synchronized MinuetoImage getScreenshot() {

		BufferedImage bufUnacceleratedImage;
		Rectangle areaToCapture;

		if (this.visible == false) {
			throw new MinuetoWindowInvalidStateException("Window is invisible");
		}
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}

		areaToCapture = new Rectangle(this.getPositionX(), this.getPositionY(), 
										 this.width, this.height);

		try {

			bufUnacceleratedImage = new Robot()
					.createScreenCapture(areaToCapture);

		} catch (Exception e) {
			return null;
		}

		return new MinuetoImage(bufUnacceleratedImage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.minueto.image.MinuetoDrawingSurface#setPxel(int, int,
	 *      org.minueto.MinuetoColor)
	 */
	public void setPixel(int x, int y, MinuetoColor color) {
		
		if(this.isClosed()) {
			throw new MinuetoWindowInvalidStateException("Window is closed!");
		}
		
		drawLine(color, x, y, x, y);
	}

}