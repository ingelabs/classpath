/* HaikuScrollPanePeer.java -- part of the Haiku AWT peer
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

import gnu.java.awt.peer.swing.SwingContainerPeer;

import java.awt.Adjustable;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.peer.ScrollPanePeer;

import javax.swing.JScrollBar;

/**
 * A scroll pane with Swing scroll bars at its right and bottom edges.
 *
 * ScrollPane.layout() puts the child at (0, 0) and asks the peer to
 * scroll. This peer scrolls by moving the child to (-x, -y), and clips
 * the child to the viewport when it paints.
 */
public class HaikuScrollPanePeer
  extends SwingContainerPeer
  implements ScrollPanePeer, java.awt.dnd.peer.DropTargetPeer
{

  private final ScrollPane sp;

  private final HaikuSwingWrapper wrapper;

  private final JScrollBar hbar;

  private final JScrollBar vbar;

  private int scrollX, scrollY;

  private int childW, childH;

  /** True while the peer sets the scroll bars; do not echo them back. */
  private boolean setting;

  HaikuScrollPanePeer(ScrollPane pane)
  {
    super(pane);
    sp = pane;
    hbar = new JScrollBar(JScrollBar.HORIZONTAL);
    vbar = new JScrollBar(JScrollBar.VERTICAL);
    wrapper = new HaikuSwingWrapper(this, pane, null);
    wrapper.setLayout(null);
    wrapper.add(hbar);
    wrapper.add(vbar);
    HaikuSwingPeer.noFocus(wrapper);
    AdjustmentListener l = new AdjustmentListener()
      {
        public void adjustmentValueChanged(AdjustmentEvent e)
        {
          if (setting)
            return;
          final int x = hbar.getValue();
          final int y = vbar.getValue();
          EventQueue.invokeLater(new Runnable()
            {
              public void run()
              {
                if (sp.getComponentCount() > 0)
                  sp.setScrollPosition(x, y);
              }
            });
        }
      };
    // AWT's default unit increment of one pixel is too small for arrow
    // clicks; native toolkits use a larger step too.
    hbar.setUnitIncrement(10);
    vbar.setUnitIncrement(10);
    hbar.addAdjustmentListener(l);
    vbar.addAdjustmentListener(l);
    init(pane, wrapper);
  }

  /** True if (x, y), in scroll pane coordinates, is in the viewport. */
  boolean inViewport(int x, int y)
  {
    Dimension vp = sp.getViewportSize();
    return x >= 0 && y >= 0 && x < vp.width && y < vp.height;
  }

  /** Places and sets up the scroll bars for the current sizes. */
  private void layoutBars()
  {
    int w = sp.getWidth();
    int h = sp.getHeight();
    Dimension vp = sp.getViewportSize();
    boolean needV = vp.width < w;
    boolean needH = vp.height < h;
    setting = true;
    try
      {
        vbar.setVisible(needV);
        hbar.setVisible(needH);
        if (needV)
          {
            vbar.setBounds(vp.width, 0, w - vp.width, vp.height);
            vbar.validate();
          }
        if (needH)
          {
            hbar.setBounds(0, vp.height, vp.width, h - vp.height);
            hbar.validate();
          }
        hbar.setValues(scrollX, vp.width, 0, Math.max(childW, vp.width));
        vbar.setValues(scrollY, vp.height, 0, Math.max(childH, vp.height));
        hbar.setBlockIncrement(Math.max(vp.width - 10, 1));
        vbar.setBlockIncrement(Math.max(vp.height - 10, 1));
      }
    finally
      {
        setting = false;
      }
  }

  public void reshape(int x, int y, int width, int height)
  {
    super.reshape(x, y, width, height);
    layoutBars();
  }

  public Dimension getPreferredSize()
  {
    return new Dimension(100, 100);
  }

  public Dimension getMinimumSize()
  {
    return new Dimension(getVScrollbarWidth() + 10,
                         getHScrollbarHeight() + 10);
  }

  public int getHScrollbarHeight()
  {
    return hbar.getPreferredSize().height;
  }

  public int getVScrollbarWidth()
  {
    return vbar.getPreferredSize().width;
  }

  public void setScrollPosition(int x, int y)
  {
    scrollX = x;
    scrollY = y;
    layoutBars();
    // ScrollPane.layout() moves the child back to (0, 0) after this call,
    // so move it later.
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          if (sp.getComponentCount() == 0)
            return;
          Component child = sp.getComponent(0);
          if (child.getX() != -scrollX || child.getY() != -scrollY)
            {
              child.setLocation(-scrollX, -scrollY);
              sp.repaint();
            }
        }
      });
  }

  public void childResized(int width, int height)
  {
    childW = width;
    childH = height;
    layoutBars();
  }

  public void setUnitIncrement(Adjustable item, int inc)
  {
    if (inc <= 1)
      return;
    if (item.getOrientation() == Adjustable.HORIZONTAL)
      hbar.setUnitIncrement(inc);
    else
      vbar.setUnitIncrement(inc);
  }

  public void setValue(Adjustable item, int value)
  {
    if (item.getOrientation() == Adjustable.HORIZONTAL)
      {
        if (value != scrollX)
          setScrollPosition(value, scrollY);
      }
    else if (value != scrollY)
      setScrollPosition(scrollX, value);
  }

  /**
   * Paints the child clipped to the viewport, then the scroll bars.
   */
  private static void paintBar(Graphics g, JScrollBar bar)
  {
    if (! bar.isVisible())
      return;
    Graphics cg = g.create(bar.getX(), bar.getY(), bar.getWidth(),
                           bar.getHeight());
    try
      {
        bar.paint(cg);
      }
    finally
      {
        cg.dispose();
      }
  }

  protected void peerPaint(Graphics g, boolean update)
  {
    Dimension vp = sp.getViewportSize();
    Graphics g2 = g.create();
    try
      {
        g2.clipRect(0, 0, vp.width, vp.height);
        peerPaintChildren(g2);
      }
    finally
      {
        g2.dispose();
      }
    int w = sp.getWidth();
    int h = sp.getHeight();
    if (vp.width < w && vp.height < h)
      {
        Color bg = sp.getBackground();
        g.setColor(bg != null ? bg : Color.lightGray);
        g.fillRect(vp.width, vp.height, w - vp.width, h - vp.height);
      }
    paintBar(g, vbar);
    paintBar(g, hbar);
  }

  /** True while a mouse button that was pressed on a scroll bar is held. */
  private boolean barGrab;

  protected void handleMouseEvent(MouseEvent e)
  {
    if (e.getID() == MouseEvent.MOUSE_PRESSED)
      barGrab = ! inViewport(e.getX(), e.getY());
    boolean bars;
    switch (e.getID())
      {
      case MouseEvent.MOUSE_RELEASED:
      case MouseEvent.MOUSE_CLICKED:
      case MouseEvent.MOUSE_PRESSED:
        bars = barGrab;
        break;
      default:
        bars = ! inViewport(e.getX(), e.getY());
      }
    if (bars)
      wrapper.handleMouseEvent(e);
    else
      super.handleMouseEvent(e);
  }

  protected void handleMouseMotionEvent(MouseEvent e)
  {
    if (e.getID() == MouseEvent.MOUSE_DRAGGED ? barGrab
        : ! inViewport(e.getX(), e.getY()))
      wrapper.handleMouseMotionEvent(e);
    else
      super.handleMouseMotionEvent(e);
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
