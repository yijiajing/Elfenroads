/*
 * @(#)MinuetoPanel.java 
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoEventQueue;
import org.minueto.MinuetoFileException;
import org.minueto.handlers.MinuetoFocusHandler;
import org.minueto.handlers.MinuetoKeyboardHandler;
import org.minueto.handlers.MinuetoMouseHandler;
import org.minueto.handlers.MinuetoMouseWheelHandler;
import org.minueto.handlers.MinuetoWindowHandler;
import org.minueto.image.MinuetoImage;

/**
 * The <code>MinuetoPanel</code> class allows you to create a Panel which can be inserted
 * inside your Swing/AWT application. This panel is your main drawing surface (your canvas).
 * It is also a JPanel.  This allows for easy swing integration by adding it like any other control.
 * The only difference is that you must pass the width and height for your canvas size first.
 * <p>
 * This class is also implements <code>MinuetoWindow</code>. As such, all the drawing 
 * functionnality is provided as well.
 * <p>
 * If you were using the old version of this class, minimal changes should be needed:
 * You just need to add it like a regular JPanel and calling enable is no longer needed.  Just
 * call setVisible(true).
 * <p>
 * When a <code>MinuetoPanel</code> is created, it is invisible. The <code>setVisible</code>
 * method must be used to show the window (make it visible).
 * <p>
 * Minueto draws to the screen using a double-buffering strategy. In other words, 
 * there are two drawing surfaces : your window itself and an off screen surface.
 * All your drawing occurs on the off screen surface. The content of the off 
 * screen buffer is drawn to the screen when the <code>render</code> method is invoked.
 * <p>
 * As Minueto does complex Graphical work, it isn't really meant to interact with
 * Swing that nicely.  While the MinuetoPanel takes care of actually allowing
 * you to draw in an accelerated fashion while contained within Swing components.
 * The Panel itself doesn't know about Swing.  Therefore, it is drawn completely
 * on top of any other swing component.  Thus, windows or buttons that are in front
 * of the MinuetoPanel will be drawn behind it.
 * 
 * @author  Michael A. Hawker
 * @version 3.0
 * @since  	1.1
 * @see 	org.minueto.window.MinuetoBaseWindow
 * @see 	org.minueto.window.MinuetoWindow
 * @see   	org.minueto.image.MinuetoImage
 */
public class MinuetoPanel extends JPanel implements MinuetoWindow {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1073698193200246677L;
	
	// Private Window Panel class we're wrapping
	private MinuetoPanelWindow window;
	private boolean autoRepaint;

	public MinuetoPanel(int width, int height) {
		window = new MinuetoPanelWindow(this, width, height);
		window.registerListeners();
		autoRepaint = false;
	}
	
