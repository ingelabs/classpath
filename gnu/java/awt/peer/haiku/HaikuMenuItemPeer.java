/* HaikuMenuItemPeer.java -- part of the Haiku AWT peer
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

import java.awt.CheckboxMenuItem;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.peer.MenuItemPeer;
import java.util.HashMap;

/**
 * A menu item backed by a native BMenuItem.
 *
 * Each item has an id. When the user selects the item, the native side
 * sends the id back as an EV_MENU event, and {@link #activated(int)} posts
 * the AWT event.
 */
public class HaikuMenuItemPeer
  implements MenuItemPeer
{

  private static final HashMap<Integer,HaikuMenuItemPeer> items =
    new HashMap<Integer,HaikuMenuItemPeer>();

  private static int nextId = 1;

  final MenuItem awtItem;

  final int id;

  /** The native BMenuItem, or 0 for a menu (see HaikuMenuPeer). */
  long item;

  HaikuMenuItemPeer(MenuItem awtItem)
  {
    this.awtItem = awtItem;
    synchronized (items)
      {
        id = nextId++;
        items.put(new Integer(id), this);
      }
    if (! (this instanceof HaikuMenuPeer))
      {
        String label = awtItem.getLabel();
        boolean separator = "-".equals(label);
        int key = 0;
        int mods = 0;
        MenuShortcut s = awtItem.getShortcut();
        if (s != null && ! separator)
          {
            key = Character.toLowerCase((char) s.getKey());
            mods = HaikuNative.B_COMMAND_KEY;
            if (s.usesShiftModifier())
              mods |= HaikuNative.B_SHIFT_KEY;
          }
        item = HaikuNative.menuItemCreate(id, label, key, mods, separator);
        if (! awtItem.isEnabled())
          setEnabled(false);
      }
  }

  static HaikuMenuItemPeer find(int id)
  {
    synchronized (items)
      {
        return items.get(new Integer(id));
      }
  }

  /**
   * Called by the event pump when the user selected this item.
   */
  void activated(int modifiers)
  {
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
      new ActionEvent(awtItem, ActionEvent.ACTION_PERFORMED,
                      awtItem.getActionCommand(),
                      System.currentTimeMillis(), modifiers));
  }

  public void disable()
  {
    setEnabled(false);
  }

  public void enable()
  {
    setEnabled(true);
  }

  public void setEnabled(boolean enabled)
  {
    if (item != 0)
      HaikuNative.menuItemSetEnabled(item, enabled);
  }

  public void setLabel(String text)
  {
    if (item != 0)
      HaikuNative.menuItemSetLabel(item, text);
  }

  public void setFont(Font font)
  {
    // Menus use the system menu font.
  }

  public void dispose()
  {
    synchronized (items)
      {
        items.remove(new Integer(id));
      }
    disposeNative();
  }

  void disposeNative()
  {
    if (item != 0)
      {
        HaikuNative.menuItemDispose(item);
        item = 0;
      }
  }
}
