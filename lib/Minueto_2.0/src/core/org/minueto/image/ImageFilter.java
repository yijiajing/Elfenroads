package org.minueto.image;

interface ImageFilter {

	public abstract int apply(int x, int y, int argb);

}