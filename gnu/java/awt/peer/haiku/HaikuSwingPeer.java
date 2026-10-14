/* HaikuSwingPeer.java -- part of the Haiku AWT peer
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

import gnu.java.awt.peer.swing.SwingComponentPeer;

import java.awt.Component;

import javax.swing.JComponent;

/**
 * Base for Swing based peers whose Swing widget is held in a
 * {@link HaikuSwingWrapper}.
 */
class HaikuSwingPeer
  extends SwingComponentPeer
  implements java.awt.dnd.peer.DropTargetPeer
{

  HaikuSwingWrapper wrapper;

  void initWrapped(Component awt, JComponent inner)
  {
    wrapper = new HaikuSwingWrapper(this, awt, inner);
    noFocus(wrapper);
    init(awt, wrapper);
    if (awt.getFont() != null)
      setFont(awt.getFont());
    if (awt.getBackground() != null)
      setBackground(awt.getBackground());
    if (awt.getForeground() != null)
      setForeground(awt.getForeground());
  }

  /**
   * Makes the Swing components unable to take the focus. The AWT
   * component has the focus and the wrapper sends key events on. AWT
   * focus requests from a Swing component would look for a Window in a
   * tree that ends at the wrapper.
   */
  static void noFocus(java.awt.Container c)
  {
    c.setFocusable(false);
    for (Component child : c.getComponents())
      if (child instanceof java.awt.Container)
        noFocus((java.awt.Container) child);
      else
        child.setFocusable(false);
  }

  public void reshape(int x, int y, int width, int height)
  {
    super.reshape(x, y, width, height);
    if (wrapper != null)
      wrapper.validate();
  }

  public void setFont(java.awt.Font font)
  {
    super.setFont(font);
    if (wrapper != null)
      for (Component c : wrapper.getComponents())
        c.setFont(font);
  }

  public void setBackground(java.awt.Color color)
  {
    // Keep the look and feel colours of the inner widgets; only the
    // wrapper takes the AWT background.
    super.setBackground(color);
  }

  /** Shows the new cursor if the mouse is over this component. */
  public void setCursor(java.awt.Cursor cursor)
  {
    super.setCursor(cursor);
    HaikuEventPump.get().refreshCursor();
  }

  // Drop targets are found by the event pump; nothing to register.

  public void addDropTarget(java.awt.dnd.DropTarget target)
  {
  }

  public void removeDropTarget(java.awt.dnd.DropTarget target)
  {
  }
}
