/* ColorModel.java -- Java class for interpreting Pixel objects
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
 * @author C. Brian Jones (cbj@gnu.org) 
 */
public abstract class ColorModel
{
    protected int pixel_bits;

    /**
     * Constructs the default color model.  The default color model 
     * can be obtained by calling <code>getRGBdefault</code> of this
     * class.
     * @param b the number of bits wide used for bit size of pixel values
     */
    public ColorModel(int b) {
	pixel_bits = b;
    }

    /**
     * Returns the default color model which in Sun's case is an instance
     * of <code>DirectColorModel</code>.
     */
    public static ColorModel getRGBdefault() {
	return new DirectColorModel(32, 0xff0000, 0xff00, 0xff, -0x1000000);
    }

    /**
     * Get get number of bits wide used for the bit size of pixel values
     */
    public int getPixelSize() {
	return pixel_bits;
    }

    /**
     * Get the red component of the given pixel.
     */
    public abstract int getRed(int pixel);

    /**
     * Get the green component of the given pixel.
     */
    public abstract int getGreen(int pixel);

    /**
     * Get the blue component of the given pixel.
     */
    public abstract int getBlue(int pixel);

    /**
     * Get the alpha component of the given pixel.
     */
    public abstract int getAlpha(int pixel);

    /**
     * Get the RGB color value of the given pixel using the default
     * RGB color model. 
     *
     * @param pixel a pixel value
     */
    public int getRGB(int pixel) {
	ColorModel cm = getRGBdefault();
	return (cm.getRGB(pixel));
    }

    /**
     * FIXME - What else should be done here?
     */
    public void finalize() {
	try {
	    super.finalize();
	} catch ( Throwable t ) { } 
    }

}

