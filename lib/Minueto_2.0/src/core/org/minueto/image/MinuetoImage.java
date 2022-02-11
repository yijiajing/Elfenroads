/*
 * @(#)MinuetoImage.java
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

package org.minueto.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoFileException;
import org.minueto.MinuetoOutOfBoundException;
import org.minueto.MinuetoTool;
import org.minueto.MinuetoZeroNegativeException;
import org.minueto.window.MinuetoUnsupportedDisplayModeException;

/**
 * The <code>MinuetoImage</code> class describes an image that can be drawn on a 
 * <code>MinuetoWindow</code>. All other types of images used by Minueto are subclasses
 * of <code>MinuetoImage</code>.
 * <p>
 * Basic 2D image transformations (such as crop, scale, rotate) can be executed
 * on any <code>MinuetoImage</code>. These transformation produce new <code>MinuetoImage</code> that can
 * be manipulated independently from their original image.Please note that these
 * transformations are slow and should not be used in realtime. Results from 
 * these operations should be preferably cached in advanced.
 * <p>
 * Images are created at the same colordepth as <code>MinuetoWindow</code>. When created, a
 * <code>MinuetoImage</code> is transparent. Other <code>MinuetoImage</code> can be draw over it, thus
 * allowing you to build more complexe composite images.
 * <p>
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 **/

public class MinuetoImage implements Cloneable, MinuetoDrawingSurface {

	private BufferedImage	bufferedImage;
	private Graphics2D 		graphics2D;
	private int				width;
	private int				height;
	private boolean			accelerated;

	/**
	 * Create a blank (transparent) image of the specified width and height.
	 *
	 * @param x <code>int</code> int denoting the width of the blank image.
	 * @param y <code>int</code< int denoting the height of the blank image.
	 * @throws <code>MinuetoZeroNegativeException</code> if the image size is invalid.
	 **/
	public MinuetoImage(int x, int y) {

		if (x < 1) throw new MinuetoZeroNegativeException("Cannot create image of width " + width);
		if (y < 1) throw new MinuetoZeroNegativeException("Cannot create image of height " + height);

		this.setUpImage(ImageTools.createImage(x, y));
	}

	/**
	 * Create an image using an existing buffered image.
	 *
	 * @param image <code>BufferedImage</code> witht he content of the image. 
	 **/
	public MinuetoImage(BufferedImage image) {
		
		this.setUpImage(image);
	}
	
	/**
	 * Protected constructor used by the package to create a <code>MinuetoImage</code>
	 * without a buffered image. The buffered image will be latter added
	 * by the <code>setUpImage</code> method.
	 **/
	protected MinuetoImage() {

	}

	/**
	 * Protected method used to set up a <code>MinuetoImage</code>. All <code>MinuetoImage</code> will,
	 * at one point in their lifetime, go throught this method.
	 *
	 * @param	bufferedImage <code>BufferedImage</code> storing the content of the
	 *				image.
	 **/
	protected void setUpImage(BufferedImage bufferedImage) {

		if (bufferedImage == null) new NullPointerException();

		this.bufferedImage = bufferedImage;

		this.width 		= this.bufferedImage.getWidth();
		this.height 		= this.bufferedImage.getHeight();
		this.graphics2D 	= this.bufferedImage.createGraphics();
		
		this.accelerateBuffer();
	}

	/**
	 * Return a cropped copy of this <code>MinuetoImage</code>. The action of cropping an
	 * image can be described as copying a portion of an image to a new image.
	 *
	 * @param cornerX <code>int</code> denoting the X coordinate of the upper left corner
	 *                 of the old image to be used as the upper left corner of the new image..
	 * @param cornerY <code>int</code> denoting the Y coordinate of the upper left corner.
	 *                 of the old image to be used as the upper left corner of the new image.
	 * @param sizeX <code>int</code> denoting the width of the portion of the old image to
	 *                 copy in the new image.
	 * @param sizeY <code>int</code> denoting the height of the portion of the old image to
	 *                 copy in the new image.
	 * @return <code>MinuetoImage</code> of the cropped image.
	 * @throws <code>MinuetoZeroNegativeException</code> if an invalid position or size is used.
	 * @throws <code>MinuetoOutOfBoundException</code> if a
	 **/
	public MinuetoImage crop(int cornerX, int cornerY, int sizeX, int sizeY) {

		/* Resulting MinuetoImage */
		MinuetoImage	destinationImage;
		
		if (cornerX < 0) throw new MinuetoZeroNegativeException("Cannot start crop at X position " + cornerX);
		if (cornerY < 0) throw new MinuetoZeroNegativeException("Cannot start crop at Y position " + cornerY);
		if (sizeX < 0) throw new MinuetoZeroNegativeException("Cannot end crop at X position " + sizeX);
		if (sizeY < 0) throw new MinuetoZeroNegativeException("Cannot end crop at Y position " + sizeY);

		if (cornerX >= this.width) throw new MinuetoOutOfBoundException("Cannot start crop at X position " + cornerX);
		if (cornerY >= this.height) throw new MinuetoOutOfBoundException("Cannot start crop at Y position " + cornerY);
		if ((cornerX+sizeX) > this.width) throw new MinuetoOutOfBoundException("Cannot end crop at X position " + (cornerX+sizeX));
		if ((cornerY+sizeY) > this.height) throw new MinuetoOutOfBoundException("Cannot end crop at Y position " + (cornerY+sizeY));

		destinationImage = new MinuetoImage(sizeX, sizeY);

		destinationImage.graphics2D.drawImage(this.getImage().getSubimage(cornerX, cornerY, sizeX, sizeY),0,0,null);

		return destinationImage;
	}

