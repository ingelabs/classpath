/* HaikuScrollbarPeer.java -- part of the Haiku AWT peer
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

import java.awt.Event;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.peer.ScrollbarPeer;

import javax.swing.JScrollBar;

/**
 * A scroll bar drawn by a Swing JScrollBar.
 */
public class HaikuScrollbarPeer
  extends HaikuSwingPeer
  implements ScrollbarPeer
{

  private final JScrollBar bar;

  /** True while AWT sets the values; do not echo them back. */
  private boolean setting;

  HaikuScrollbarPeer(final Scrollbar sb)
  {
    bar = new JScrollBar(sb.getOrientation() == Scrollbar.HORIZONTAL
                         ? JScrollBar.HORIZONTAL : JScrollBar.VERTICAL);
    setValues(sb.getValue(), sb.getVisibleAmount(), sb.getMinimum(),
              sb.getMaximum());
    setLineIncrement(sb.getUnitIncrement());
    setPageIncrement(sb.getBlockIncrement());
    bar.addAdjustmentListener(new AdjustmentListener()
      {
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
          if (setting)
            return;
          int type = e.getAdjustmentType();
          int value = e.getValue();
          AdjustmentEvent ae =
            new AdjustmentEvent(sb, AdjustmentEvent.ADJUSTMENT_VALUE_CHANGED,
                                type, value, e.getValueIsAdjusting());
          Event old = null;
          if (sb.getAdjustmentListeners().length == 0)
            old = new Event(sb, oldType(type), new Integer(value));
          HaikuSwingWrapper.post(sb, ae, old);
        }
      });
    initWrapped(sb, bar);
  }

  static int oldType(int type)
  {
    switch (type)
      {
      case AdjustmentEvent.UNIT_DECREMENT:
        return Event.SCROLL_LINE_UP;
      case AdjustmentEvent.UNIT_INCREMENT:
        return Event.SCROLL_LINE_DOWN;
      case AdjustmentEvent.BLOCK_DECREMENT:
        return Event.SCROLL_PAGE_UP;
      case AdjustmentEvent.BLOCK_INCREMENT:
        return Event.SCROLL_PAGE_DOWN;
      default:
        return Event.SCROLL_ABSOLUTE;
      }
  }

  public void setLineIncrement(int inc)
  {
    bar.setUnitIncrement(inc);
  }

  public void setPageIncrement(int inc)
  {
    bar.setBlockIncrement(inc);
  }

  public void setValues(int value, int visible, int min, int max)
  {
    setting = true;
    try
      {
        bar.setValues(value, visible, min, max);
      }
    finally
      {
        setting = false;
      }
  }
}
