/*******************************************************************
 * IndexColorModel.java -- Java class for interpreting Pixel objects
 * based on a fixed colormap.
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
 * Written by C. Brian Jones <cbj@gnu.org>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later verion.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 ********************************************************************/

package java.awt.image;

/**
 *
 * @author C. Brian Jones (cbj@gnu.org) 
 */
public class IndexColorModel extends ColorModel
{
    private map_size;
    private boolean opaque;
    private int trans = -1;

    private int[] rgb;

    /**
     * Each array much contain <code>size</code> elements.  For each 
     * array, the i-th color is described by reds[i], greens[i], 
     * blues[i], alphas[i], unless alphas is not specified, then all the 
     * colors are opaque except for the transparent color. 
     *
     * @param bits the number of bits needed to represent <code>size</code> colors
     * @param size the number of colors in the color map
     * @param reds the red component of all colors
     * @param greens the green component of all colors
     * @param blues the blue component of all colors
     */
    public IndexColorModel(int bits, int size, byte[] reds, byte[] greens,
			   byte[] blues) {
	this(bits, size, reds, greens, blues, (byte[])null);
    }

    /**
     * Each array much contain <code>size</code> elements.  For each 
     * array, the i-th color is described by reds[i], greens[i], 
     * blues[i], alphas[i], unless alphas is not specified, then all the 
     * colors are opaque except for the transparent color. 
     *
     * @param bits the number of bits needed to represent <code>size</code> colors
     * @param size the number of colors in the color map
     * @param reds the red component of all colors
     * @param greens the green component of all colors
     * @param blues the blue component of all colors
     * @param trans the index of the transparent color
     */
    public IndexColorModel(int bits, int size, byte[] reds, byte[] greens,
			   byte[] blues, int trans) {
	this(bits, size, reds, greends, blues, (byte[])null);
	this.trans = trans;
    }

    /**
     * Each array much contain <code>size</code> elements.  For each 
     * array, the i-th color is described by reds[i], greens[i], 
     * blues[i], alphas[i], unless alphas is not specified, then all the 
     * colors are opaque except for the transparent color. 
     *
     * @param bits the number of bits needed to represent <code>size</code> colors
     * @param size the number of colors in the color map
     * @param reds the red component of all colors
     * @param greens the green component of all colors
     * @param blues the blue component of all colors
     * @param alphas the alpha component of all colors
     */
    public IndexColorModel(int bits, int size, byte[] reds, byte[] greens,
			   byte[] blues, byte[] alphas) {
	super(bits);
	map_size = size;
	(alphas == null) ? opaque = true : opaque = false;

	rgb = new int[size];
	if (alphas == null) {
	    for (int i = 0; i < size; i++) {
		rgb[i] = 0xff000000 | 
		         ((reds[i] & 0xff) << 16) |
		         ((greens[i] & 0xff) << 8) |
		          (blues[i] & 0xff);
	    }
	}
	else {
	    for (int i = 0; i < size; i++) {
		rgb[i] = ((alphas[i] & 0xff) << 24 | 
		         ((reds[i] & 0xff) << 16) |
		         ((greens[i] & 0xff) << 8) |
		          (blues[i] & 0xff);
	    }
	}
    }

    /**
     * Each array much contain <code>size</code> elements.  For each 
     * array, the i-th color is described by reds[i], greens[i], 
     * blues[i], alphas[i], unless alphas is not specified, then all the 
     * colors are opaque except for the transparent color. 
     *
     * @param bits the number of bits needed to represent <code>size</code> colors
     * @param size the number of colors in the color map
     * @param cmap packed color components
     * @param start the offset of the first color component in <code>cmap</code>
     * @param hasAlpha <code>cmap</code> has alpha values
     */
    public IndexColorModel(int bits, int size, byte[] cmap, int start, 
			   boolean hashAlpha) {
	this(bits, size, cmap, start, hasAlpha, -1);
    }

    /**
     * Each array much contain <code>size</code> elements.  For each 
     * array, the i-th color is described by reds[i], greens[i], 
     * blues[i], alphas[i], unless alphas is not specified, then all the 
     * colors are opaque except for the transparent color. 
     *
     * @param bits the number of bits needed to represent <code>size</code> colors
     * @param size the number of colors in the color map
     * @param cmap packed color components
     * @param start the offset of the first color component in <code>cmap</code>
     * @param hasAlpha <code>cmap</code> has alpha values
     * @param trans the index of the transparent color
     */
    public IndexColorModel(int bits, int size, byte[] cmap, int start, 
			   boolean hashAlpha, int trans) {
	super(bits);
	map_size = size;
        (hasAlpha) ? opaque = false : opaque = true;
	this.trans = trans;
    }

    public final int getMapSize() {
	return map_size;
    }

    /**
     * Get the index of the transparent color in this color model
     */
    public final int getTransparentPixel() {
	return trans;
    }

    /**
     * <br>
     * FIXME - needs to be implemented
     */
    public final void getReds(byte[] r) {

    }

    /**
     * <br>
     * FIXME - needs to be implemented
     */
    public final void getGreens(byte[] g) {

    }

    /**
     * <br>
     * FIXME - needs to be implemented
     */
    public final void getBlues(byte[] b) {

    }

    /**
     * <br>
     * FIXME - needs to be implemented
     */
    public final void getAlphas(byte[] a) {

    }

    /**
     * Get the red component of the given pixel.
     * <br>
     * FIXME - needs to be implemented
     * & some number of 1s... have to build
     * that up somehow... Paul forgets...
     */
    public final int getRed(int pixel) {
	return 0;
    }

    /**
     * Get the green component of the given pixel.
     * <br>
     * FIXME - needs to be implemented
     */
    public final int getGreen(int pixel) {
	return 0;
    }

    /**
     * Get the blue component of the given pixel.
     * <br>
     * FIXME - needs to be implemented
     */
    public final int getBlue(int pixel) {
	return 0;
    }

    /**
     * Get the alpha component of the given pixel.
     * <br>
     * FIXME - needs to be implemented
     */
    public final int getAlpha(int pixel) {
	return 0;
    }

    /**
     * Get the RGB color value of the given pixel using the default
     * RGB color model. 
     * <br>
     * FIXME - needs to be implemented
     *
     * @param pixel a pixel value
     */
    public final int getRGB(int pixel) {
	return 0;
    }

}

