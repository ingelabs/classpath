/*
 * GtkButtonPeer.java -- Implements ButtonPeer with GTK
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
import java.awt.event.MouseEvent;
import java.awt.peer.*;

public class GtkButtonPeer extends GtkComponentPeer
    implements ButtonPeer
{
  native void gtkButtonNewWithLabel (String label, boolean visible);
  native void gtkButtonLabelSet (String label);    

  public GtkButtonPeer (Button b, ComponentPeer cp)
  {
    super (b);
    gtkButtonNewWithLabel (b.getLabel(), b.isVisible());
    Point p = b.getLocation ();
    System.out.println ("buttonpeer: location: "+p.x+","+p.y);
    
    gtkFixedPut (cp, p.x, p.y);
  }
    
  public void setLabel (String label) 
  {
    System.out.println ("java setlabel");
    gtkButtonLabelSet (label);
  }

  public void handleEvent (AWTEvent e)
  {
    if (e instanceof MouseEvent)
      {
	MouseEvent me = (MouseEvent) e;
	if (!me.isConsumed()
	    && me.getID() == MouseEvent.MOUSE_CLICKED
	    && (me.getModifiers() & MouseEvent.BUTTON1_MASK) != 0)
	  postActionEvent (((Button)awtComponent).getActionCommand (), 
			   me.getModifiers ());
      }

    super.handleEvent (e);
  }
}
