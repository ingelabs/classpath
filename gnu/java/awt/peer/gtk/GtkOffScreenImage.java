/* GtkOffScreenImage.java
   Copyright (C) 1999 Free Software Foundation, Inc.

This file is part of the peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */

package gnu.java.awt.peer.gtk;

import java.awt.*;
import java.awt.image.*;

public class GtkOffScreenImage extends Image
{
  int width, height;
  ImageProducer source;
  Graphics g;
  
  public GtkOffScreenImage (ImageProducer source, Graphics g,
			    int width, int height)
  {
    this.width = width;
    this.height = height;

    this.source = source;
    this.g = g;
  }

  public int getWidth (ImageObserver observer)
  {
    return width;
  }

  public int getHeight (ImageObserver observer)
  {
    return height;
  }

  public ImageProducer getSource ()
  {
    return source;
  }

  public Graphics getGraphics ()
  {
    return g;
  }

  public Object getProperty (String name, ImageObserver observer)
  {
    return Image.UndefinedProperty;
  }

  public void flush ()
  {
  }
}
