package org.minueto.image;

import java.awt.GraphicsConfiguration;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import org.minueto.MinuetoOptions;

public class ImageTools {

	private static GraphicsConfiguration graphicConfiguration;
	
	/**
	 * Return a normal BufferedImage. 
	 *
	 * @param x int denoting the width of the BufferedImage.
	 * @param y int denothing the height of the BufferedImage.  .
	 **/
	protected static BufferedImage createImage(int x, int y) {

		return new BufferedImage(x, y, BufferedImage.TYPE_INT_ARGB);
	}
	
	/**
	 * Return a hardware accelerated BufferedImage. 
	 *
	 * @param x int denoting the width of the BufferedImage.
	 * @param y int denothing the height of the BufferedImage.  .
	 **/
	protected static BufferedImage createAcceleratedImage(int x, int y)
	{
		/* To produce a hardware accelerated BufferedImage, you need the current
		 * GraphicConfiguration. Since only MinuetoWindow stores this information,
		 * we need use this static function to create them. */

		/* If MinuetoWindow was not initialized yet, we should not be creating
		   an accelerated BufferedImage. */
		if (ImageTools.graphicConfiguration == null) {
			return null;
		}

		/* Return the new buffered image. */
		if (MinuetoOptions.isAlphaEnabled()) {		
			return ImageTools.graphicConfiguration.
				createCompatibleImage(x,y, Transparency.TRANSLUCENT);
		} else {
			return ImageTools.graphicConfiguration.
			createCompatibleImage(x,y, Transparency.BITMASK);
		}
	}
	
	/**
	 * Extracts the buffered image from a MinuetoImage. This function
	 * breaks the encapsulation of a MinuetoImage, but is needed by
	 * some internal Minueto functions for optimization. Use with 
	 * caution.
	 * 
	 * @param image <code>MinuetoImage</code> with the buffer we want to extract.
	 * @return <code>BufferedImage</code> that we want to retrieve.
	 */
	public static BufferedImage getBufferedImage(MinuetoImage image) {
		
		return image.getImage();
	}
	

	/**
	 * To create hardware accelerated image, the ImageTools class needs
	 * to know the graphic configuration of the computer. This method
	 * is public only for convience reasons. Do no use.
	 * 
	 * @param graphicConfiguration <code>GraphicConfiguration</code> of
	 *   the screen.
	 */
	public static void setGraphicConfiguration(
			GraphicsConfiguration graphicConfiguration) {
		ImageTools.graphicConfiguration = graphicConfiguration;
	}
}
