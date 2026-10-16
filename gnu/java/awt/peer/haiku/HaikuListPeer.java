/* HaikuListPeer.java -- part of the Haiku AWT peer
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

import java.awt.Dimension;
import java.awt.Event;
import java.awt.FontMetrics;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.peer.ListPeer;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * A list drawn by a Swing JList in a JScrollPane.
 */
public class HaikuListPeer
  extends HaikuSwingPeer
  implements ListPeer
{

  private class KeyList
    extends JList
    implements HaikuSwingWrapper.KeyTarget
  {
    KeyList()
    {
      super(model);
    }

    public void routeKey(KeyEvent e)
    {
      processKeyEvent(new KeyEvent(this, e.getID(), e.getWhen(),
                                   e.getModifiersEx(), e.getKeyCode(),
                                   e.getKeyChar(), e.getKeyLocation()));
    }
  }

  private final List awtList;

  private final DefaultListModel model = new DefaultListModel();

  private final KeyList jList;

  private final JScrollPane scroll;

  /** True while AWT changes the selection; do not echo it back. */
  private boolean setting;

  /** The selection last reported to AWT. */
  private int[] reported = new int[0];

  HaikuListPeer(final List list)
  {
    awtList = list;
    jList = new KeyList();
    String[] items = list.getItems();
    for (int i = 0; i < items.length; i++)
      model.addElement(items[i]);
    setMultipleMode(list.isMultipleMode());
    int[] sel = list.getSelectedIndexes();
    setting = true;
    jList.setSelectedIndices(sel);
    setting = false;
    reported = sel;
    jList.addListSelectionListener(new ListSelectionListener()
      {
        public void valueChanged(ListSelectionEvent e)
        {
          if (! setting && ! e.getValueIsAdjusting())
            selectionChanged();
        }
      });
    jList.addMouseListener(new MouseAdapter()
      {
        public void mouseClicked(MouseEvent e)
        {
          if (e.getClickCount() == 2)
            {
              int i = jList.locationToIndex(e.getPoint());
              if (i >= 0)
                HaikuSwingWrapper.post(list,
                  new ActionEvent(list, ActionEvent.ACTION_PERFORMED,
                                  list.getItem(i)), null);
            }
        }
      });
    scroll = new JScrollPane(jList);
    // The backing store mode needs createImage() on a lightweight
    // component, which has no peer to make the image.
    scroll.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);
    initWrapped(list, scroll);
    wrapper.keyTarget = jList;
  }

  /** Posts item events for every index that changed. */
  private void selectionChanged()
  {
    int[] now = jList.getSelectedIndices();
    int[] before = reported;
    reported = now;
    boolean old = awtList.getItemListeners().length == 0;
    for (int i = 0; i < now.length; i++)
      if (! contains(before, now[i]))
        post(now[i], true, old);
    for (int i = 0; i < before.length; i++)
      if (! contains(now, before[i]))
        post(before[i], false, old);
  }

  private void post(int index, boolean selected, boolean old)
  {
    ItemEvent e = new ItemEvent(awtList, ItemEvent.ITEM_STATE_CHANGED,
                                new Integer(index),
                                selected ? ItemEvent.SELECTED
                                         : ItemEvent.DESELECTED);
    Event oe = null;
    if (old)
      oe = new Event(awtList, selected ? Event.LIST_SELECT
                                       : Event.LIST_DESELECT,
                     new Integer(index));
    HaikuSwingWrapper.post(awtList, e, oe);
  }

  private static boolean contains(int[] a, int v)
  {
    for (int i = 0; i < a.length; i++)
      if (a[i] == v)
        return true;
    return false;
  }

  private void setSelection(int[] sel)
  {
    setting = true;
    try
      {
        jList.setSelectedIndices(sel);
        reported = jList.getSelectedIndices();
      }
    finally
      {
        setting = false;
      }
  }

  public void add(String item, int index)
  {
    if (index < 0 || index > model.size())
      index = model.size();
    model.add(index, item);
  }

  public void addItem(String item, int index)
  {
    add(item, index);
  }

  public void clear()
  {
    removeAll();
  }

  public void delItems(int start, int end)
  {
    model.removeRange(start, end);
    reported = jList.getSelectedIndices();
  }

  public void deselect(int index)
  {
    setting = true;
    try
      {
        jList.removeSelectionInterval(index, index);
        reported = jList.getSelectedIndices();
      }
    finally
      {
        setting = false;
      }
  }

  public int[] getSelectedIndexes()
  {
    return jList.getSelectedIndices();
  }

  public void makeVisible(int index)
  {
    jList.ensureIndexIsVisible(index);
  }

  public void removeAll()
  {
    model.clear();
    reported = new int[0];
  }

  public void select(int index)
  {
    if (jList.getSelectionMode() == ListSelectionModel.SINGLE_SELECTION)
      setSelection(new int[] { index });
    else
      {
        setting = true;
        try
          {
            jList.addSelectionInterval(index, index);
            reported = jList.getSelectedIndices();
          }
        finally
          {
            setting = false;
          }
      }
  }

  public void setMultipleMode(boolean multi)
  {
    jList.setSelectionMode(multi
                           ? ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
                           : ListSelectionModel.SINGLE_SELECTION);
  }

  public void setMultipleSelections(boolean multi)
  {
    setMultipleMode(multi);
  }

  public Dimension getPreferredSize(int rows)
  {
    return preferredSize(rows);
  }

  public Dimension getMinimumSize(int rows)
  {
    return minimumSize(rows);
  }

  public Dimension preferredSize(int rows)
  {
    FontMetrics fm = jList.getFontMetrics(jList.getFont());
    int rowH = fm.getHeight();
    int w = 0;
    for (int i = 0; i < model.size(); i++)
      w = Math.max(w, fm.stringWidth(String.valueOf(model.get(i))));
    Insets in = scroll.getInsets();
    int sb = scroll.getVerticalScrollBar().getPreferredSize().width;
    return new Dimension(Math.max(w, 4 * rowH) + 8 + sb + in.left + in.right,
                         rows * rowH + 4 + in.top + in.bottom);
  }

  public Dimension minimumSize(int rows)
  {
    return preferredSize(rows);
  }
}
