/* GtkCanvasPeer.java
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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

package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.event.PaintEvent;
import java.awt.peer.*;

public class GtkCanvasPeer extends GtkComponentPeer implements CanvasPeer
{
  native void create ();

  public GtkCanvasPeer (Canvas c)
  {
    super (c);
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

  /* Preferred size for a drawing widget is always what the user requested */
  public Dimension getPreferredSize ()
  {
    return awtComponent.getSize ();
  }
}
