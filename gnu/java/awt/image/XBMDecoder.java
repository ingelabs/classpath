package gnu.java.awt.image;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;

public class XBMDecoder extends ImageDecoder
{
  BufferedReader reader;
  static final ColorModel cm = ColorModel.getRGBdefault ();
  static final int black = 0xff000000;
  static final int transparent = 0x00000000;
  static final int masktable[] = { 0x01, 0x02, 0x04, 0x08, 
				   0x10, 0x20, 0x40, 0x80 };

  public XBMDecoder (InputStream is)
  {
    reader = new BufferedReader (new InputStreamReader (is));
  }

  public void produce (Vector v) throws IOException
  {
    int width = -1, height = -1;

    for (int i = 0; i < 2; i++)
      {
	String line = reader.readLine ();
	StringTokenizer st = new StringTokenizer (line);
	
	st.nextToken ();		// #define
	st.nextToken ();		// name_[width|height]
	if (i == 0)
	  width = Integer.parseInt (st.nextToken (), 10);
	else
	  height = Integer.parseInt (st.nextToken (), 10);
      }

    for (int i = 0; i < v.size (); i++)
      {
	ImageConsumer ic = (ImageConsumer) v.elementAt (i);

	ic.setDimensions (width, height);
	ic.setColorModel (cm);
	ic.setHints (ImageConsumer.COMPLETESCANLINES
		     | ImageConsumer.SINGLEFRAME
		     | ImageConsumer.SINGLEPASS
		     | ImageConsumer.TOPDOWNLEFTRIGHT);
      }

    /* skip to the byte array */
    while (reader.read () != '{') { }

    /* loop through each scanline */
    for (int line = 0; line < height; line++)
      {
	int scanline[] = getScanline (reader, width);

	for (int i = 0; i < v.size (); i++)
	  {
	    ImageConsumer ic = (ImageConsumer) v.elementAt (i);
	    ic.setPixels (0, 0 + line, width, 1, cm, scanline, 0, width);
	  }
      }

    /* tell each ImageConsumer that we're finished */
    for (int i = 0; i < v.size (); i++)
      {
	ImageConsumer ic = (ImageConsumer) v.elementAt (i);
	ic.imageComplete (ImageConsumer.STATICIMAGEDONE);
      }
  }    

  static public int[] getScanline (Reader in, int len) throws IOException
  {
    char byteStr[] = new char[2];
    int scanline[] = new int[len];
    int x = 0;

    while (x < len)
      {
	int ch = in.read ();
	if (ch == '0')
	  {
	    in.read ();		// 'x'
	    
	    byteStr[0] = (char) in.read ();
	    byteStr[1] = (char) in.read ();

	    int byteVal = Integer.parseInt (new String (byteStr), 16);

	    for (int i = 0; i < 8; i++, x++)
	      {
		if (x == len)	// condition occurs if bitmap is padded
		  return scanline;

		scanline[x] = ((byteVal & masktable[i]) != 0) ? 
		               black : transparent;
	      }
	  }	
      }

    return scanline;
  }
}
