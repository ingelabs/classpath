/*
 * GtkMenuPeer.java -- Implements MenuPeer with GTK+
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
import java.awt.peer.MenuPeer;
import java.awt.peer.MenuItemPeer;

public class GtkMenuPeer extends GtkMenuItemPeer
  implements MenuPeer
{
  native void create (String label);
  native void addItem (MenuItemPeer item);

  public GtkMenuPeer (Menu menu)
  {
    super (menu);
  }

  public void addItem (MenuItem item)
  {
    addItem ((MenuItemPeer) item.getPeer ());
  }

  public void addSeparator ()
  {
    addItem (new MenuItem ("-"));
  }

  native public void delItem (int index);
}
