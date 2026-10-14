/* HaikuPanelPeer.java -- part of the Haiku AWT peer
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

import gnu.java.awt.peer.swing.SwingPanelPeer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Rectangle;

/**
 * A panel. Like a native panel, it clears its background before AWT
 * paints it; SwingPanelPeer does not.
 */
public class HaikuPanelPeer
  extends SwingPanelPeer
  implements java.awt.dnd.peer.DropTargetPeer
{

  HaikuPanelPeer(Panel panel)
  {
    super(panel);
  }

  protected void peerPaintComponent(Graphics g)
  {
    clearBackground(awtComponent, g);
    // Component.update() clears with the graphics background colour; the
    // double buffer image's graphics would use white.
    Color bg = awtComponent.getBackground();
    if (bg != null && g instanceof java.awt.Graphics2D)
      ((java.awt.Graphics2D) g).setBackground(bg);
    // paint() gets a copy of g; it must start with the component's
    // foreground colour and font, not the background colour.
    g.setColor(awtComponent.getForeground());
    if (awtComponent.getFont() != null)
      g.setFont(awtComponent.getFont());
  }

  static void clearBackground(java.awt.Component c, Graphics g)
  {
    Color bg = c.getBackground();
    if (bg == null)
      bg = HaikuWindowPeer.DEFAULT_BACKGROUND;
    Rectangle clip = g.getClipBounds();
    if (clip == null)
      clip = new Rectangle(0, 0, c.getWidth(), c.getHeight());
    g.setColor(bg);
    g.fillRect(clip.x, clip.y, clip.width, clip.height);
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
