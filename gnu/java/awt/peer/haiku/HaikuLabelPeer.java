/* HaikuLabelPeer.java -- part of the Haiku AWT peer
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

import gnu.java.awt.peer.swing.SwingLabelPeer;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Label;

/**
 * A label drawn by Swing. An empty AWT label keeps the height of one
 * line of text, as in other toolkits; an empty JLabel has no height.
 */
public class HaikuLabelPeer
  extends SwingLabelPeer
  implements java.awt.dnd.peer.DropTargetPeer
{

  HaikuLabelPeer(Label label)
  {
    super(label);
  }

  private Dimension atLeastOneLine(Dimension d)
  {
    java.awt.Font f = awtComponent.getFont();
    if (f == null || awtComponent.getParent() == null)
      return d;
    FontMetrics fm = getFontMetrics(f);
    if (fm != null)
      d.height = Math.max(d.height, fm.getHeight());
    return d;
  }

  public Dimension getPreferredSize()
  {
    return atLeastOneLine(super.getPreferredSize());
  }

  public Dimension preferredSize()
  {
    return atLeastOneLine(super.preferredSize());
  }

  public Dimension getMinimumSize()
  {
    return atLeastOneLine(super.getMinimumSize());
  }

  public Dimension minimumSize()
  {
    return atLeastOneLine(super.minimumSize());
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
