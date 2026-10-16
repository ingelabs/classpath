/* HaikuSwingWrapper.java -- part of the Haiku AWT peer
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

import gnu.java.awt.peer.swing.SwingComponent;
import gnu.java.awt.peer.swing.SwingComponentPeer;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Event;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Holds the Swing widgets of a Swing based peer.
 *
 * The Classpath Swing peers send all mouse events to the top Swing
 * component. Widgets made of several Swing components (a scroll bar with
 * arrow buttons, a list in a scroll pane) need the events at the deepest
 * component, so this wrapper finds it. While a button is held, the
 * component that got the press gets all events, as in AWT.
 */
class HaikuSwingWrapper
  extends JPanel
  implements SwingComponent
{

  /** Swing components that take key events from the wrapper. */
  interface KeyTarget
  {
    void routeKey(KeyEvent e);
  }

  private final SwingComponentPeer peer;

  private final Component awt;

  /** The component that gets key events, or null. */
  KeyTarget keyTarget;

  /** The component that got the last mouse press. */
  private Component grab;

  /** The component under the mouse. */
  private Component hover;

  HaikuSwingWrapper(SwingComponentPeer peer, Component awt, JComponent inner)
  {
    super(new BorderLayout());
    this.peer = peer;
    this.awt = awt;
    setOpaque(false);
    setDoubleBuffered(false);
    if (inner != null)
      add(inner, BorderLayout.CENTER);
    if (inner instanceof KeyTarget)
      keyTarget = (KeyTarget) inner;
  }

  public JComponent getJComponent()
  {
    return this;
  }

  public void handleMouseEvent(MouseEvent ev)
  {
    route(ev);
  }

  public void handleMouseMotionEvent(MouseEvent ev)
  {
    route(ev);
  }

  public void handleKeyEvent(KeyEvent ev)
  {
    if (keyTarget != null)
      keyTarget.routeKey(ev);
  }

  public void handleFocusEvent(FocusEvent ev)
  {
    // Nothing to do; the AWT component has the focus.
  }

  /**
   * Sends a mouse event (in wrapper coordinates) to the right Swing
   * component.
   */
  private void route(MouseEvent ev)
  {
    int id = ev.getID();
    int x = ev.getX();
    int y = ev.getY();
    Component target;
    switch (id)
      {
      case MouseEvent.MOUSE_PRESSED:
        target = deepest(x, y);
        grab = target;
        break;
      case MouseEvent.MOUSE_DRAGGED:
      case MouseEvent.MOUSE_RELEASED:
      case MouseEvent.MOUSE_CLICKED:
        target = grab != null ? grab : deepest(x, y);
        break;
      case MouseEvent.MOUSE_EXITED:
        if (hover != null)
          send(hover, ev, MouseEvent.MOUSE_EXITED);
        hover = null;
        return;
      case MouseEvent.MOUSE_ENTERED:
        return;
      default:
        target = deepest(x, y);
        break;
      }
    if (id == MouseEvent.MOUSE_MOVED || id == MouseEvent.MOUSE_DRAGGED)
      {
        Component under = deepest(x, y);
        if (under != hover)
          {
            if (hover != null)
              send(hover, ev, MouseEvent.MOUSE_EXITED);
            send(under, ev, MouseEvent.MOUSE_ENTERED);
            hover = under;
          }
      }
    send(target, ev, id);
  }

  private Component deepest(int x, int y)
  {
    Component c = SwingUtilities.getDeepestComponentAt(this, x, y);
    return c != null ? c : this;
  }

  private void send(Component target, MouseEvent ev, int id)
  {
    int x = ev.getX();
    int y = ev.getY();
    for (Component c = target; c != null && c != this; c = c.getParent())
      {
        x -= c.getX();
        y -= c.getY();
      }
    MouseEvent e = new MouseEvent(target, id, ev.getWhen(),
                                  ev.getModifiersEx(),
                                  x, y, ev.getClickCount(),
                                  ev.isPopupTrigger(), ev.getButton());
    if (target == this)
      {
        if (id == MouseEvent.MOUSE_MOVED || id == MouseEvent.MOUSE_DRAGGED)
          processMouseMotionEvent(e);
        else
          processMouseEvent(e);
      }
    else
      target.dispatchEvent(e);
  }

  // The wrapper is not in the AWT tree; take these from the peer.

  public Point getLocationOnScreen()
  {
    return peer.getLocationOnScreen();
  }

  public boolean isShowing()
  {
    return awt != null && awt.isShowing();
  }

  public Image createImage(int w, int h)
  {
    return peer.createImage(w, h);
  }

  public Graphics getGraphics()
  {
    return peer.getGraphics();
  }

  public Container getParent()
  {
    return awt != null ? awt.getParent() : null;
  }

  /**
   * Posts an AWT event for c, and an AWT 1.0 event when c has no
   * listener of the new kind. Classpath does not translate item and
   * adjustment events to the old model.
   */
  static void post(final Component c, final AWTEvent e, final Event old)
  {
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          c.dispatchEvent(e);
          if (old != null)
            c.postEvent(old);
        }
      });
  }
}
