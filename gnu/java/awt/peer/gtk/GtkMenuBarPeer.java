/* GtkMenuBarPeer.java -- Implements MenuBarPeer with GTK+
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
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuPeer;

public class GtkMenuBarPeer extends GtkMenuComponentPeer
  implements MenuBarPeer
{

  native void create ();
  native void addMenu (MenuPeer menu);

  public GtkMenuBarPeer (MenuBar target)
  {
    super (target);
    create ();
  }

  /* In Gnome, help menus are no longer right flushed. */
  public void addHelpMenu (Menu menu)
  {
    addMenu (menu);
  }

  public void addMenu (Menu menu)
  {
    addMenu ((MenuPeer) menu.getPeer ());
  }

  native public void delMenu (int index);
}
