/* HaikuCheckboxPeer.java -- part of the Haiku AWT peer
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

import gnu.java.awt.peer.swing.SwingCheckboxPeer;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Event;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JToggleButton;

/**
 * A checkbox drawn by Swing. In a CheckboxGroup it is drawn as a radio
 * button and cannot be cleared by a click.
 *
 * SwingCheckboxPeer calls the AWT listeners directly and has no
 * CheckboxGroup support. This peer posts the ItemEvent, and an AWT 1.0
 * ACTION_EVENT for components that use the old event model.
 */
public class HaikuCheckboxPeer
  extends SwingCheckboxPeer
  implements java.awt.dnd.peer.DropTargetPeer
{

  /**
   * A radio button icon. The look and feel icons need a JRadioButton.
   */
  private static final Icon RADIO = new Icon()
    {
      public int getIconWidth()
      {
        return 13;
      }

      public int getIconHeight()
      {
        return 13;
      }

      public void paintIcon(Component c, Graphics g, int x, int y)
      {
        ButtonModel m = ((AbstractButton) c).getModel();
        boolean on = m.isEnabled();
        g.setColor(m.isArmed() && m.isPressed() ? Color.lightGray
                                                : Color.white);
        g.fillOval(x, y, 12, 12);
        g.setColor(on ? Color.darkGray : Color.gray);
        g.drawOval(x, y, 12, 12);
        if (m.isSelected())
          {
            g.setColor(on ? Color.black : Color.gray);
            g.fillOval(x + 3, y + 3, 7, 7);
          }
      }
    };

  HaikuCheckboxPeer(final Checkbox cb)
  {
    super(cb);
    final JToggleButton jb = (JToggleButton) swingComponent.getJComponent();
    ItemListener[] l = jb.getItemListeners();
    for (int i = 0; i < l.length; i++)
      jb.removeItemListener(l[i]);
    jb.addItemListener(new ItemListener()
      {
        public void itemStateChanged(ItemEvent e)
        {
          boolean state = e.getStateChange() == ItemEvent.SELECTED;
          if (cb.getCheckboxGroup() != null && ! state)
            {
              // A radio button stays on until another one is chosen.
              setState(true);
              return;
            }
          // Checkbox.setState() does not update the group.
          if (cb.getCheckboxGroup() != null)
            cb.getCheckboxGroup().setSelectedCheckbox(cb);
          else
            cb.setState(state);
          ItemEvent ie = new ItemEvent(cb, ItemEvent.ITEM_STATE_CHANGED,
                                       cb.getLabel(),
                                       state ? ItemEvent.SELECTED
                                             : ItemEvent.DESELECTED);
          Event old = null;
          if (cb.getItemListeners().length == 0)
            old = new Event(cb, Event.ACTION_EVENT, Boolean.valueOf(state));
          HaikuSwingWrapper.post(cb, ie, old);
        }
      });
    setCheckboxGroup(cb.getCheckboxGroup());
  }

  public void setCheckboxGroup(CheckboxGroup group)
  {
    JToggleButton jb = (JToggleButton) swingComponent.getJComponent();
    jb.setIcon(group != null ? RADIO : null);
    jb.repaint();
  }

  /**
   * Sets the Swing state without item events. The listeners are taken
   * off, because SwingCheckboxPeer calls this before this class replaced
   * its listener.
   */
  public void setState(boolean state)
  {
    JToggleButton jb = (JToggleButton) swingComponent.getJComponent();
    ItemListener[] l = jb.getItemListeners();
    for (int i = 0; i < l.length; i++)
      jb.removeItemListener(l[i]);
    try
      {
        jb.setSelected(state);
      }
    finally
      {
        for (int i = 0; i < l.length; i++)
          jb.addItemListener(l[i]);
      }
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
