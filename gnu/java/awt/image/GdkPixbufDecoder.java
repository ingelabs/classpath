package gnu.java.awt.image;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.net.URL;

public class GdkPixbufDecoder extends ImageDecoder
{
  native static void initState ();

  static 
  {
    System.out.println ("loading gdkpixbuf library");	
    System.loadLibrary ("cpgdkpixbuf");
    initState ();
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
    System.out.println ("VALID: " + is.getFD ().valid ());

    loaderWrite (v, is.getFD ());

    for (int i = 0; i < v.size (); i++)
      {
	ImageConsumer ic = (ImageConsumer) v.elementAt (i);
	ic.imageComplete (ImageConsumer.STATICIMAGEDONE);
      }
  }
}
