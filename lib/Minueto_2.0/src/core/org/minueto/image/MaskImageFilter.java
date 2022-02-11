package org.minueto.image;

import org.minueto.MinuetoColor;

class MaskImageFilter implements ImageFilter  {
	
	int mask;
		
	public MaskImageFilter(MinuetoColor mask) {
		super();

		//System.out.println(mask);
		this.mask = mask.getARGBColorValue();
	}

	/* (non-Javadoc)
	 * @see Minueto.ImageFilter#process(int, int, int)
	 */
	public int apply(int x, int y, int argb) {
	
		/*System.out.println("debug " + (argb & 0xff000000));
		System.out.println("MASK " + mask);*/
		
		if ((argb & 0xff000000) != 0) {
			return mask;			
		}
		
		return argb;
	}
}
