/*
 * GtkContainerPeer.java -- Implements ContainerPeer with GTK
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by James E. Blair <corvus@gnu.org>
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
 */

package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.event.*;
import java.awt.peer.ContainerPeer;

public class GtkContainerPeer extends GtkComponentPeer
  implements ContainerPeer
{
  Insets insets;
  Container c;

  public GtkContainerPeer(Container c)
  {
    super (c);
    this.c = c;
    insets = new Insets (0, 0, 0, 0);
  }

  public void beginValidate() 
  {
  }

  public void endValidate() 
  {
//      q.postEvent (new PaintEvent (awtComponent, PaintEvent.PAINT,
//  				 new Rectangle (x, y, width, height)));
//      Graphics gc = getGraphics ();
//      if (gc != null)
//        {
//  	awtComponent.update (gc);
//  	gc.dispose ();
//        }
//      System.out.println ("got here");
//      awtComponent.repaint ();
  }

  public Insets getInsets() 
  {
    return insets;
  }

  public Insets insets() 
  {
    return getInsets ();
  }

  public void setBounds (int x, int y, int width, int height)
  {
    super.setBounds (x, y, width, height);
    awtComponent.validate ();
  }

  public Graphics getGraphics ()
  {
    return new GdkGraphics (this);
  }

  public void handleEvent (AWTEvent event)
  {
    int id = event.getID();
      
    switch (id)
      {
      case PaintEvent.PAINT:
      case PaintEvent.UPDATE:
	{
	  try 
	    {
	      Graphics g = getGraphics ();
	      g.setClip (((PaintEvent)event).getUpdateRect());

	      if (id == PaintEvent.PAINT)
		awtComponent.paint (g);
	      else
		awtComponent.update (g);
	      
	      g.dispose ();
	    } 
	  catch (InternalError e)
	    { 
	      System.err.println (e);
	    }
	}
	break;
      }
  }
}
