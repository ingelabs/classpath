/*******************************************************************
 * DirectColorModel.java -- Java class for interpreting Pixel objects
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
public class DirectColorModel extends ColorModel
{
    private red_mask;
    private green_mask;
    private alpha_mask;
    private blue_mask;

    /**
     * For the color model created with this constructor the pixels
     * will have fully opaque alpha components with a value of 255.
     * Each mask should describe a fully contiguous set of bits in the
     * most likely order of red, green, blue from the most significant
     * byte to the least significant byte.
     * 
     * @param bits the number of bits wide used for bit size of pixel values
     * @param rmask the bits describing the red component of a pixel
     * @param gmask the bits describing the green component of a pixel
     * @param bmask the bits describing the blue component of a pixel 
     */
    public DirectColorModel(int bits, int rmask, int gmask, int bmask) {
	// 0 alpha mask determined from JDK 1.1
	this(bits, rmask, gmask, bmask, 0);  
    }

    /**
     * For the color model created with this constructor the pixels
     * will have fully opaque alpha components with a value of 255.
     * Each mask should describe a fully contiguous set of bits in the
     * most likely order of alpha, red, green, blue from the most significant
     * byte to the least significant byte.
     * 
     * @param bits the number of bits wide used for bit size of pixel values
     * @param rmask the bits describing the red component of a pixel
     * @param gmask the bits describing the green component of a pixel
     * @param bmask the bits describing the blue component of a pixel 
     * @param amask the bits describing the alpha component of a pixel 
     */
    public DirectColorModel(int bits, int rmask, int gmask, int bmask, int amask) {
	super(bits);
	red_mask = rmask;
	green_mask = gmask;
	blue_mask = bmask;
	alpha_mask = amask;
    }

    public final int getRedMask() {
	return red_mask;
    }

    public final int getGreenMask() {
	return green_mask;
    }

    public final int getBlueMask() {
	return blue_mask;
    }

    public final int getAlphaMask() {
	return alpha_mask;
    }

    /**
     * Get the red component of the given pixel.
     * <br>
     * FIXME - needs to be implemented.
     */
    public final int getRed(int pixel) {
	return 0;
    }

    /**
     * Get the green component of the given pixel.
     * <br>
     * FIXME - needs to be implemented.
     */
    public final int getGreen(int pixel) {
	return 0;
    }

    /**
     * Get the blue component of the given pixel.
     * <br>
     * FIXME - needs to be implemented.
     */
    public final int getBlue(int pixel) {
	return 0;
    }

    /**
     * Get the alpha component of the given pixel.
     * <br>
     * FIXME - needs to be implemented.
     */
    public final int getAlpha(int pixel) {
	return 0;
    }

    /**
     * Get the RGB color value of the given pixel using the default
     * RGB color model. 
     * <br>
     * FIXME - needs to be implemented.
     *
     * @param pixel a pixel value
     */
    public final int getRGB(int pixel) {
	return 0;
    }
}

