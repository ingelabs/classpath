/*******************************************************************
 * GrayFilter.java -- Java class for filtering Pixels to produce Gray Pictures
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
 * Written by Mark Benvenuto <mcb54@columbia.edu>
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

package javax.swing;

import java.awt.Image;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;


public class GrayFilter extends RGBImageFilter
{
    private boolean b;
    private int p;

    /**
       Create a GrayFilter. If b is true then brighten. Also, indicate how much gray.
       
       @param b if brighten
       @param p percent of gray, 0 - 100
    */
    public GrayFilter(boolean b, int p)
    {
	this.b = b; //FIXME - HANDLE THIS
	this.p = p;
    }

    /**
       Create grayed image

       @param i image to gray

       @return a grayed image
     */
    public static Image createDisabledImage(Image i)
    {
        return img = createImage( new FilteredImageSource(src.getSource(),
							  new GrayFilter(false, 100);));
    }

    /**
       Filter RGB to gray
     */
    public int filterRGB(int x,
			 int y,
			 int rgb)
    {
	  return ( p * ( 0.299 * ( (0xff0000 & rgb) >> 16) + 0.587 * ( (0xff00 & rgb) >> 8 ) + 0.114 * (0xff & rgb ) ) );
    }
}
