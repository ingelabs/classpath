/* GtkMenuPeer.java -- Implements MenuPeer with GTK+
   Copyright (C) 1999 Free Software Foundation, Inc.

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
import java.awt.peer.MenuPeer;
import java.awt.peer.MenuItemPeer;

public class GtkMenuPeer extends GtkMenuItemPeer
  implements MenuPeer
{
  native void create (String label);
  native void addItem (MenuItemPeer item, int key, boolean shiftModifier);
  native void setupAccelGroup (GtkGenericPeer container);

  public GtkMenuPeer (Menu menu)
  {
    super (menu);
    
    MenuContainer parent = menu.getParent ();
    if (parent instanceof Menu)
      setupAccelGroup ((GtkGenericPeer)((Menu)parent).getPeer ());
    else if (parent instanceof Component)
      setupAccelGroup ((GtkGenericPeer)((Component)parent).getPeer ());
    else
      setupAccelGroup (null);
  }

  public void addItem (MenuItem item)
  {
    int key = 0;
    boolean shiftModifier = false;

    MenuShortcut ms = item.getShortcut ();
    if (ms != null)
      {
	key = ms.getKey ();
	shiftModifier = ms.usesShiftModifier ();
      }

    addItem ((MenuItemPeer) item.getPeer (), key, shiftModifier);
  }

  public void addItem (MenuItemPeer item, MenuShortcut ms)
  {
    int key = 0;
    boolean shiftModifier = false;

    if (ms != null)
      {
	key = ms.getKey ();
	shiftModifier = ms.usesShiftModifier ();
      }

    addItem (item, key, shiftModifier);
  }

  public void addSeparator ()
  {
    addItem (new MenuItem ("-"));
  }

  native public void delItem (int index);
}
