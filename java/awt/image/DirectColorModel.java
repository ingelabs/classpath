/* DirectColorModel.java -- Java class for interpreting Pixel objects
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package java.awt.image;

/**
 *
 * @author C. Brian Jones (cbj@gnu.org), Mark Benvenuto (mcb54@columbia.edu )
 */
public class DirectColorModel extends ColorModel
{
    private int red_mask;
    private int green_mask;
    private int alpha_mask;
    private int blue_mask;

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
     */
    public final int getRed(int pixel) {
	return (red_mask & pixel );
    }

    /**
     * Get the green component of the given pixel.
     * <br>
     */
    public final int getGreen(int pixel) {
	return (green_mask & pixel );
    }

    /**
     * Get the blue component of the given pixel.
     * <br>
     */
    public final int getBlue(int pixel) {
	return (blue_mask & pixel );
    }

    /**
     * Get the alpha component of the given pixel.
     * <br>
     */
    public final int getAlpha(int pixel) {
	return (alpha_mask & pixel );
    }

    /**
     * Get the RGB color value of the given pixel using the default
     * RGB color model. 
     * <br>
     * FIXME - sun's documentation is parse - unsure ?
     *
     * @param pixel a pixel value
     */
    public final int getRGB(int pixel) {
	return makeColor( getAlpha(pixel), getRed( pixel ), getGreen( pixel ), getBlue( pixel ) );
    }   

    private int makeColor( int a, int r, int g, int b )
    {
	return (int)( 0xff000000 & (a << 24) | 0xff0000 & (r << 16) | 0xff00 & (b << 8) | 0xff & g ); 
    }


}

