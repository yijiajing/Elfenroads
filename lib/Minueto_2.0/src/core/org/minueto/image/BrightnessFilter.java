package org.minueto.image;

import java.awt.Color;

class BrightnessFilter implements ImageFilter {

	int value;
	
	public BrightnessFilter(int value) {
		super();
		// TODO Auto-generated constructor stub
		this.value = value;
	}

	/* (non-Javadoc)
	 * @see Minueto.ImageFilter#process(int, int, int)
	 */
	public int apply(int x, int y, int argb) {
	
		Color oldColor = new Color(argb);
		Color newColor;
		
   		int newRed = oldColor.getRed() + value;		
		int newGreen = oldColor.getGreen() + value;
		int newBlue = oldColor.getBlue() + value;
		
		newColor = new Color(newRed, newBlue, newGreen, oldColor.getAlpha());
				
		return newColor.getRGB();
	}
}
