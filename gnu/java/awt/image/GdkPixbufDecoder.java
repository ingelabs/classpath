/* GdkPixbufDecoder.java
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


package gnu.java.awt.image;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.net.URL;
import gnu.classpath.Configuration;

public class GdkPixbufDecoder extends ImageDecoder
{
  native static void initState ();

  static 
  {
    if (Configuration.INIT_LOAD_LIBRARY)
      {
	System.loadLibrary ("cpgdkpixbuf");
	initState ();
      }
  }

  /* gdk-pixbuf provids data in RGBA format */
  static final ColorModel cm = new DirectColorModel (32, 0xff000000, 
						         0x00ff0000, 
				          		 0x0000ff00, 
                                                         0x000000ff);
  public GdkPixbufDecoder (String filename)
  {
    super (filename);
  }

  public GdkPixbufDecoder (URL url)
  {
    super (url);
  }

  void areaPrepared (Vector v, int width, int height)
  {
    for (int i = 0; i < v.size (); i++)
      {
	ImageConsumer ic = (ImageConsumer) v.elementAt (i);
	ic.setDimensions (width, height);
	ic.setColorModel (cm);
	ic.setHints (ImageConsumer.RANDOMPIXELORDER);
      }
  }

  void areaUpdated (Vector v, int x, int y, int width, int height, 
		    int pixels[], int scansize)
  {
    for (int i = 0; i < v.size (); i++)
      {
	ImageConsumer ic = (ImageConsumer) v.elementAt (i);
	ic.setPixels (x, y, width, height, cm, pixels, 0, scansize);
      }
  }

  native void loaderWrite (Vector v, FileDescriptor fd);

  void produce (Vector v, FileInputStream is) throws IOException
  {
    loaderWrite (v, is.getFD ());

    for (int i = 0; i < v.size (); i++)
      {
	ImageConsumer ic = (ImageConsumer) v.elementAt (i);
	ic.imageComplete (ImageConsumer.STATICIMAGEDONE);
      }
  }
}
