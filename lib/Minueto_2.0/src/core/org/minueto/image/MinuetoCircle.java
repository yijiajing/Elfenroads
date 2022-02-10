/*
 * @(#)MinuetoCircle.java
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
 * The <code>MinuetoCircle</code> class allows you to build the image of a circle or an ellipse.
 * Since <code>MinuetoCircle</code> extends <code>MinuetoImage</code>, all the 
 * important image manipulation fonctionnality (such as draw, drop, scale, etc) 
 * are available.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 		MinuetoImage
 **/
public class MinuetoCircle extends MinuetoImage {

	/**
	 * Creates a <code>MinuetoCircle</code> image of a circle (or an ellipse) of the specified 
	 * color and size.
	 *
	 * @param sizeX <code>int</code> denoting the width of the circle.
	 * @param sizeY <code>int</code> denoting the height of the circle.
	 * @param color <code>MinuetoColor</code> denoting the color of the circle.
	 * @param fill <code>boolean</code> denoting if the circle should be full (true) or
	 *        transparent (false).
	 * @throws <code>MinuetoZeroNegativeException</code> if the supplied circle size is invalid.
	 **/
   public MinuetoCircle(int sizeX, int sizeY, MinuetoColor color, boolean fill) {
 
		/* When subclassing to a class with multiple constructor,it's
		* important to specify which constructor to use. */
   	super();
   	   	
   	if (sizeX < 1) throw new MinuetoZeroNegativeException("Cannot create circle of width " + sizeX);
		if (sizeY < 1) throw new MinuetoZeroNegativeException("Cannot create circle of height " + sizeY);
		if (color == null) new NullPointerException();
		
   	   	
   	this.buildCircle(sizeX, sizeY, color, fill);
   }
   
   /**
	 * Creates a <code>MinuetoCircle</code> image of a circle (or an ellipse) of the specified 
	 * color and radius.
	 *
	 * @param radius <code>int</code> denoting the radius of the circle.	 
	 * @param color <code>MinuetoColor</code> denoting the color of the circle.
	 * @param fill <code>boolean</code> denothing if the circle should be full (true) or
	 *        transparent (false).
	 * @throws <code>MinuetoZeroNegativeException</code> if the supplied radius is invalid.
	 **/
   public MinuetoCircle(int radius, MinuetoColor color, boolean fill) {
 
		/* When subclassing to a class with multiple constructor,it's
		* important to specify which constructor to use. */
   	   	super();
   	   	
				if (radius < 1) throw new MinuetoZeroNegativeException("Cannot create circle of radius " + radius);
				if (color == null) throw new NullPointerException();
   	   	
   	   	this.buildCircle(radius*2, radius*2, color, fill);
   }
   
   	
   /**
	  * Since the two constructors are similar, the functionnality of building
	  * the circle was move to this method.
	  **/
   protected void buildCircle(int sizeX, int sizeY, MinuetoColor color, boolean fill) {
    	   	
   	   	BufferedImage bufferedImage;
   	   	Graphics2D graphics2D;
				
 				/* Create the surface to draw the rectangle on. */
   	   	bufferedImage = ImageTools.createImage( sizeX, sizeY );
   	   	graphics2D = bufferedImage.createGraphics();
   	   	
				/* Set the color of the rectangle. */
   	   	graphics2D.setColor(color.getAWTColor());
   	   	
				/* Draw the rectangle onto our surface. */
   	   	if (fill == true) {
   	   		graphics2D.fillOval(0,0,sizeX-1,sizeY-1);
   	   		
   	   	} else {
   	   		graphics2D.drawOval(0,0,sizeX-1,sizeY-1);
   	   		
   	   	}		
   	   	
   	   	super.setUpImage(bufferedImage); 
   }

}
