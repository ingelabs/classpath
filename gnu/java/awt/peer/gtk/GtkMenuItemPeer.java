/* GtkMenuItemPeer.java -- Implements MenuItemPeer with GTK+
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
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuComponentPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuPeer;

public class GtkMenuItemPeer extends GtkMenuComponentPeer
  implements MenuItemPeer
{
  native void create (String label);

  public GtkMenuItemPeer (MenuItem item)
  {
    super (item);
    create (item.getLabel ());
    setEnabled (item.isEnabled ());
    setParent (item);
  }

  void setParent (MenuItem item)
  {
    // add ourself differently, based on what type of parent we have
    // yes, the typecasting here is nasty.
    Object parent = item.getParent ();
    if (parent instanceof MenuBar)
      {
	((GtkMenuBarPeer)((MenuBar)parent).getPeer ()).addMenu ((MenuPeer) this);
      }
    else // parent instanceof Menu
      {
	((GtkMenuPeer)((Menu)parent).getPeer ()).addItem (this, 
							  item.getShortcut ());
      }
  }

  public void disable ()
  {
    setEnabled (false);
  }

  public void enable ()
  {
    setEnabled (true);
  }

  native public void setEnabled (boolean b);
  native public void setLabel (String label);

  protected void postMenuActionEvent ()
  {
    postActionEvent (((MenuItem)awtWidget).getActionCommand (), 0);
  }
}