	/**
	 * Return a rotated copy of this <code>MinuetoImage</code>. The angle must be supplied 
	 * in radian.
	 *
	 * @param dAngle <code>double</code> denoting the rotation angle in radian.
	 * @return <code>MinuetoImage</code> of the rotated image.
	 **/
	public MinuetoImage rotate(double dAngle) {

		/* Reference to the destination image. */
		BufferedImage destinationBufferedImage;
		/* Reference to the affine transformation (the rotation) */
		AffineTransform affineTransform;
		/* Reference to the GraphicPointer */
		Graphics2D graphics2DDestination;
		/* Resulting MinuetoImage */
		MinuetoImage	destinationImage;
		/* Size of the resulting MinuetoImage */
		int	newHeight;
		int newWidth;
		
		
		double x1,x2,x3,y1,y2,y3,xMin,xMax,yMin,yMax, cosAngle, sinAngle;
		
		/* Set up the affine transformation needed to rotate the image */
		affineTransform = new AffineTransform();
		
		/* Rotate the image by dAngle, centered on width/2 and height/2 */
		affineTransform.rotate(dAngle, (double)this.width/2.0, (double)this.height/2.0);		

		cosAngle = Math.cos(dAngle);
		sinAngle = Math.sin(dAngle);

		/* Get the size of our new image */
		/**
		* Before simplification
		* x0 = 0 * cosAngle - 0 * sinAngle;
		* x1 = 0 * cosAngle - this.height * sinAngle;
		* x2 = this.width * cosAngle - 0 * sinAngle;
		* x3 = this.width * cosAngle - this.height * sinAngle;
		*
		* y0 = 0 * sinAngle + 0 * cosAngle;
		* y1 = 0 * sinAngle + this.height * cosAngle;
		* y2 = this.width * sinAngle + 0 * cosAngle;
		* y3 = this.width * sinAngle + this.height * cosAngle;
		*
		* xMin = Math.min( Math.min(x0, x1), Math.min(x2, x3) );
		* xMax = Math.max( Math.max(x0, x1), Math.max(x2, x3) );
		*
		* yMin = Math.min( Math.min(y0, y1), Math.min(y2, y3) );
		* yMax = Math.max( Math.max(y0, y1), Math.max(y2, y3) ); 
		**/
		
		x1 = - this.height * sinAngle;
		x2 = this.width * cosAngle;
		x3 = x2 + x1;
		
		y1 = this.height * cosAngle;
		y2 = this.width * sinAngle;
		y3 = y2 + y1;
		
		xMin = Math.min( Math.min(0, x1), Math.min(x2, x3) );
		xMax = Math.max( Math.max(0, x1), Math.max(x2, x3) );
		
		yMin = Math.min( Math.min(0, y1), Math.min(y2, y3) );
		yMax = Math.max( Math.max(0, y1), Math.max(y2, y3) );
		
		newWidth = (int)Math.ceil(Math.abs(xMax - xMin));
		newHeight = (int)Math.ceil(Math.abs(yMax - yMin));
		
		newWidth = (int)Math.ceil(Math.abs(xMax - xMin));
		newHeight = (int)Math.ceil(Math.abs(yMax - yMin));
		
		//System.out.println("Debug: " + newWidth + " , " + newHeight + " , " +  dAngle);
		
		/* The rotation normaly produces an unaccelerated buffered image.
		 * Instead, we produce an accelerated buffered for the result.
		 * The maximum size this image can be is 141% larger than the original.*/
		destinationBufferedImage = ImageTools.createImage(newWidth, newHeight);
		/* Reference to the GraphicPointer of the destination image.*/
		graphics2DDestination = destinationBufferedImage.createGraphics();
		
		/* Make sure we will draw in the middle of our new image. */
		graphics2DDestination.translate( (this.width-newWidth)/-2, (this.height-newHeight)/-2 );
		
		/* Draw the rotated image. */
		graphics2DDestination.drawImage(	this.bufferedImage,affineTransform,
											null);

		/* Set up the new MinuetoImage with the rotated image. */
		destinationImage = new MinuetoImage();
		destinationImage.setUpImage(destinationBufferedImage);

		return destinationImage;
	}

