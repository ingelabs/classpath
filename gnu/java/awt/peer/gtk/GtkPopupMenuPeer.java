/*
 * GtkPopupMenuPeer.java -- Implements PopupMenuPeer with GTK+
 *
 * Copyright (c) 1999 Free Software Foundation, Inc.
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
import java.awt.peer.PopupMenuPeer;

public class GtkPopupMenuPeer extends GtkMenuPeer
  implements PopupMenuPeer
{
  public GtkPopupMenuPeer (PopupMenu menu)
  {
    super (menu);
  }

  void setParent (MenuItem item)
  {
    // popup's lack a parent, so we do nothing here
  }

  native void show (int x, int y, long time);
  public void show (Event e)
  {
    Point abs = ((Component)e.target).getLocationOnScreen ();
    show (abs.x + e.x, abs.y + e.y, e.when);
  }
}
