package org.minueto.image;

import java.awt.image.BufferedImage;

class ImageTransformations {
	
	public final static int ALPHA = 0;
	public final static int RED = 1;
	public final static int GREEN = 2;
	public final static int BLUE = 3;	
			
	protected BufferedImage applyFilter(BufferedImage image, ImageFilter filter) {
		
		BufferedImage result = ImageTools.createImage(image.getWidth(),image.getHeight());
		int[] sourceDataARGB = new int[image.getWidth()*image.getHeight()];	
		int[] resultDataARGB = new int[image.getWidth()*image.getHeight()];
		
		sourceDataARGB = image.getRGB(0,0,image.getWidth(),image.getHeight(), sourceDataARGB, 0, image.getWidth() );
		
		for (int x = 0; x < image.getWidth(); x++) {
			for(int y = 0; y < image.getHeight(); y++) {
				
				int position = x + y*image.getWidth();
				
				resultDataARGB[position] = filter.apply(x,y,sourceDataARGB[position]);								
			}
		}
		
		result.setRGB(0,0,image.getWidth(),image.getHeight(), resultDataARGB, 0, image.getWidth() );
		
		return result;
	}
	
}

