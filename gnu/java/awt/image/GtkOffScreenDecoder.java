/* GtkOffScreenDecoder.java
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

package gnu.java.awt.image;

import java.awt.*;
import java.awt.image.*;
import gnu.java.awt.peer.gtk.*;


public class GtkOffScreenDecoder implements ImageProducer
{
  GtkOffScreenImage image;
  ImageConsumer consumer;

  public GtkOffScreenDecoder (GtkOffScreenImage image)
  {
    this.image = image;
  }

  public void addConsumer (ImageConsumer ic)
  {
    startProduction (ic);
  }

  public synchronized void startProduction (ImageConsumer ic)
  {
    consumer = ic;

    try
      {
	produce ();
      } 
    catch (Exception e) 
      {
	if (consumer != null)
	  consumer.imageComplete (ImageConsumer.IMAGEERROR);
      }

    consumer = null;
  }

  public synchronized boolean isConsumer (ImageConsumer ic)
  {
    return (ic == consumer);
  }

  public synchronized void removeConsumer (ImageConsumer ic)
  {
    if (consumer == ic)
      consumer = null;
  }

  public void requestTopDownLeftRightResend (ImageConsumer ic)
  {
  }

  void produce ()
  {
    consumer.setDimensions (image.getWidth (null), image.getHeight (null));
  }
}
  