	/**
	 * Return a scaled copy of this <code>MinuetoImage</code>. The parameters are described as 
	 * scale factors. Thus, calling the scale function with the 2,2 parameters 
	 * would create a image of double width and height.
	 *
	 * @author Michael A. Hawker
	 * @param factorX <code>double</code> denoting how the image should be scaled on the X axis.
	 * @param factorY <code>double</code> denoting how the image should be scaled on the Y axis.
	 * @return <code>MinuetoImage</code> of the scaled image.
	 * @throws <code>MinuetoZeroNegativeException</code> if a wrong scale factor is used.
	 **/	
	public MinuetoImage scale(double factorX, double factorY) {

		if (factorX > 0 && factorY > 0) {
			return this.scaleInternal(factorX, factorY);
		} else {
			throw new MinuetoZeroNegativeException("Cannot scale an image by a factor of " +factorX + " by " + factorY);
		}
	}
	
	/**
	 * The mask method creates a new <code>MinuetoImage</code> where
	 * all the non-transparent pxels of this image are replaced 
	 * with the provided color.
	 * 
	 * @param color <code>MinuetoColor</code> denoting the color of the mask.
	 * @return <code>MinuetoImage</code> of the mask
	 */
	public MinuetoImage mask(MinuetoColor color) {
		
		BufferedImage resultImage; // To store the result of the mask
		MinuetoImage result = new MinuetoImage(); // Image we will return
		ImageTransformations it = new ImageTransformations(); // Transformation
		// for the mask
		
		// Apply the mask transformation
		resultImage = it.applyFilter(bufferedImage, new MaskImageFilter(color));
		
		// Set up the resulting image
		result.setUpImage(resultImage);
		 
		return result;		
				
	}

	/**
	 * Return a scaled copy of this <code>MinuetoImage</code>. The parameters are described as 
	 * scale factors. Thus, calling the scale function with the 2,2 parameters 
	 * would create a image of double width and height.
	 *
	 * @author Michael A. Hawker
	 * @since 0.45
	 * @param factorX <code>double</code> denoting how the image should be scaled on the X axis.
	 * @param factorY <code>double</code> denoting how the image should be scaled on the Y axis.
	 * @return <code>MinuetoImage</code> of the scaled image.
	 * REMOVED @throws <code>MinuetoZeroNegativeException</code> if a wrong scale factor is used.
	 **/	
	private MinuetoImage scaleInternal(double factorX, double factorY) {

		/* Reference to the destination image. */
		BufferedImage destinationBufferedImage;
		/* Reference to the affine transformation (the scale) */
		AffineTransform affineTransform;
		/* Reference to the GraphicPointer */
		Graphics2D graphics2DDestination;
		/* Resulting MinuetoImage */
		MinuetoImage	destinationImage;

		/* Set up the affine transformation needed to scale the image */
		affineTransform = new AffineTransform();
		/* Scale the image by dFacorX and factorY */
		affineTransform.scale(factorX, factorY);

		/* ADDED: Translate to Account for Negative Scaling Factors */
		if (factorX < 0 && factorY < 0) {
			affineTransform.translate(this.width * factorX, this.height * factorY);
		}
		else if(factorX < 0) {
			affineTransform.translate(this.width * factorX, 0);
		}
		else if(factorY < 0) {
			affineTransform.translate(0, this.height * factorY);
		}

		/* The translation normaly produces an unaccelerated buffered image.
		 * Instead, we produce an accelerated buffered for the result. */   	  
		/* Math.abs to allow for negative scaling factors */
		destinationBufferedImage = ImageTools.createImage((int) Math.abs(factorX*this.width), (int) Math.abs(factorY*this.height));
		/* Reference to the GraphicPointer of the destination image.*/
		graphics2DDestination = destinationBufferedImage.createGraphics();
		/* Draw the scaled image. */
		graphics2DDestination.drawImage(this.bufferedImage,affineTransform ,null);

		/* Set up the new MinuetoImage with the scaled image. */
		destinationImage = new MinuetoImage();
		destinationImage.setUpImage(destinationBufferedImage);

		return destinationImage;
	}

