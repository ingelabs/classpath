/* HaikuChoicePeer.java -- part of the Haiku AWT peer
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

import java.awt.Choice;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.peer.ChoicePeer;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * A choice. A Swing JComboBox draws the closed choice; the list of items
 * is a native BPopUpMenu, so no Swing popup window is needed.
 */
public class HaikuChoicePeer
  extends HaikuSwingPeer
  implements ChoicePeer
{

  private static final HashMap<Integer,HaikuChoicePeer> choices =
    new HashMap<Integer,HaikuChoicePeer>();

  private static int nextId = 1;

  private final int id;

  private final Choice awtChoice;

  private final DefaultComboBoxModel model = new DefaultComboBoxModel();

  private final JComboBox combo;

  HaikuChoicePeer(Choice choice)
  {
    awtChoice = choice;
    synchronized (choices)
      {
        id = nextId++;
        choices.put(new Integer(id), this);
      }
    for (int i = 0; i < choice.getItemCount(); i++)
      model.addElement(choice.getItem(i));
    combo = new JComboBox(model);
    combo.setFocusable(false);
    if (choice.getSelectedIndex() >= 0)
      combo.setSelectedIndex(choice.getSelectedIndex());
    initWrapped(choice, combo);
  }

  static HaikuChoicePeer find(int id)
  {
    synchronized (choices)
      {
        return choices.get(new Integer(id));
      }
  }

  protected void handleMouseEvent(MouseEvent e)
  {
    if (e.getID() == MouseEvent.MOUSE_PRESSED
        && e.getButton() == MouseEvent.BUTTON1 && awtChoice.isEnabled())
      showPopup();
  }

  protected void handleMouseMotionEvent(MouseEvent e)
  {
    // The native popup tracks the mouse itself.
  }

  protected void handleKeyEvent(KeyEvent e)
  {
    if (e.getID() != KeyEvent.KEY_PRESSED)
      return;
    int i = awtChoice.getSelectedIndex();
    switch (e.getKeyCode())
      {
      case KeyEvent.VK_UP:
        if (i > 0)
          selected(i - 1);
        break;
      case KeyEvent.VK_DOWN:
        if (i < awtChoice.getItemCount() - 1)
          selected(i + 1);
        break;
      case KeyEvent.VK_SPACE:
      case KeyEvent.VK_ENTER:
        showPopup();
        break;
      default:
        break;
      }
  }

  private void showPopup()
  {
    int n = awtChoice.getItemCount();
    if (n == 0)
      return;
    String[] items = new String[n];
    for (int i = 0; i < n; i++)
      items[i] = awtChoice.getItem(i);
    Point p = getLocationOnScreen();
    HaikuNative.choicePopup(id, items, awtChoice.getSelectedIndex(),
                            p.x, p.y + awtChoice.getHeight());
  }

  /**
   * Called when the user picked an item.
   */
  void selected(final int index)
  {
    java.awt.EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          if (index < 0 || index >= awtChoice.getItemCount()
              || index == awtChoice.getSelectedIndex())
            return;
          awtChoice.select(index);
          String item = awtChoice.getItem(index);
          ItemEvent e = new ItemEvent(awtChoice,
                                      ItemEvent.ITEM_STATE_CHANGED, item,
                                      ItemEvent.SELECTED);
          Event old = null;
          if (awtChoice.getItemListeners().length == 0)
            old = new Event(awtChoice, Event.ACTION_EVENT, item);
          HaikuSwingWrapper.post(awtChoice, e, old);
        }
      });
  }

  public void add(String item, int index)
  {
    if (index < 0 || index > model.getSize())
      index = model.getSize();
    model.insertElementAt(item, index);
  }

  public void addItem(String item, int index)
  {
    add(item, index);
  }

  public void remove(int index)
  {
    model.removeElementAt(index);
  }

  public void removeAll()
  {
    model.removeAllElements();
  }

  public void select(int index)
  {
    if (index >= 0 && index < model.getSize())
      combo.setSelectedIndex(index);
  }

  public void dispose()
  {
    synchronized (choices)
      {
        choices.remove(new Integer(id));
      }
    super.dispose();
  }
}
