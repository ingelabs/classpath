/* ImageDecoder.java
   Copyright (C) 1999, 2000 Free Software Foundation, Inc.

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

public abstract class ImageDecoder implements ImageProducer 
{
  Vector consumers = new Vector ();
  String filename;
  URL url;

  public static ColorModel cm;

  static
  {
    // FIXME: there was some broken code here that looked like
    // it wanted to rely on this property.  I don't have any idea
    // what it was intended to do.
    // String endian = System.getProperties ().getProperty ("gnu.cpu.endian");
  }

  public ImageDecoder (String filename)
  {
    this.filename = filename;
  }

  public ImageDecoder (URL url)
  {
    this.url = url;
  }

  public void addConsumer (ImageConsumer ic) 
  {
    consumers.addElement (ic);
  }

  public boolean isConsumer (ImageConsumer ic)
  {
    return consumers.contains (ic);
  }
  
  public void removeConsumer (ImageConsumer ic)
  {
    consumers.removeElement (ic);
  }

  public void startProduction (ImageConsumer ic)
  {
    addConsumer (ic);
    Vector list = (Vector) consumers.clone ();
    try 
      {
	FileInputStream is = (url == null) ? new FileInputStream (filename) :
	                                  (FileInputStream) url.openStream();
						  
	produce (list, is);
      } 
    catch (Exception e)
      {
	for (int i = 0; i < list.size (); i++)
	  {
	    ImageConsumer ic2 = (ImageConsumer) list.elementAt (i);
	    ic2.imageComplete (ImageConsumer.IMAGEERROR);
	  }
      }
  }

  public void requestTopDownLeftRightResend (ImageConsumer ic) 
  { 
  }

  abstract void produce (Vector v, FileInputStream is) throws IOException;
}