	/*public void registerListeners(Container container) {
		window.registerListeners(container);
	}*/
		
	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#clear()
	 */
	public void clear() {
		window.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#clear(org.minueto.MinuetoColor)
	 */
	public void clear(MinuetoColor color) {
		window.clear(color);
	}

	/**
	 * Closing a MinuetoPanel doesn't really make too much sense.
	 * 
	 * In this case we'll use it as an Alias for setVisible(false)
	 */
	public void close() {
		window.close();
		WindowManager.getInstance().unregister(this);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#draw(org.minueto.image.MinuetoImage, int, int)
	 */
	public void draw(MinuetoImage image, int x, int y) {
		window.draw(image, x, y);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#drawLine(org.minueto.MinuetoColor, int, int, int, int)
	 */
	public void drawLine(MinuetoColor color, int xStart, int yStart, int xStop, int yStop) {
		window.drawLine(color, xStart, yStart, xStop, yStop);
	}

	/* (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#getFrameRate()
	 */
	public double getFrameRate() {
		return window.getFrameRate();
	}

	/* (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#getHeight()
	 */
	public int getHeight() {
		return window.getHeight();
	}

	/* (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#getWidth()
	 */
	public int getWidth() {
		return window.getWidth();
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#registerKeyboardHandler(org.minueto.handlers.MinuetoKeyboardHandler, org.minueto.MinuetoEventQueue)
	 */
	public void registerKeyboardHandler(MinuetoKeyboardHandler handler, MinuetoEventQueue queue) {
		window.registerKeyboardHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#registerMouseHandler(org.minueto.handlers.MinuetoMouseHandler, org.minueto.MinuetoEventQueue)
	 */
	public void registerMouseHandler(MinuetoMouseHandler handler, MinuetoEventQueue queue) {
		window.registerMouseHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#registerMouseWheelHandler(org.minueto.handlers.MinuetoMouseWheelHandler, org.minueto.MinuetoEventQueue)
	 */
	public void registerMouseWheelHandler(MinuetoMouseWheelHandler handler, MinuetoEventQueue queue) {
		window.registerMouseWheelHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#registerWindowHandler(org.minueto.handlers.MinuetoWindowHandler, org.minueto.MinuetoEventQueue)
	 */
	public void registerWindowHandler(MinuetoWindowHandler handler, MinuetoEventQueue queue) {
		throw new UnsupportedOperationException("MinuetoPanel's are not windows and thus do not have window like events, try using MinuetoFocuseHandler instead.");
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#registerFocusHandler(org.minueto.handlers.MinuetoFocusHandler, org.minueto.MinuetoEventQueue)
	 */
	public void registerFocusHandler(MinuetoFocusHandler handler, MinuetoEventQueue queue) {
		window.registerFocusHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#render()
	 */
	public void render() {
		window.render();		
		
		//window.jPanel.setBackground(Color.BLUE);
		
		// Since we know we're in a swing app, we need to yield
		// to allow swing to process events.
		Thread.yield();
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#setCursorVisible(boolean)
	 */
	public void setCursorVisible(boolean value) {
		window.setCursorVisible(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#setMaxFrameRate(double)
	 */
	public void setMaxFrameRate(double value) {
		window.setMaxFrameRate(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		window.setTitle(title);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#isVisible()
	 */
	public boolean isVisible() {
		return window.isVisible();
	}

	/* (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#setVisible(boolean)
	 */
	public void setVisible(boolean arg0) {
		window.setVisible(arg0);
		super.setVisible(arg0);		
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#unregisterKeyboardHandler(org.minueto.handlers.MinuetoKeyboardHandler, org.minueto.MinuetoEventQueue)
	 */
	public void unregisterKeyboardHandler(MinuetoKeyboardHandler handler, MinuetoEventQueue queue) {
		window.unregisterKeyboardHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#unregisterMouseHandler(org.minueto.handlers.MinuetoMouseHandler, org.minueto.MinuetoEventQueue)
	 */
	public void unregisterMouseHandler(MinuetoMouseHandler handler, MinuetoEventQueue queue) {
		window.unregisterMouseHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#unregisterMouseWheelHandler(org.minueto.handlers.MinuetoMouseWheelHandler, org.minueto.MinuetoEventQueue)
	 */
	public void unregisterMouseWheelHandler(MinuetoMouseWheelHandler handler, MinuetoEventQueue queue) {
		window.unregisterMouseWheelHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#unregisterWindowHandler(org.minueto.handlers.MinuetoWindowHandler, org.minueto.MinuetoEventQueue)
	 */
	public void unregisterWindowHandler(MinuetoWindowHandler handler, MinuetoEventQueue queue) {
		window.unregisterWindowHandler(handler, queue);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoWindow#unregisterFocusHandler(org.minueto.handlers.MinuetoFocusHandler, org.minueto.MinuetoEventQueue)
	 */
	public void unregisterFocusHandler(MinuetoFocusHandler handler, MinuetoEventQueue queue) {
		window.unregisterFocusHandler(handler, queue);		
	}

	
	/* (non-Javadoc)
	 * @see java.awt.Component#addFocusListener(java.awt.event.FocusListener)
	 */
	public synchronized void addFocusListener(FocusListener arg0) {
		super.addFocusListener(arg0);
		window.addFocusListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#addKeyListener(java.awt.event.KeyListener)
	 */
	public synchronized void addKeyListener(KeyListener arg0) {
		super.addKeyListener(arg0);
		window.addKeyListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#addMouseListener(java.awt.event.MouseListener)
	 */
	public synchronized void addMouseListener(MouseListener arg0) {
		super.addMouseListener(arg0);
		window.addMouseListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#addMouseMotionListener(java.awt.event.MouseMotionListener)
	 */
	public synchronized void addMouseMotionListener(MouseMotionListener arg0) {
		super.addMouseMotionListener(arg0);
		window.addMouseMotionListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#addMouseWheelListener(java.awt.event.MouseWheelListener)
	 */
	public synchronized void addMouseWheelListener(MouseWheelListener arg0) {
		super.addMouseWheelListener(arg0);
		window.addMouseWheelListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#removeFocusListener(java.awt.event.FocusListener)
	 */
	public synchronized void removeFocusListener(FocusListener arg0) {
		super.removeFocusListener(arg0);
		window.removeFocusListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#removeKeyListener(java.awt.event.KeyListener)
	 */
	public synchronized void removeKeyListener(KeyListener arg0) {
		super.removeKeyListener(arg0);
		window.removeKeyListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#removeMouseListener(java.awt.event.MouseListener)
	 */
	public synchronized void removeMouseListener(MouseListener arg0) {
		super.removeMouseListener(arg0);
		window.removeMouseListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#removeMouseMotionListener(java.awt.event.MouseMotionListener)
	 */
	public synchronized void removeMouseMotionListener(MouseMotionListener arg0) {
		super.removeMouseMotionListener(arg0);
		window.removeMouseMotionListener(arg0);
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#removeMouseWheelListener(java.awt.event.MouseWheelListener)
	 */
	public synchronized void removeMouseWheelListener(MouseWheelListener arg0) {
		super.removeMouseWheelListener(arg0);
		window.removeMouseWheelListener(arg0);
	}

	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#save(String filename)
	 */
	public void save(String filename) throws MinuetoFileException {
		window.save(filename);		
	}

	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#setPxel(int x, int y, MinuetoColor color()
	 */
	public void setPixel(int x, int y, MinuetoColor color) {
		window.setPixel(x, y, color);
	}

	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionX()
	 */
	public int getPositionX() {
		return window.getPositionX();
	}
	
	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionY()
	 */
	public int getPositionY() {
		return window.getPositionY();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#getPreferredSize()
	 */
	public Dimension getPreferredSize() {
		return new Dimension(window.getWidth(), window.getHeight());
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#setSize(java.awt.Dimension)
	 */
	public void setSize(Dimension dimension) {
		this.setSize((int)dimension.getWidth(), (int)dimension.getHeight());
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics arg0) {
		super.paint(arg0);
		
		if (autoRepaint && window != null && window.isVisible()) {
			window.render();
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#update(java.awt.Graphics)
	 */
	public void update(Graphics arg0) {
		// TODO Auto-generated method stub
		super.update(arg0);
		
//		if (autoRepaint && window != null && window.isVisible()) {
//			window.render();
//		}
	}

	@Override
	public boolean isClosed() {
		
		return false;
	}
}
