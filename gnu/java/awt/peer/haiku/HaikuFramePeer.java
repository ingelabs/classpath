/* HaikuFramePeer.java -- part of the Haiku AWT peer
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

import java.awt.Frame;
import java.awt.Image;
import java.awt.MenuBar;
import java.awt.Rectangle;
import java.awt.event.WindowEvent;
import java.awt.peer.FramePeer;

/**
 * A Frame backed by a titled BWindow.
 */
public class HaikuFramePeer
  extends HaikuWindowPeer
  implements FramePeer
{

  HaikuFramePeer(Frame f)
  {
    super(f);
    MenuBar mb = f.getMenuBar();
    if (mb != null)
      setMenuBar(mb);
    if (f.getMaximizedBounds() != null)
      setMaximizedBounds(f.getMaximizedBounds());
  }

  public void setIconImage(Image image)
  {
    // Haiku windows have no per-window icon.
  }

  /**
   * Puts a native BMenuBar at the top of the window. The menu bar is part
   * of the insets, as in other toolkits; the outer bounds stay the same.
   */
  public void setMenuBar(MenuBar mb)
  {
    long bar = 0;
    if (mb != null && mb.getPeer() instanceof HaikuMenuBarPeer)
      bar = ((HaikuMenuBarPeer) mb.getPeer()).bar;
    HaikuNative.setMenuBar(handle, bar);
    insetsChanged();
  }

  public void setTitle(String title)
  {
    super.setTitle(title);
  }

  /** The AWT state of the native window. */
  public int getState()
  {
    boolean[] st = new boolean[2];
    HaikuNative.getFrameState(handle, st);
    return toAWTState(st[0], st[1]);
  }

  private static int toAWTState(boolean minimized, boolean zoomed)
  {
    int state = Frame.NORMAL;
    if (minimized)
      state |= Frame.ICONIFIED;
    if (zoomed)
      state |= Frame.MAXIMIZED_BOTH;
    return state;
  }

  /**
   * Minimizes, restores or zooms the window. Haiku has one zoomed state,
   * so MAXIMIZED_HORIZ and MAXIMIZED_VERT zoom the window too.
   */
  public void setState(int state)
  {
    // The native window reports the change with EV_STATE, which posts
    // the window events.
    HaikuNative.setFrameState(handle, (state & Frame.ICONIFIED) != 0,
                              (state & Frame.MAXIMIZED_BOTH) != 0);
  }

  /** The state last reported to AWT. */
  private int lastState = Frame.NORMAL;

  /**
   * Called by the event pump when the window was minimized, restored or
   * zoomed. Posts the matching window events.
   */
  void nativeStateChanged(boolean minimized, boolean zoomed)
  {
    int state = toAWTState(minimized, zoomed);
    int old;
    synchronized (this)
      {
        old = lastState;
        if (state == old)
          return;
        lastState = state;
      }
    Frame f = (Frame) getWindow();
    java.awt.EventQueue q = java.awt.Toolkit.getDefaultToolkit()
                            .getSystemEventQueue();
    if ((state & Frame.ICONIFIED) != (old & Frame.ICONIFIED))
      q.postEvent(new WindowEvent(f, (state & Frame.ICONIFIED) != 0
                                     ? WindowEvent.WINDOW_ICONIFIED
                                     : WindowEvent.WINDOW_DEICONIFIED));
    q.postEvent(new WindowEvent(f, WindowEvent.WINDOW_STATE_CHANGED, old,
                                state));
  }

  public void setMaximizedBounds(Rectangle r)
  {
    if (r == null)
      HaikuNative.setZoomLimits(handle, 0, 0);
    else
      HaikuNative.setZoomLimits(handle, r.width, r.height);
  }

  public boolean isRestackSupported()
  {
    return false;
  }

  public void setBoundsPrivate(int x, int y, int width, int height)
  {
    reshape(x, y, width, height);
  }

  public Rectangle getBoundsPrivate()
  {
    return getOuterBounds();
  }
}
