/*
 * @(#)MinuetoRectangle.java
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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.minueto.MinuetoColor;
import org.minueto.MinuetoZeroNegativeException;

/**
 * The <code>MinuetoRectangle</code> class allows you to build the image of a rectangle or a square.
 * Since <code>MinuetoRectangle</code> extends <code>MinuetoImage</code>, all the important image
 * manipulation fonctionnality (such as draw, drop, scale, etc) are available.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 		MinuetoImage
 **/
public class MinuetoRectangle extends MinuetoImage {
	
	/**
	 * Creates a <code>MinuetoRectangle</code> image of a rectangle (or a square) of the specified 
	 * color and size.
	 *
	 * @param sizeX <code>int</code> denoting the width of the rectangle.
	 * @param sizeY <code>int</code> denoting the height of the rectangle.
	 * @param color <code>MinuetoColor</code> denoting the color of the rectangle.
	 * @param fill <code>boolean</code> denothing if the rectangle should be full (true) or
	 *        transparent.
	 * @throws <code>MinuetoZeroNegativeException</code> if the supplied rectangle size is invalid.
	 **/
   public  MinuetoRectangle(int sizeX, int sizeY, MinuetoColor color, boolean fill)
   {
   	
		/* When subclassing to a class with multiple constructor,it's
		* important to specify which constructor to use. */
		super();
		
		if (sizeX < 1) throw new MinuetoZeroNegativeException("Cannot create rectangle of width " + sizeX);
		if (sizeY < 1) throw new MinuetoZeroNegativeException("Cannot create rectangle of height " + sizeY);
		if (color == null) throw new NullPointerException();
		
		BufferedImage bufferedImage;
		Graphics2D graphics2D;
   	   	
		/* Create the surface to draw the rectangle on. */
		bufferedImage = ImageTools.createImage( sizeX, sizeY );
		graphics2D = bufferedImage.createGraphics();
   	   	
		/* Set the color of the rectangle. */
		graphics2D.setColor(color.getAWTColor());
   	   	
		/* Draw the rectangle onto our surface. */
		if (fill == true) {
			graphics2D.fillRect(0,0,sizeX,sizeY);
			
		} else {
			graphics2D.drawRect(0,0,sizeX-1,sizeY-1);
			
		}
		
		super.setUpImage(bufferedImage);   	   	
   }

}
