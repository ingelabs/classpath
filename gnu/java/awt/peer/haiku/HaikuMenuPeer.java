/* HaikuMenuPeer.java -- part of the Haiku AWT peer
   Copyright (C) 2026 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */



package gnu.java.awt.peer.haiku;

import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.peer.MenuPeer;

/**
 * A menu backed by a native BMenu. Its items are added by AWT through
 * {@link #addItem(MenuItem)} (see Menu.addNotify()).
 */
public class HaikuMenuPeer
  extends HaikuMenuItemPeer
  implements MenuPeer
{

  /** The native BMenu. */
  long menu;

  HaikuMenuPeer(Menu awtMenu)
  {
    this(awtMenu, HaikuNative.MENU);
  }

  HaikuMenuPeer(Menu awtMenu, int kind)
  {
    super(awtMenu);
    menu = HaikuNative.menuCreate(kind, awtMenu.getLabel());
    if (! awtMenu.isEnabled())
      setEnabled(false);
  }

  /**
   * Adds the native object of an item (or submenu) to a native menu.
   */
  static void addTo(long parent, MenuItem mi, int index)
  {
    Object p = mi.getPeer();
    if (p instanceof HaikuMenuPeer)
      HaikuNative.menuAddSubmenu(parent, ((HaikuMenuPeer) p).menu, index);
    else if (p instanceof HaikuMenuItemPeer)
      HaikuNative.menuAddItem(parent, ((HaikuMenuItemPeer) p).item, index);
  }

  public void addItem(MenuItem item)
  {
    // AWT adds items in order, but Menu.insert() deletes the tail and
    // adds it again; the index keeps the native order the same.
    Menu m = (Menu) awtItem;
    int index = -1;
    for (int i = 0; i < m.getItemCount(); i++)
      if (m.getItem(i) == item)
        index = i;
    addTo(menu, item, index);
  }

  public void addSeparator()
  {
    HaikuNative.menuAddSeparator(menu);
  }

  public void delItem(int index)
  {
    HaikuNative.menuRemoveItem(menu, index);
  }

  public void setEnabled(boolean enabled)
  {
    if (menu != 0)
      HaikuNative.menuSetEnabled(menu, enabled);
  }

  public void setLabel(String text)
  {
    HaikuNative.menuSetLabel(menu, text);
  }

  void disposeNative()
  {
    if (menu != 0)
      {
        HaikuNative.menuDispose(menu);
        menu = 0;
      }
  }
}
