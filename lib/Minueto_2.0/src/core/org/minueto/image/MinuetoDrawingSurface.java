package org.minueto.image;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;

/**
 * This interface denotes an object which can be drawn to with images or lines
 * As well as having a width and height, pxels can be accessed or set
 * It is also possible to save the image to a file.
 * 
 * This interface was created to solidy the similarities between a <code>MinuetoWindow</code>
 * and a <code>MinuetoImage</code>
 * 
 * @author Michael A. Hawker
 * @since 1.1
 * @see org.minueto.image.MinuetoImage
 * @see org.minueto.window.MinuetoBaseWindow
 */
public interface MinuetoDrawingSurface {

	/**
	 * Clears the contents of the Drawing Surface.  The color that remains
	 * depends on the JVM.
	 *
	 * @since 1.2
	 **/
	public abstract void clear();

	/**
	 * Erases the contents from the Drawing Surface. The color of the cleared surface is specified
	 * as a parameter.
	 * 
	 * @since 1.2
	 * @param color <code>MinuetoColor</code> of the cleared buffer.
	 **/
	public abstract void clear(MinuetoColor color);
	
	/**
	 * Change the color of pxel at location X,y.
	 * <p>
	 * This uses the drawLine function to be faster than a direct raw data setPxel,
	 * though it should be used with caution as it is still slower than drawing a
	 * completed image to the drawing surface.
	 *
	 * @since 1.1
	 * @param x <code>int</code> denoting which pxel on the X axis should be colored.
	 * @param y <code>int</code> denoting which pxel on the y axis should be colored.
	 * @param color <code>MinuetoColor</code> denoting the new color for pxel x,y.
	 */
	public abstract void setPixel(int x, int y, MinuetoColor color);

	/**
	 * Draw another <code>MinuetoImage</code> on this <code>MinuetoDrawingSurface</code> (thus compositing them).
	 *
	 * @param image <code>MinuetoImage</code> that we will draw on our image.
	 * @param x <code>int</code> denoting where we should start drawing on the x axis.
	 * @param y <code>int</code> denothing where we should start drawing on the y axis.
	 **/
	public abstract void draw(MinuetoImage image, int x, int y);

	/**
	 * Draw a line of the specified color from the start point to the end point.
	 *
	 * @param color <code>MinuetoColor</code> denoting the color of the line.
	 * @param xStart <code>int</code> denoting the X value of the start point.
	 * @param yStart <code>int</code> denoting the Y value of the start point.
	 * @param xStop <code>int</code> denoting the X value of the end point.
	 * @param yStop <code>int</code> denoting the Y value of the end point.
	 * @throws <code>MinuetoOutOfBoundException</code> if the target coordinates are not
	 * valid.
	 **/

	public abstract void drawLine(MinuetoColor color, int xStart,
			int yStart, int xStop, int yStop);

	/**
	 * Return the width of the image.
	 *
	 * @return	<code>int</code> denoting the width of the image.
	 **/
	public abstract int getWidth();

	/**
	 * Return the height of the image.
	 *
	 * @return	<code>int</code> denoting the height of the image.
	 **/
	public abstract int getHeight();

	/**
	 * Saves the content of the MinuetoDrawingSurface to a PNG file. Since this method
	 * requires File I/O, it is somewhat slow. However, this method can be
	 * used as a debugging tool.
	 *
	 * @param filename String indicating the name of the PNG file.
	 *
	 * @throws <code>MinuetoFileException</code> if it cannot write the file.
	 **/
	public abstract void save(String filename) throws MinuetoFileException;

}