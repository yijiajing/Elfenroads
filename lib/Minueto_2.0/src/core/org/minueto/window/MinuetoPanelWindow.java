package org.minueto.window;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.JPanel;

import org.minueto.image.ImageTools;
import org.minueto.image.MinuetoImage;

/**
 * This is the original <code>MinuetoPanel</code> class found from Minueto 1.0
 * It has been wrapped in a JPanel extender to provide both the functionality of
 * a JPanel and a MinuetoWindow.
 * 
 * The setVisible function has also been reworked to correctly setup the
 * graphic display surface automatically.
 * 
 * @author 	Alexandre Denault
 * @version 1.0
 * @since Minueto 1.0
 */
class MinuetoPanelWindow extends MinuetoBaseWindow {
	
	private JPanel jPanel;
	private Canvas canvas;
	private MinuetoImage buffer = null;

	public MinuetoPanelWindow(JPanel container, int width, int height) {
		super(width, height);
				
		GraphicsDevice			graphicDevice;
		GraphicsEnvironment 	graphicEnvironment;
				
		this.visible = false;
				
		graphicEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
		
		/* Minueto uses the default graphic environment. We can assume this because
		 * Minueto does not yet support dual display configuration. */
		graphicDevice = graphicEnvironment.getDefaultScreenDevice();
		graphicConfiguration = graphicDevice.getDefaultConfiguration();
		
		this.jPanel = container;
		this.jPanel.setSize(width, height);
		
		this.jPanel.setLayout(null);
		this.jPanel.setPreferredSize(new Dimension(width, height));
		this.jPanel.setFocusTraversalKeysEnabled(false);
					
		this.canvas = new Canvas(graphicConfiguration);
		this.canvas.setIgnoreRepaint(true);
		this.canvas.setBounds( 0, 0, width, height);
		this.canvas.setFocusTraversalKeysEnabled(false);			

		this.jPanel.add(this.canvas);			
		
		ImageTools.setGraphicConfiguration(graphicConfiguration);

		WindowManager.getInstance().register(this, false);
	}
	
	/**
	 * This method is called in the Minueto Panel constructor after the
	 * Panel has been constructed.
	 */
	protected void registerListeners() {
		// This will add to the canvas too
		this.jPanel.addKeyListener(this.getKeyListener());
		this.jPanel.addMouseListener(this.getMouseListener());
		this.jPanel.addMouseMotionListener(this.getMouseMotionListener());
		this.jPanel.addMouseWheelListener(this.getMinuetoMouseWheelListener());
		this.jPanel.addFocusListener(this.getFocusListener());
	}
	
	/**
	 * Used to initialized our graphics pointer to allow us to draw.
	 *
	 * Called by setVisible
	 */
	private void initialize() {
		
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
			
			WindowManager.getInstance().register(this, false);
			
			if (buffer != null) {
				draw(buffer, 0, 0);
				buffer = null;
			}
			
			this.visible = true;
		}
		
		
	}
	
	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionX()
	 */
	public int getPositionX() {			
		return this.canvas.getLocationOnScreen().x;
	}
	
	/* (non-Javadoc) 
	 * @see org.minueto.window.MinuetoWindow#getPositionY()
	 */
	public int getPositionY() {
		return this.canvas.getLocationOnScreen().y;
	}

	public synchronized void setVisible(boolean value) {
		
		if (visible == value) return;
					
		if (value) {
			visible = value;
			if (graphics2D == null) {
				initialize();
			}
		} else {
			if (graphics2D != null) {
				try {
					buffer = this.getScreenshot();
				} catch (IllegalComponentStateException exception) {
					// failed to capture screenshot
					// probably just disposing of the panel
				}
				this.graphics2D.dispose();
				this.graphics2D = null;	
		
				/*this.getKeyListener().clear();
				this.getMouseListener().clear();
				this.getMouseMotionListener().clear();
				this.getWindowListener().clear();*/
				
				WindowManager.getInstance().unregister(this);
			}
			visible = value;
		}		
	}
	
	public void close() {
		setVisible(false);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.window.MinuetoBaseWindow#setCursorVisible(boolean)
	 */
	public void setCursorVisible(boolean value) {
		if(value == false) {
			
			this.jPanel.setCursor(this.jPanel.getToolkit().createCustomCursor(										
					ImageTools.getBufferedImage(new MinuetoImage(3,3)),
					new Point(0, 0), "null"));
			
		} else {
			
			this.jPanel.setCursor(null);
		}
	}

	public void setTitle(String title) {
		// No Title on a panel			
	}

	protected synchronized void addFocusListener(FocusListener arg0) {
		canvas.addFocusListener(arg0);
	}

	protected synchronized void addKeyListener(KeyListener arg0) {
		canvas.addKeyListener(arg0);
	}

	protected synchronized void addMouseListener(MouseListener arg0) {
		canvas.addMouseListener(arg0);
	}

	protected synchronized void addMouseMotionListener(MouseMotionListener arg0) {
		canvas.addMouseMotionListener(arg0);
	}

	protected synchronized void addMouseWheelListener(MouseWheelListener arg0) {
		canvas.addMouseWheelListener(arg0);
	}

	protected synchronized void removeFocusListener(FocusListener arg0) {
		canvas.removeFocusListener(arg0);
	}

	protected synchronized void removeKeyListener(KeyListener arg0) {
		canvas.removeKeyListener(arg0);
	}

	protected synchronized void removeMouseListener(MouseListener arg0) {
		canvas.removeMouseListener(arg0);
	}

	protected synchronized void removeMouseMotionListener(MouseMotionListener arg0) {
		canvas.removeMouseMotionListener(arg0);
	}

	protected synchronized void removeMouseWheelListener(MouseWheelListener arg0) {
		canvas.removeMouseWheelListener(arg0);
	}

	public boolean isClosed() {

		return false;
	}
	


}
