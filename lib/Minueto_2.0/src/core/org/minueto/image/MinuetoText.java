/*
 * @(#)MinuetoText.java
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

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import org.minueto.MinuetoColor;

/**
 * The <code>MinuetoText</code> class allows you to build an image of a string of text. 
 * You can draw that image on the screen or over another image.
 * <p>
 * Since <code>MinuetoText</code> extends <code>MinuetoImage</code>, all the important image
 * manipulation fonctionnality (such as draw, drop, scale, etc) are available.
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.4
 * @see 		MinuetoImage
 **/
public class MinuetoText extends MinuetoImage {

	/**
	 * Builds a <code>MinuetoText</code> image of a string of the specified 
	 * color and font.
	 *
	 * @param text <code>String</code> denoting the text to be used to create the image.
	 * @param color <code>MinuetoColor</code> denoting the color of the string.
	 * @param font <code>MinuetoFont</code> describing the font used to draw the string.
	 **/	
   public  MinuetoText(String text, MinuetoFont font, MinuetoColor color) {
	
		/* When subclassing to a class with multiple constructor,it's
		* important to specify which constructor to use. */
		super();
		
		this.setUpText(text, font, color, false);
   }
	
	/**
	 * Builds a <code>MinuetoText</code> image of a string of the specified 
	 * color and font. The string can also anti-aliased. Anti-aliasing tries to
	 * improve the visibility of a font by introducing variation in the colors
	 * used to draw the text. This gives great result for large fonts, but should
	 * be avoided with small fonts.
	 *
	 * @since 0.45
	 * @param text <code>String</code> denoting the text to be used to create the image.
	 * @param color <code>MinuetoColor</code> denoting the color of the string.
	 * @param font <code>MinuetoFont</code> describing the font used to draw the string.
	 ** @param antiAliased <code>boolean</code> indicating if the text should be anti-aliased.
	 **/
	public MinuetoText(String text, MinuetoFont font, MinuetoColor color, boolean antiAliased) {
	
		/* When subclassing to a class with multiple constructor,it's
		* important to specify which constructor to use. */
		super();
		
		this.setUpText(text, font, color, antiAliased);
	}
	
	/**
	 * Builds the actual MinuetoText objects. The constructors are wrappers for this function.
	 */
	public void setUpText(String text, MinuetoFont font, MinuetoColor color, boolean antiAliased) {
	
		/* The hardest part of build the image with the text string is 
		 * figuring out the size of the string itself. **/
		
		if (text == null) new NullPointerException();
		if (font == null) new NullPointerException();
		if (color == null) new NullPointerException();
		
		BufferedImage bufferedImage;
		Graphics2D graphics2D;
		FontMetrics fontMetrics;
						
		int width;		/* Width of our image */
		int height;	/* Height of our image */
   		
		/* To calculate the size of the string, we need to get information
		 * about the screen (since the string size depends on the DPI 
		 * resolution of the monitor). To get this information, we create
		 * a temporary 1x1 accelerated image and get the font metric
		 * information from the graphic 2D image. */
		bufferedImage = ImageTools.createImage( 1, 1 );
		graphics2D = bufferedImage.createGraphics();
	
		/* Before getting the font metric, we need to set the font on the
		 * grpahic 2D object. */
   		graphics2D.setFont(font.getFont());
			
			/* Enable or disable antialiasing. */
   		if (antiAliased == true) {
   			graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
   		} else {
				graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			}
   		
		/* Ah! now we have the font metrics! */
   		fontMetrics = graphics2D.getFontMetrics();   		
   		   		
		/* It might seem simple, but figuring out that these 2 functions exist
		 * was insanely hard. The Java font engine can be quite complicated. */
   		height = fontMetrics.getHeight();
   		width = fontMetrics.stringWidth(text);
   		
   		/* Since we should never create an image of width 0. */
   		if (width == 0) { width = 1; }
   		
		/* So now we know the proper size of the image. */
		bufferedImage = ImageTools.createImage( width, height );
   		graphics2D = bufferedImage.createGraphics();
	
		/* Set the font and the color. */
   		graphics2D.setFont(font.getFont());
   		graphics2D.setColor(color.getAWTColor());
   		
   		/* Enable or disable antialiasing. */
   		if (antiAliased == true) {
   			graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
   		} else {
				graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
			}
   		
   	   	
		/* Draw the string on the buffer. */
		graphics2D.drawString(text, 0, fontMetrics.getAscent());
   	   	
		/* Set up the MinuetoImage */
   	super.setUpImage(bufferedImage);
	}
}