	/**
	 * Returns a flipped copy of this <code>MinuetoImage</code>. The image can be
	 * flipped vertically, horizontally, or both. For example, <code>flip(true, 
	 * false)</code> would flip the image horizontally.
	 *
	 * @author Michael A. Hawker
	 * @since 0.45
	 * @param horizontal <code>boolean</code> if the image should be flipped on the X axis.
	 * @param vertical <code>boolean</code> if the image should be flipped on the Y axis.
	 * @return <code>MinuetoImage</code> of the flipped image.
	 **/	
	public MinuetoImage flip(boolean horizontal, boolean vertical) {
		if (horizontal == false && vertical == false) {
			// Have Nothing To Do But Return Original Image
			return (MinuetoImage) this.clone();
		}
		else {
			/* What we want to flip */
			int iFlipX = 1, iFlipY = 1;

			/* Test our Arguments and Convert to Useable Values */
			if (horizontal == true) {
				iFlipX = -1;
			}

			if (vertical == true) {
				iFlipY = -1;
			}

			// Return a Negatively Scaled Image
			return this.scaleInternal(iFlipX, iFlipY);
		}
	}

	/* (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#setPxel(int, int, org.minueto.MinuetoColor)
	 */
	public void setPixel(int x, int y, MinuetoColor color)  {
		//this.bufferedImage.setRGB(x, y, color.getARGBColorValue());
		drawLine(color, x, y, x, y);
	}

