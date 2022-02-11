/*
 * @(#)MinuetoFont.java
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

import java.awt.Font;

import org.minueto.MinuetoZeroNegativeException;

/**
 * The <code>MinuetoFont</code> class is used to store information describing a 
 * Font. All "writing" fonctionnality in Minueto requires a <code>MinuetoFont</code>
 * at some point.
 * <p>
 * Loading a font in Minueto is a best effort task. If the specified font is not 
 * available, Minueto will load a default font instead.
 * <p> 
 * The following fonts are available in the JVM, thus making them platform
 * independant.
 * <ul>
 * <li> Dialog
 * <li> DialogInput
 * <li> Monospaced
 * <li> Serif
 * <li> SansSerif
 * </ul>
 *
 * @author	Alexandre Denault
 * @version 1.0
 * @since 	Minueto 0.3
 **/
public class MinuetoFont
{
	private Font fontInformation;
	private int fontSize;

	/** One of the standard Java font, similar to Arial. */
	public static final String Dialog = "Dialog";
	/** One of the standard Java font, similar to Courrier New. */
	public static final String DialogInput = "DialogInput";
	/** One of the standard Java font, similar to Courrier New.*/
	public static final String Monospaced = "Monospaced";
	/** One of the standard Java font, similar to Times New Roman. */
	public static final String Serif = "Serif";
	/** One of the standard Java font, similar to Arial. */
	public static final String SansSerif = "SansSerif";

	/**
	 * Creates a font object by loading the specified font with the specified 
	 * caracteristics. Loading a font in Minueto is a best effort task. If the
	 * specified font is not available, Minueto will load a default font instead.
	 *
	 * @param fontName <code>String</code> denoting the name of the font to be loeaded.
	 * @param fontSize <code>int</code> indicating the size of the font.
	 * @param bold <code>boolean</code> indicating if the font should be bold.
	 * @param italic <code>boolean</code> indicating if the font should be italic.
	 * @throws <code>MinuetoZeroNegativeException</code> if the font is invalid. 
	 **/
   public  MinuetoFont(String fontName, int fontSize, boolean bold, boolean italic) {
   	
   	int style;
   	
   	
		if ( fontSize < 1 ) new MinuetoZeroNegativeException("Cannot create a font of size " + fontSize);
		if ( fontName == null ) new NullPointerException();
		
   	style = 0;
   	this.fontSize = fontSize; 
   		
   	if(bold == true) {
   		if (italic == true) {
   			style = Font.ITALIC | Font.BOLD;
   		} else {
   			style = Font.BOLD;
   		}
   	} else {
   		if (italic == true) {
   			style = Font.ITALIC;
   		} else {
   			style = Font.PLAIN;
   		}
   	}
   			
   	this.fontInformation = new Font(fontName, style, fontSize);   		
   		
   	return;
   }
   
   protected int getFontSize() {
   	
   		return this.fontSize;
   }
   
   protected Font getFont() {
   		
   	return this.fontInformation;
   }
   
}
