/*
 * GtkScrollbarPeer.java -- Implements ScrollbarPeer with GTK
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
import java.awt.peer.*;

public class GtkScrollbarPeer extends GtkComponentPeer
    implements ScrollbarPeer
{

  native void gtkScrollbarNew (int orientation, int value, int min, int max);
  native public void setLineIncrement (int amount);
  native public void setPageIncrement (int amount);
  native public void setValues (int value, int size, int min, int max);
  native public void dispose ();

  public GtkScrollbarPeer (Scrollbar s, ComponentPeer cp)
  {
    super (s);

    gtkScrollbarNew ((s.getOrientation()==Scrollbar.HORIZONTAL)?1:0,
		     s.getValue(),
		     s.getMinimum(),
		     s.getMaximum());

    Point p=s.getLocation();
    
    gtkFixedPut (cp, p.x, p.y);
  }
}
