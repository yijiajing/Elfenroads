/*
 * @(#)MinuetoColor.java
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

package org.minueto;

import java.awt.Color;
import java.io.Serializable;

/**
 * The <code>MinuetoColor</code> class is used to store information describing a 
 * color. Several prefined colors defined as constants can also be found in this
 * class.
 * <p>
 * Note: Minueto defines a color as a RGB value (red/green/blue). If you are new
 * to computer graphics, please note that this color scheme is significantly different
 * from the one used for paper (cyan, blue, yellow, black, also known as CMYK). 
 *
 * @author	Alexandre Denault
 * @version 1.1
 * @since 	Minueto 0.4
 **/
 public class MinuetoColor implements Serializable {	
	
	private static final long serialVersionUID = 6077031996981772626L;

	private final static String ID = MinuetoColor.class.getName();

	private int red;
	private int blue;
	private int green;
	private int alpha;
	
	/** The color Red. */
	public static final MinuetoColor RED 	= new MinuetoColor( 255, 0, 0);
	/** The color Green. */
	public static final MinuetoColor GREEN 	= new MinuetoColor( 0, 255, 0);
	/** The color Blue. */
	public static final MinuetoColor BLUE 	= new MinuetoColor( 0, 0, 255);
	/** The color Yellow.*/
	public static final MinuetoColor YELLOW = new MinuetoColor( 255, 255, 0);
	/** The color Black.*/
	public static final MinuetoColor BLACK 	= new MinuetoColor( 0, 0, 0);
	/** The color White */
	public static final MinuetoColor WHITE 	= new MinuetoColor( 255, 255, 255);
	
	/**
	 * Creates a new RGB color. Colors are defined as three integer
	 * values ranging from 0 to 255. 
	 *
	 * @param red <code>int</code> denoting the red color value.
	 * @param green <code>int</code> denoting the green color value.
	 * @param blue <code>int</code> denoting the blue color value.
	 *
	 * @throws <code>MinuetoInvalidColorValueException</code> if an invalid color value is passed.
	 **/
	public MinuetoColor(int red, int green, int blue) {

		if ( (red < 0) || (red > 255) ) 
			throw new MinuetoInvalidColorValueException("The red color value cannot be " +red);
		if ( (green < 0) || (green > 255) ) 
			throw new MinuetoInvalidColorValueException("The green color value cannot be " +green);
		if ( (blue < 0) || (blue > 255) ) 
			throw new MinuetoInvalidColorValueException("The blue color value cannot be " +blue);

		this.red = red;
		this.green = green;
		this.blue = blue;	
		this.alpha = 255;
   	}
	
	/**
	 * Creates a new RGBA color from a Java AWT Color object
	 * 
	 * @param color <code>java.awt.Color</code> ths color value
	 * @see java.awt.Color
	 */
	public MinuetoColor(Color color) {
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();
		this.alpha = color.getAlpha();
	}
	
	/**
	 * Creates a MinuetoColor object from a single int value just as java.awt.Color:
	 * Creates an opaque sRGB color with the specified combined RGB value consisting of the red component in bits 16-23, the green component in bits 8-15, and the blue component in bits 0-7.
	 * 
	 * @param color <code>int</code> 
	 */
	public MinuetoColor(int color) {
		this(new Color(color));
	}
	
	/**
	 * Creates a new RGBA color. Colors are defined as four integers
	 * values ranging from 0 to 255. 
	 *
	 * @param red <code>int</code> denoting the red color value.
	 * @param green <code>int</code> denoting the green color value.
	 * @param blue <code>int</code> denoting the blue color value.
	 * @param alpha <code>int</code> denothing the alpha color value.
	 *
	 * @throws <code>MinuetoInvalidColorValueException</code> if an invalid color value is passed.
	 **/
	public MinuetoColor(int red, int green, int blue, int alpha) {

		if ( (red < 0) || (red > 255) ) 
			throw new MinuetoInvalidColorValueException("The red color value cannot be " +red);
		if ( (green < 0) || (green > 255) ) 
			throw new MinuetoInvalidColorValueException("The green color value cannot be " +green);
		if ( (blue < 0) || (blue > 255) ) 
			throw new MinuetoInvalidColorValueException("The blue color value cannot be " +blue);
		if ( (alpha < 0) || (alpha > 255) ) 
			throw new MinuetoInvalidColorValueException("The alpha color value cannot be " + alpha);

		this.red = red;
		this.green = green;
		this.blue = blue;				
		this.alpha = alpha;
   	}
   	
	
	/**
	 * Creates a new RGB color. Colors are defined as three floating-point
	 * values ranging from 0.0 to 1.0. 
	 *
	 * @param red <code>double</code> denoting the red color value.
	 * @param green <code>double</code> denoting the green color value.
	 * @param blue Double <code>double</code> the blue color value.
	 *
	 * @throws <code>MinuetoInvalidColorValueException</code> if an invalid color value is passed.
	 **/
   	public  MinuetoColor(double red, double green, double blue) {
	
		if ( (red < 0.0) || (red > 1.0) ) 
			throw new MinuetoInvalidColorValueException("The red color value cannot be " +red);
		if ( (green < 0.0) || (green > 1.0) ) 
			throw new MinuetoInvalidColorValueException("The green color value cannot be " +green);
		if ( (blue < 0.0) || (blue > 1.0) ) 
			throw new MinuetoInvalidColorValueException("The blue color value cannot be " +blue);
			
		this.red = Math.round((int)(255.0*red));
		this.green = Math.round((int)(255.0*green));
		this.blue = Math.round((int)(255.0*blue));
		this.alpha = 255;
   	}
   	
	/**
	 * Creates a new RGBA color. Colors are defined as four floating-point
	 * values ranging from 0.0 to 1.0. 
	 *
	 * @param red <code>double</code> denoting the red color value.
	 * @param green <code>double</code> denoting the green color value.
	 * @param blue <code>double</code> denoting the blue color value.
	 * @param alpha <code>double</code> denoting the alpha color value. 
	 *
	 * @throws <code>MinuetoInvalidColorValueException</code> if an invalid color value is passed.
	 **/
   	public  MinuetoColor(double red, double green, double blue, double alpha) {
	
		if ( (red < 0.0) || (red > 1.0) ) 
			throw new MinuetoInvalidColorValueException("The red color value cannot be " +red);
		if ( (green < 0.0) || (green > 1.0) ) 
			throw new MinuetoInvalidColorValueException("The green color value cannot be " +green);
		if ( (blue < 0.0) || (blue > 1.0) ) 
			throw new MinuetoInvalidColorValueException("The blue color value cannot be " +blue);
		if ( (alpha < 0.0) || (alpha > 1.0) ) 
			throw new MinuetoInvalidColorValueException("The alpha color value cannot be " +alpha);
			
		this.red = Math.round((int)(255.0*red));
		this.green = Math.round((int)(255.0*green));
		this.blue = Math.round((int)(255.0*blue));
		this.alpha = Math.round((int)(255.0*alpha));
   	}

   	/**
   	 * Returns a darker color. The darkness factor is determined by the double value, whose value should 
   	 * be between 0.0 and 1.0.
   	 *  
   	 * @param factor <code>double</code> that determines how darker the color will be.
   	 * @return <code>MinuetoColor</code> darker color.
   	 */
   	public MinuetoColor darken(double factor) {
		
		return this.darken((int)(255.0*factor));
	}
   	
   	/**
   	 * Returns a darker color. The darkness factor is determined by the integer value, whose value should 
   	 * be between 0 and 255.
   	 *  
   	 * @param factor <code>double</code> that determines how darker the color will be.
   	 * @return <code>MinuetoColor</code> darker color.
   	 */
   	public MinuetoColor darken(int factor) {
		
   		if (factor > 255) factor = 255;
   		if (factor < 0) factor = 0;
   		
   		int newRed = this.red - factor;		
		int newGreen = this.green - factor;
		int newBlue = this.blue - factor;
		
		return new MinuetoColor(
				(newRed > 0) ? newRed : 0,
				(newGreen > 0) ? newGreen : 0,
				(newBlue > 0) ? newBlue : 0,
				alpha);						
	}
	
   	/**
   	 * Returns a lighter color. The lightness factor is determined by the double value, whose value should 
   	 * be between 0.0 and 1.0.
   	 *  
   	 * @param factor <code>double</code> that determines how lighter the color will be.
   	 * @return <code>MinuetoColor</code> lighter color.
   	 */
   	public MinuetoColor lighten(double factor) {
		
		return this.lighten((int)(255.0*factor));
	}
	
   	/**
   	 * Returns a lighter color. The lightness factor is determined by the double value, whose value should 
   	 * be between 0.0 and 1.0.
   	 *  
   	 * @param factor <code>double</code> that determines how lighter the color will be.
   	 * @return <code>MinuetoColor</code> lighter color.
   	 */
   	public MinuetoColor lighten(int factor) {
		
   		if (factor > 255) factor = 255;
   		if (factor < 0) factor = 0;
   		
   		int newRed = this.red + factor;		
		int newGreen = this.green + factor;
		int newBlue = this.blue + factor;
		
		return new MinuetoColor(
				(newRed < 255) ? newRed : 255,
				(newGreen < 255) ? newGreen : 255,
				(newBlue < 255) ? newBlue : 255,
				alpha);
	}
   	
   	/**
   	 * Returns the blue value of this color. The returned value is an integer 
   	 * whose value is between 0 and 255.
   	 * 
   	 * @return <code>int</code> denoting the blue value of this color.
   	 */
   	public int getBlue() {
   		
   		return this.blue;
   	}
   	
   	/**
   	 * Returns the red value of this color. The returned value is an integer 
   	 * whose value is between 0 and 255.
   	 * 
   	 * @return <code>int</code> denoting the red value of this color.
   	 */
   	public int getRed() {
   		
   		return this.red;
   	}
   	
   	/**
   	 * Returns the green value of this color. The returned value is an integer 
   	 * whose value is between 0 and 255.
   	 * 
   	 * @return <code>int</code> denoting the green value of this color.
   	 */
   	public int getGreen() {
   		
   		return this.green;
   	}
   	
   	/**
   	 * Returns the alpha value of this color. The returned value is an integer 
   	 * whose value is between 0 and 255.
   	 * 
   	 * @return <code>int</code> denoting the alpha value of this color.
   	 */
   	public int getAlpha() {
   		
   		return this.alpha;
   	}
   	
   	/**
   	 * Returns the equivalent AWT color object.
   	 *  
   	 * @return <code>Color</code> Color code of the object.
   	 */
   	public Color getAWTColor() {
   		
   		return new Color(this.red, this.green, this.blue, this.alpha);
   	}
   	
   	/**
   	 * Returns the sRGB color code for this color as an integer.
   	 * 
   	 * @return <code>int</code> of the sRGB color code.
   	 */
   	public int getARGBColorValue() {
   		
   		return new Color(this.red, this.green, this.blue, this.alpha).getRGB();
   	}
   	
   	/**
   	 * Returns a string representation of the color.
   	 * 
   	 * @return <code>String</code> containing the color information.
   	 */
   	public String toString() {
   		return ID + " <Red: " + this.red + " Green: " + this.green + " Blue: " + this.blue + " Alpha: " + this.alpha + " >";  
   	}
}