	/**
	 * Get the color of the pxel at location x,y. 
	 * <p>
	 * Note: Pxel manipulation of a <code>MinuetoImage</code> is a very slow operation. It should 
	 * not be used in realtime. 
	 *
	 * @since 1.1
	 * @param x <code>int</code> denoting which pxel on the X axis we are targeting.
	 * @param y <code>int</code> denoting which pxel on the y axis we are targeting.
	 * @return <code>MinuetoColor</code> denoting the color of the specified pxel.
	 **/
	public MinuetoColor getPixel(int x, int y) {
		return new MinuetoColor(this.bufferedImage.getRGB(x, y));
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#clear()
	 */
	public void clear() {
		
		this.graphics2D.setBackground(new Color(0, 0, 0, 0));
		
		/* Clear the whole image. */
		this.graphics2D.clearRect(0, 0, this.width, this.height);
	}

	/*
	 * (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#clear(org.minueto.MinuetoColor)
	 */
	public void clear(MinuetoColor color) {

		if (color == null)
			new NullPointerException();

		this.graphics2D.setColor(color.getAWTColor());

		/* Clear the whole image. */
		this.graphics2D.fillRect(0, 0, this.width, this.height);
	}

	/* (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#draw(org.minueto.image.MinuetoImage, int, int)
	 */
	public void draw(MinuetoImage image, int x, int y) {

		if (image == null) new NullPointerException();
		
		this.graphics2D.drawImage(image.getImage(),x,y,null);
		//this.graphics2D.dispose();
	}

	/* (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#drawLine(org.minueto.MinuetoColor, int, int, int, int)
	 */
	public void drawLine(MinuetoColor color, int xStart, int yStart, int xStop, int yStop) {

		if (color == null) new NullPointerException();

		int width = this.width;
		int height = this.height;

		/* Bound check */
		if (( xStart < 0 ) || ( xStart >= width))
			throw new MinuetoOutOfBoundException(xStart, 0, width);
		//if (( xStop < 0 ) || ( xStop >= width))
		//	throw new MinuetoOutOfBoundException(xStop, 0, width);
		if (( yStart < 0 ) || ( yStart >= height))
			throw new MinuetoOutOfBoundException(yStart, 0, height);
		//if (( yStop < 0 ) || ( yStop >= height))
		//	throw new MinuetoOutOfBoundException(yStop, 0, height);

		this.graphics2D.setColor(color.getAWTColor());
		this.graphics2D.drawLine(xStart,yStart,xStop,yStop);
	}

	/* (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#getWidth()
	 */
	public int getWidth() {

		return this.width;
	}

	/* (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#getHeight()
	 */
	public int getHeight() {

		return this.height;
	}

	/**
	 * Return the buffered image (content) of this MinuetoImage. This should
	 * only be called by methods in the package.
	 *
	 * @return <code>BufferedImage</code> of the <code>MinuetoImage</code>.
	 **/
	protected BufferedImage getImage() {

		if (this.accelerated == false) this.accelerateBuffer();

		return this.bufferedImage;
	}
	
	protected boolean accelerateBuffer() {
			
		Graphics2D graphics2D;
				
		BufferedImage bufferedImage;
		
		if (this.accelerated == true) return true;
		
		bufferedImage = ImageTools.createAcceleratedImage(
			this.bufferedImage.getWidth(),
			this.bufferedImage.getHeight());
			
		if (bufferedImage == null) return false;
						
		graphics2D = bufferedImage.createGraphics();
				
		/* Copy image */
		graphics2D.drawImage(this.bufferedImage ,0,0,null);
		graphics2D.dispose();
		
		this.bufferedImage = bufferedImage;
		this.graphics2D = this.bufferedImage.createGraphics();		
		
		this.accelerated = true;
		return true;
	}
	
	/**
	 * Draw a rectangle of the specified color from the start point and of the desired size.
	 *
	 * @param color <code>MinuetoColor</code> denoting the color of the rectangle.
	 * @param x <code>int</code> denoting the x value of the start point.
	 * @param y <code>int</code> denoting the y value of the start point.
	 * @param width <code>int</code> denoting the width of the rectangle.
	 * @param height <code>int</code> denoting the height of the rectangle.
	 * @throws <code>MinuetoOutOfBoundException</code> if the target coordinates are not
	 * valid.
	 **/
	public void drawRectangle(MinuetoColor color, int x, int y, int width, int height) {
		
		/* Bound check */
		if (( x < 0 ) || ( x >= width))
			throw new MinuetoOutOfBoundException(x, 0, width);
		if (( y < 0 ) || ( y >= height))
			throw new MinuetoOutOfBoundException(y, 0, height);
	
		Color previousColor = this.graphics2D.getColor();
		
		this.graphics2D.setColor(color.getAWTColor());
		this.graphics2D.fillRect(x, y, width, height);
		
		this.graphics2D.setColor(previousColor);
	}
	
	/**
	 * Draw a polygon of the specified color from the specified points. Points are
	 * specified in a single integer array in the following format : {x1,y1,x2,y2,...}
	 *
	 * @param color <code>MinuetoColor</code> denoting the color of the rectangle.
	 * @param points <code>int[]</code> denoting the points of the polygon.
	 **/
	public void drawPolygon(MinuetoColor color, int[] points) {
		
		Color previousColor = this.graphics2D.getColor();
		
		this.graphics2D.setColor(color.getAWTColor());
		
		Polygon p = new Polygon();
		
		for(int i = 0; i < points.length; i = i + 2) {
			p.addPoint(points[i], points[i+1]);
		}
		
		this.graphics2D.fill(p);
		
		this.graphics2D.setColor(previousColor);
		
	}
	
	/**
	 * Draw a circle of the specified color at the start point and of the desired diameter.
	 *
	 * @param color <code>MinuetoColor</code> denoting the color of the circle.
	 * @param x <code>int</code> denoting the x value of the start point.
	 * @param y <code>int</code> denoting the y value of the start point.
	 * @param diameter <code>int</code> denoting the diameter of the circle.
	 * @throws <code>MinuetoOutOfBoundException</code> if the target coordinates are not
	 * valid.
	 **/
	public void drawCircle(MinuetoColor color, int x, int y, int diameter) {
		
		Color previousColor = this.graphics2D.getColor();
		
		this.graphics2D.setColor(color.getAWTColor());
		this.graphics2D.fillOval(x, y, diameter, diameter);
		
		this.graphics2D.setColor(previousColor);
	}

	/* (non-Javadoc)
	 * @see org.minueto.image.MinuetoDrawingSurface#save(java.lang.String)
	 */
	public void save(String filename) throws MinuetoFileException {

		if (filename == null) new NullPointerException();

		try {
			File file = new File(filename);
			ImageIO.write(this.bufferedImage, "png", file);
		} catch ( IOException ioe) {
			throw new MinuetoFileException("Cannot write to \"" + filename + "\" .");
		}
	}
	
	/**
	 * Creates and returns a copy of this MinuetoImage. The returned MinuetoImage
	 * is independant from this MinuetoImage. 
	 **/
	public Object clone() {
	
		MinuetoImage imageClone = new MinuetoImage(this.width, this.height);
		imageClone.draw(this, 0, 0);
		
		return imageClone;
	}

}


