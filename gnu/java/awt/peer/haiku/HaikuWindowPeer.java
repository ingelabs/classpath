/* HaikuWindowPeer.java -- part of the Haiku AWT peer
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

import gnu.java.awt.peer.ClasspathFontPeer;
import gnu.java.awt.peer.swing.SwingWindowPeer;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.PaintEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.VolatileImage;

/**
 * A heavyweight window backed by a native BWindow.
 *
 * The window content is rendered in Java into {@link #backing}, an
 * INT_RGB image the size of the native client area. Dirty areas are
 * copied to the native window when a {@link HaikuGraphics2D} is disposed.
 *
 * AWT window bounds include the decorations (insets); the native window
 * frame is the client area only. AWT coordinates (0, 0) are the outer
 * corner, so window graphics are translated by (-left, -top).
 */
public class HaikuWindowPeer
  extends SwingWindowPeer
  implements java.awt.dnd.peer.DropTargetPeer
{

  private static int nextId = 1;

  /** The background of windows that have no background colour. */
  static final Color DEFAULT_BACKGROUND = new Color(0xDDDDDD);

  final int id;

  final long handle;

  /** Decorator insets reported by the app_server. */
  private Insets insets;

  /** Client area in screen coordinates, as last set or reported. */
  private int clientX, clientY, clientW, clientH;

  /** The backing store, the size of the client area. */
  private BufferedImage backing;

  private boolean disposed;

  HaikuWindowPeer(Window window)
  {
    super(window);
    // Like other toolkits, give windows default colours, so that
    // components that inherit them never draw with a null colour.
    if (window.getForeground() == null)
      window.setForeground(Color.black);
    if (window.getBackground() == null)
      window.setBackground(DEFAULT_BACKGROUND);
    synchronized (HaikuWindowPeer.class)
      {
        id = nextId++;
      }
    int kind;
    String title = null;
    if (window instanceof Frame)
      {
        Frame f = (Frame) window;
        kind = f.isUndecorated() ? HaikuNative.KIND_WINDOW
                                 : HaikuNative.KIND_FRAME;
        title = f.getTitle();
      }
    else if (window instanceof Dialog)
      {
        Dialog d = (Dialog) window;
        kind = d.isUndecorated() ? HaikuNative.KIND_WINDOW
                                 : HaikuNative.KIND_DIALOG;
        title = d.getTitle();
      }
    else
      kind = HaikuNative.KIND_WINDOW;

    int x = window.getX();
    int y = window.getY();
    int w = Math.max(window.getWidth(), 1);
    int h = Math.max(window.getHeight(), 1);
    handle = HaikuNative.createWindow(id, kind, title, x, y, w, h);

    int[] in = new int[4];
    HaikuNative.getInsets(handle, in);
    insets = new Insets(in[1], in[0], in[3], in[2]);
    HaikuEventPump.get().register(this);
    setClientFromAWT(x, y, w, h);
    if (window instanceof Frame)
      HaikuNative.setResizable(handle, ((Frame) window).isResizable());
    else if (window instanceof Dialog)
      HaikuNative.setResizable(handle, ((Dialog) window).isResizable());
    if (window.isAlwaysOnTop())
      updateFeel();
  }

  /**
   * Reads the insets again (for example after a menu bar was added) and
   * lays out the window for the new client area.
   */
  void insetsChanged()
  {
    int[] in = new int[4];
    HaikuNative.getInsets(handle, in);
    Insets i = new Insets(in[1], in[0], in[3], in[2]);
    synchronized (this)
      {
        if (i.equals(insets))
          return;
        insets = i;
      }
    final Window w = getWindow();
    setClientFromAWT(w.getX(), w.getY(), w.getWidth(), w.getHeight());
    java.awt.EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          w.invalidate();
          w.validate();
          w.repaint();
        }
      });
  }

  Window getWindow()
  {
    return (Window) awtComponent;
  }

  /**
   * Applies AWT outer bounds to the native window and backing store.
   */
  private synchronized void setClientFromAWT(int x, int y, int w, int h)
  {
    Insets i = insets;
    clientX = x + i.left;
    clientY = y + i.top;
    clientW = Math.max(w - i.left - i.right, 1);
    clientH = Math.max(h - i.top - i.bottom, 1);
    HaikuNative.setBounds(handle, clientX, clientY, clientW, clientH);
    ensureBacking(clientW, clientH);
  }

  /**
   * Makes sure the backing store has the given size. Old content is kept.
   *
   * @return true if the backing store was replaced
   */
  private synchronized boolean ensureBacking(int w, int h)
  {
    if (backing != null && backing.getWidth() == w
        && backing.getHeight() == h)
      return false;
    BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    Color bg = awtComponent.getBackground();
    int rgb = (bg != null ? bg : DEFAULT_BACKGROUND).getRGB();
    int[] p = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    java.util.Arrays.fill(p, rgb);
    if (backing != null)
      {
        int[] old = ((DataBufferInt) backing.getRaster().getDataBuffer())
                    .getData();
        int ow = backing.getWidth();
        int cw = Math.min(ow, w);
        int ch = Math.min(backing.getHeight(), h);
        for (int row = 0; row < ch; row++)
          System.arraycopy(old, row * ow, p, row * w, cw);
      }
    backing = img;
    return true;
  }

  /**
   * Called by the event pump when the user resized the native window.
   *
   * @return true if the AWT bounds changed
   */
  synchronized boolean nativeResized(int w, int h)
  {
    boolean changed = w != clientW || h != clientH;
    clientW = w;
    clientH = h;
    if (ensureBacking(w, h))
      flushBacking(backing, 0, 0, w, h);
    return changed;
  }

  synchronized boolean nativeMoved(int x, int y)
  {
    boolean changed = x != clientX || y != clientY;
    clientX = x;
    clientY = y;
    return changed;
  }

  /** AWT outer bounds derived from the native client area. */
  synchronized Rectangle getOuterBounds()
  {
    Insets i = insets;
    return new Rectangle(clientX - i.left, clientY - i.top,
                         clientW + i.left + i.right,
                         clientH + i.top + i.bottom);
  }

  /**
   * Copies part of the backing store to the native window. Ignored if
   * img is no longer the backing store (the window was resized).
   */
  synchronized void flushBacking(BufferedImage img, int x, int y, int w,
                                 int h)
  {
    if (disposed || img != backing)
      return;
    int[] data = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
    HaikuNative.blit(handle, data, img.getWidth(), img.getHeight(),
                     x, y, w, h);
  }

  public Graphics getGraphics()
  {
    BufferedImage img;
    synchronized (this)
      {
        img = backing;
      }
    HaikuGraphics2D g = new HaikuGraphics2D(img, this);
    g.translate(-insets.left, -insets.top);
    g.setColor(awtComponent.getForeground());
    Color bg = awtComponent.getBackground();
    g.setBackground(bg != null ? bg : DEFAULT_BACKGROUND);
    g.setFont(awtComponent.getFont());
    return g;
  }

  /**
   * The window itself has no Swing component; clear the exposed area to
   * the background colour before AWT paints on top of it.
   */
  protected void peerPaintComponent(Graphics g)
  {
    Rectangle clip = g.getClipBounds();
    if (clip == null)
      clip = new Rectangle(0, 0, awtComponent.getWidth(),
                           awtComponent.getHeight());
    g.clearRect(clip.x, clip.y, clip.width, clip.height);
  }

  public Point getLocationOnScreen()
  {
    Rectangle r = getOuterBounds();
    return new Point(r.x, r.y);
  }

  public Image createImage(int w, int h)
  {
    return new BufferedImage(Math.max(w, 1), Math.max(h, 1),
                             BufferedImage.TYPE_INT_RGB);
  }

  public VolatileImage createVolatileImage(int w, int h)
  {
    return new HaikuVolatileImage(w, h);
  }

  public GraphicsConfiguration getGraphicsConfiguration()
  {
    return GraphicsEnvironment.getLocalGraphicsEnvironment()
           .getDefaultScreenDevice().getDefaultConfiguration();
  }

  public FontMetrics getFontMetrics(Font font)
  {
    ClasspathFontPeer fontPeer = (ClasspathFontPeer) font.getPeer();
    return fontPeer.getFontMetrics(font);
  }

  public void show()
  {
    HaikuNative.setVisible(handle, true);
    Window w = getWindow();
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
      new PaintEvent(w, PaintEvent.PAINT,
                     new Rectangle(0, 0, w.getWidth(), w.getHeight())));
  }

  public void hide()
  {
    HaikuNative.setVisible(handle, false);
  }

  public void reshape(int x, int y, int width, int height)
  {
    setClientFromAWT(x, y, width, height);
  }

  public void setBounds(int x, int y, int width, int height)
  {
    reshape(x, y, width, height);
  }

  public void setBounds(int x, int y, int width, int height, int z)
  {
    reshape(x, y, width, height);
  }

  public Rectangle getBounds()
  {
    return getOuterBounds();
  }

  public Insets insets()
  {
    return (Insets) insets.clone();
  }

  public Insets getInsets()
  {
    return insets();
  }

  public void setBackground(Color c)
  {
    // Nothing to do; the background is painted from Java.
  }

  public void setForeground(Color c)
  {
    // Nothing to do.
  }

  public void setFont(Font f)
  {
    peerFont = f;
  }

  /** The cursor type and custom cursor shown in the native window. */
  private int shownCursorType = Cursor.DEFAULT_CURSOR;
  private long shownCursorHandle;

  /**
   * Shows a cursor in the native window. The event pump calls this when
   * the mouse moves to a component with another cursor.
   */
  void showCursor(Cursor c)
  {
    int type = c.getType();
    long custom = c instanceof HaikuCursor ? ((HaikuCursor) c).handle : 0;
    synchronized (this)
      {
        if (disposed
            || (type == shownCursorType && custom == shownCursorHandle))
          return;
        shownCursorType = type;
        shownCursorHandle = custom;
      }
    HaikuNative.setCursor(handle, type, custom);
  }

  /**
   * Called by Component.setCursor() for this window and its lightweight
   * children. The cursor of the component under the mouse is shown.
   */
  public void setCursor(Cursor cursor)
  {
    HaikuEventPump.get().refreshCursor();
  }

  public void toBack()
  {
    HaikuNative.toBack(handle);
  }

  public void toFront()
  {
    HaikuNative.toFront(handle);
  }

  /** True while this is a shown modal dialog. */
  private boolean modalShown;

  void setModalShown(boolean shown)
  {
    modalShown = shown;
    updateFeel();
  }

  /**
   * A modal dialog has the modal app feel; an always-on-top window floats
   * above all windows of all applications.
   */
  void updateFeel()
  {
    int feel = HaikuNative.B_NORMAL_WINDOW_FEEL;
    if (modalShown)
      feel = HaikuNative.B_MODAL_APP_WINDOW_FEEL;
    else if (getWindow().isAlwaysOnTop())
      feel = HaikuNative.B_FLOATING_ALL_WINDOW_FEEL;
    HaikuNative.setFeel(handle, feel);
  }

  public void updateAlwaysOnTop()
  {
    updateFeel();
  }

  public void setAlwaysOnTop(boolean alwaysOnTop)
  {
    updateFeel();
  }

  // Drop targets are found by the event pump; nothing to register.

  public void addDropTarget(java.awt.dnd.DropTarget target)
  {
  }

  public void removeDropTarget(java.awt.dnd.DropTarget target)
  {
  }

  public boolean requestWindowFocus()
  {
    HaikuNative.toFront(handle);
    return true;
  }

  void setTitle(String title)
  {
    HaikuNative.setTitle(handle, title);
  }

  public void setResizable(boolean resizable)
  {
    HaikuNative.setResizable(handle, resizable);
  }

  public void dispose()
  {
    synchronized (this)
      {
        if (disposed)
          return;
        disposed = true;
      }
    HaikuEventPump.get().unregister(this);
    HaikuNative.dispose(handle);
    super.dispose();
  }
}
