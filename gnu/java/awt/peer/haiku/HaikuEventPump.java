/* HaikuEventPump.java -- part of the Haiku AWT peer
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

import gnu.java.awt.ComponentReshapeEvent;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.ScrollPane;
import java.awt.TextComponent;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.PaintEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Polls native events from libhaikupeer and posts the matching AWT events
 * to the system event queue.
 */
final class HaikuEventPump
  implements Runnable
{

  // Haiku modifier bits (InterfaceDefs.h).
  private static final int B_SHIFT_KEY = 0x00000001;
  private static final int B_COMMAND_KEY = 0x00000002;
  private static final int B_CONTROL_KEY = 0x00000004;
  private static final int B_OPTION_KEY = 0x00000040;

  // Haiku mouse buttons.
  private static final int B_PRIMARY_MOUSE_BUTTON = 0x01;
  private static final int B_SECONDARY_MOUSE_BUTTON = 0x02;
  private static final int B_TERTIARY_MOUSE_BUTTON = 0x04;

  // Haiku special raw_char values.
  private static final int B_BACKSPACE = 0x08;
  private static final int B_RETURN = 0x0a;
  private static final int B_TAB = 0x09;
  private static final int B_ESCAPE = 0x1b;
  private static final int B_LEFT_ARROW = 0x1c;
  private static final int B_RIGHT_ARROW = 0x1d;
  private static final int B_UP_ARROW = 0x1e;
  private static final int B_DOWN_ARROW = 0x1f;
  private static final int B_INSERT = 0x05;
  private static final int B_DELETE = 0x7f;
  private static final int B_HOME = 0x01;
  private static final int B_END = 0x04;
  private static final int B_PAGE_UP = 0x0b;
  private static final int B_PAGE_DOWN = 0x0c;
  private static final int B_FUNCTION_KEY = 0x10;

  // Haiku "key" codes for function keys.
  private static final int B_F1_KEY = 0x02;
  private static final int B_F12_KEY = 0x0d;

  /** Trace native events to stderr. */
  private static final boolean DEBUG =
    Boolean.getBoolean("gnu.java.awt.peer.haiku.debug");

  private static HaikuEventPump instance;

  private final HashMap<Integer,HaikuWindowPeer> windows =
    new HashMap<Integer,HaikuWindowPeer>();

  /** Target and position of the last mouse press, for MOUSE_CLICKED. */
  private Component pressTarget;
  private int pressButton;
  private boolean dragged;

  /** The click count of the last press. */
  private int pressClicks = 1;

  /** Component under the mouse, for MOUSE_ENTERED / MOUSE_EXITED. */
  private Component hoverTarget;

  /** Window and window coordinates of the last mouse position. */
  private HaikuWindowPeer cursorPeer;
  private int cursorX, cursorY;

  static synchronized HaikuEventPump get()
  {
    if (instance == null)
      {
        instance = new HaikuEventPump();
        Thread t = new Thread(instance, "Haiku AWT event pump");
        t.setDaemon(true);
        t.start();
      }
    return instance;
  }

  private HaikuEventPump()
  {
  }

  synchronized void register(HaikuWindowPeer peer)
  {
    windows.put(new Integer(peer.id), peer);
  }

  synchronized void unregister(HaikuWindowPeer peer)
  {
    windows.remove(new Integer(peer.id));
  }

  private synchronized HaikuWindowPeer lookup(int id)
  {
    return windows.get(new Integer(id));
  }

  /** Shown modal dialogs, the newest last. */
  private final ArrayList<HaikuDialogPeer> modals =
    new ArrayList<HaikuDialogPeer>();

  synchronized void modalShown(HaikuDialogPeer p)
  {
    modals.remove(p);
    modals.add(p);
  }

  synchronized void modalHidden(HaikuDialogPeer p)
  {
    modals.remove(p);
  }

  /**
   * The modal dialog that blocks input to w, or null. A modal dialog
   * blocks every window except itself and the windows it owns.
   */
  private HaikuDialogPeer blocker(Window w)
  {
    HaikuDialogPeer top;
    synchronized (this)
      {
        if (modals.isEmpty())
          return null;
        top = modals.get(modals.size() - 1);
      }
    Window m = top.getWindow();
    for (Window x = w; x != null; x = x.getOwner())
      if (x == m)
        return null;
    return top;
  }

  /** Event types that are user input to a window. */
  private static boolean isInput(int type)
  {
    switch (type)
      {
      case HaikuNative.EV_CLOSE:
      case HaikuNative.EV_MOUSE_DOWN:
      case HaikuNative.EV_MOUSE_UP:
      case HaikuNative.EV_MOUSE_MOVED:
      case HaikuNative.EV_MOUSE_ENTER:
      case HaikuNative.EV_MOUSE_EXIT:
      case HaikuNative.EV_KEY_DOWN:
      case HaikuNative.EV_KEY_UP:
      case HaikuNative.EV_WHEEL:
      case HaikuNative.EV_EDIT:
      case HaikuNative.EV_DROP:
      case HaikuNative.EV_IM:
        return true;
      default:
        return false;
      }
  }

  public void run()
  {
    int[] ev = new int[HaikuNative.EV_INTS];
    while (true)
      {
        try
          {
            if (HaikuNative.nextEvent(ev, 100) > 0)
              handle(ev);
          }
        catch (ThreadDeath death)
          {
            return;
          }
        catch (Throwable t)
          {
            System.err.println("Exception in Haiku AWT event pump:");
            t.printStackTrace();
          }
      }
  }

  private static void post(AWTEvent e)
  {
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
  }

  private void handle(int[] ev)
  {
    int type = ev[0];
    if (DEBUG)
      {
        StringBuilder b = new StringBuilder("haiku event:");
        for (int i = 0; i < ev.length; i++)
          b.append(' ').append(ev[i]);
        System.err.println(b);
      }
    if (type == HaikuNative.EV_APP_QUIT)
      {
        ArrayList<HaikuWindowPeer> all;
        synchronized (this)
          {
            all = new ArrayList<HaikuWindowPeer>(windows.values());
          }
        for (HaikuWindowPeer p : all)
          post(new WindowEvent(p.getWindow(), WindowEvent.WINDOW_CLOSING));
        return;
      }
    if (type == HaikuNative.EV_MENU)
      {
        HaikuMenuItemPeer item = HaikuMenuItemPeer.find(ev[2]);
        if (item != null)
          item.activated(mapModifiers(ev[3]));
        return;
      }
    if (type == HaikuNative.EV_CHOICE)
      {
        HaikuChoicePeer choice = HaikuChoicePeer.find(ev[1]);
        if (choice != null)
          choice.selected(ev[2]);
        return;
      }

    HaikuWindowPeer peer = lookup(ev[1]);
    if (peer == null)
      return;
    Window w = peer.getWindow();
    if (w == null)
      return;
    Insets in = peer.insets();
    long when = System.currentTimeMillis();

    if (isInput(type))
      {
        HaikuDialogPeer b = blocker(w);
        if (b != null)
          {
            // A click on a blocked window brings the modal dialog forward.
            if (type == HaikuNative.EV_MOUSE_DOWN
                || type == HaikuNative.EV_CLOSE)
              b.toFront();
            return;
          }
      }

    switch (type)
      {
      case HaikuNative.EV_FILE_PANEL:
        if (peer instanceof HaikuFileDialogPeer)
          ((HaikuFileDialogPeer) peer).panelClosed(ev[2] != 0);
        break;

      case HaikuNative.EV_FILTER:
        if (peer instanceof HaikuFileDialogPeer)
          ((HaikuFileDialogPeer) peer).filterRequested();
        break;

      case HaikuNative.EV_STATE:
        if (peer instanceof HaikuFramePeer)
          ((HaikuFramePeer) peer).nativeStateChanged(ev[2] != 0, ev[3] != 0);
        break;

      case HaikuNative.EV_EDIT:
        edit(w, ev[2], when);
        break;

      case HaikuNative.EV_DRAG:
        if (blocker(w) == null || ev[4] == HaikuNative.DRAG_EXIT)
          HaikuDragDrop.drag(w, ev[2] + in.left, ev[3] + in.top, ev[4],
                             ev[5]);
        break;

      case HaikuNative.EV_DROP:
        HaikuDragDrop.drop(w, ev[2] + in.left, ev[3] + in.top, ev[5]);
        break;

      case HaikuNative.EV_IM:
        HaikuInputMethods.event(peer, w, ev[2], ev[3] != 0, ev[4], ev[5]);
        break;

      case HaikuNative.EV_CLOSE:
        post(new WindowEvent(w, WindowEvent.WINDOW_CLOSING));
        break;

      case HaikuNative.EV_RESIZED:
        if (peer.nativeResized(ev[2], ev[3]))
          {
            // Lay out again, then repaint everything at the new size.
            reshape(w, peer.getOuterBounds(), true);
            post(new ComponentEvent(w, ComponentEvent.COMPONENT_RESIZED));
          }
        else
          post(new PaintEvent(w, PaintEvent.PAINT,
                              new Rectangle(0, 0, w.getWidth(),
                                            w.getHeight())));
        break;

      case HaikuNative.EV_MOVED:
        if (peer.nativeMoved(ev[2], ev[3]))
          {
            reshape(w, peer.getOuterBounds(), false);
            post(new ComponentEvent(w, ComponentEvent.COMPONENT_MOVED));
          }
        break;

      case HaikuNative.EV_ACTIVATED:
        if (ev[2] != 0)
          {
            post(new WindowEvent(w, WindowEvent.WINDOW_ACTIVATED));
            post(new WindowEvent(w, WindowEvent.WINDOW_GAINED_FOCUS));
          }
        else
          {
            post(new WindowEvent(w, WindowEvent.WINDOW_LOST_FOCUS));
            post(new WindowEvent(w, WindowEvent.WINDOW_DEACTIVATED));
          }
        break;

      case HaikuNative.EV_MOUSE_DOWN:
        mouseDown(w, ev[2] + in.left, ev[3] + in.top, ev[4], ev[5], ev[6],
                  when);
        break;

      case HaikuNative.EV_MOUSE_UP:
        mouseUp(w, ev[2] + in.left, ev[3] + in.top, ev[4], ev[5], when);
        HaikuDragDrop.mouseReleased();
        break;

      case HaikuNative.EV_MOUSE_MOVED:
        mouseMoved(w, ev[2] + in.left, ev[3] + in.top, ev[4], ev[5], when);
        synchronized (this)
          {
            cursorPeer = peer;
            cursorX = ev[2] + in.left;
            cursorY = ev[3] + in.top;
          }
        if (ev[4] == 0)
          updateCursor(peer, w, ev[2] + in.left, ev[3] + in.top);
        break;

      case HaikuNative.EV_MOUSE_ENTER:
        break;

      case HaikuNative.EV_MOUSE_EXIT:
        if (hoverTarget != null)
          {
            postMouse(hoverTarget, w, MouseEvent.MOUSE_EXITED, when,
                      mapModifiers(ev[5]), ev[2] + in.left, ev[3] + in.top,
                      0, 0);
            hoverTarget = null;
          }
        break;

      case HaikuNative.EV_WHEEL:
        {
          int x = ev[2] + in.left;
          int y = ev[3] + in.top;
          Component target = findTarget(w, x, y);
          int[] p = translate(w, target, x, y);
          post(new MouseWheelEvent(target, MouseEvent.MOUSE_WHEEL, when,
                                   mapModifiers(ev[5]), p[0], p[1], 0, false,
                                   MouseWheelEvent.WHEEL_UNIT_SCROLL, 3,
                                   ev[4]));
        }
        break;

      case HaikuNative.EV_KEY_DOWN:
      case HaikuNative.EV_KEY_UP:
        key(w, type == HaikuNative.EV_KEY_DOWN, ev[2], ev[3], ev[4], ev[5],
            when);
        break;

      default:
        break;
      }
  }

  /**
   * Updates the AWT bounds of w after the user moved or resized the
   * native window, without calling back into the peer.
   */
  private static void reshape(final Window w, Rectangle r,
                              final boolean resized)
  {
    post(new ComponentReshapeEvent(w, r.x, r.y, r.width, r.height));
    if (! resized)
      return;
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          w.invalidate();
          w.validate();
          Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(
            new PaintEvent(w, PaintEvent.PAINT,
                           new Rectangle(0, 0, w.getWidth(), w.getHeight())));
        }
      });
  }

  /**
   * The Command-X/C/V/A shortcuts of a Haiku window arrive as edit
   * commands, not as keys. Post them as the Ctrl keys that Swing's text
   * components use for cut, copy, paste and select all.
   */
  private static void edit(Window w, int command, long when)
  {
    int vk;
    switch (command)
      {
      case HaikuNative.EDIT_CUT: vk = KeyEvent.VK_X; break;
      case HaikuNative.EDIT_COPY: vk = KeyEvent.VK_C; break;
      case HaikuNative.EDIT_PASTE: vk = KeyEvent.VK_V; break;
      case HaikuNative.EDIT_SELECT_ALL: vk = KeyEvent.VK_A; break;
      default: return;
      }
    int m = InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK;
    char c = (char) (vk - KeyEvent.VK_A + 1);
    post(new KeyEvent(w, KeyEvent.KEY_PRESSED, when, m, vk, c));
    post(new KeyEvent(w, KeyEvent.KEY_RELEASED, when, m, vk, c));
  }

  // Cursors.

  /**
   * Shows the cursor of the component at window point (x, y). AWT
   * components inherit the cursor of their parent; text components show
   * the text cursor unless a cursor was set.
   */
  private static void updateCursor(HaikuWindowPeer peer, Window w, int x,
                                   int y)
  {
    Component c = w.findComponentAt(x, y);
    if (c == null)
      c = w;
    Cursor cursor = c.getCursor();
    if (c instanceof TextComponent && ! c.isCursorSet()
        && cursor.getType() == Cursor.DEFAULT_CURSOR)
      cursor = Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
    peer.showCursor(cursor);
  }

  /**
   * Shows the cursor again at the last mouse position. Called when a
   * component cursor changed.
   */
  void refreshCursor()
  {
    HaikuWindowPeer p;
    int x, y;
    synchronized (this)
      {
        p = cursorPeer;
        x = cursorX;
        y = cursorY;
      }
    if (p != null && p.getWindow() != null)
      updateCursor(p, p.getWindow(), x, y);
  }

  // Mouse handling.

  private void mouseDown(Window w, int x, int y, int buttons, int mods,
                         int clicks, long when)
  {
    int button = mapButton(buttons);
    Component target = findTarget(w, x, y);
    pressTarget = target;
    pressButton = button;
    pressClicks = clicks;
    dragged = false;
    postMouse(target, w, MouseEvent.MOUSE_PRESSED, when,
              mapModifiers(mods) | buttonMask(button), x, y, clicks, button);
  }

  private void mouseUp(Window w, int x, int y, int released, int mods,
                       long when)
  {
    int button = mapButton(released);
    if (button == MouseEvent.NOBUTTON)
      button = pressButton;
    Component target = pressTarget != null ? pressTarget
                                           : findTarget(w, x, y);
    int m = mapModifiers(mods) | buttonMask(button);
    postMouse(target, w, MouseEvent.MOUSE_RELEASED, when, m, x, y,
              pressClicks, button);
    if (! dragged && target == findTarget(w, x, y))
      postMouse(target, w, MouseEvent.MOUSE_CLICKED, when, m, x, y,
                pressClicks, button);
    pressTarget = null;
  }

  private void mouseMoved(Window w, int x, int y, int buttons, int mods,
                          long when)
  {
    int m = mapModifiers(mods);
    if (pressTarget != null && buttons != 0)
      {
        dragged = true;
        postMouse(pressTarget, w, MouseEvent.MOUSE_DRAGGED, when,
                  m | buttonMask(pressButton), x, y, 0, 0);
        return;
      }
    Component target = findTarget(w, x, y);
    if (target != hoverTarget)
      {
        if (hoverTarget != null)
          postMouse(hoverTarget, w, MouseEvent.MOUSE_EXITED, when, m, x, y,
                    0, 0);
        postMouse(target, w, MouseEvent.MOUSE_ENTERED, when, m, x, y, 0, 0);
        hoverTarget = target;
      }
    postMouse(target, w, MouseEvent.MOUSE_MOVED, when, m, x, y, 0, 0);
  }

  /** Posts a mouse event; x/y are window coordinates. */
  private static void postMouse(Component target, Window w, int id,
                                long when, int mods, int x, int y,
                                int clicks, int button)
  {
    int[] p = translate(w, target, x, y);
    post(new MouseEvent(target, id, when, mods, p[0], p[1], clicks,
                        button == MouseEvent.BUTTON3
                        && id == MouseEvent.MOUSE_PRESSED,
                        button));
  }

  /** Window coordinates to target coordinates. */
  private static int[] translate(Window w, Component target, int x, int y)
  {
    for (Component c = target; c != null && c != w; c = c.getParent())
      {
        x -= c.getX();
        y -= c.getY();
      }
    return new int[] { x, y };
  }

  /**
   * Finds the deepest visible heavyweight component at (x, y).
   * Lightweight components are handled by AWT's LightweightDispatcher.
   */
  private static Component findTarget(Component src, int x, int y)
  {
    // Children of a ScrollPane can extend under its scrollbars.
    if (src instanceof ScrollPane
        && src.getPeer() instanceof HaikuScrollPanePeer
        && ! ((HaikuScrollPanePeer) src.getPeer()).inViewport(x, y))
      return src;
    if (src instanceof Container)
      {
        Container cont = (Container) src;
        int n = cont.getComponentCount();
        for (int i = 0; i < n; i++)
          {
            Component child = cont.getComponent(i);
            if (child != null && child.isVisible()
                && child.contains(x - child.getX(), y - child.getY()))
              {
                Component deeper = findTarget(child, x - child.getX(),
                                              y - child.getY());
                if (deeper != null)
                  return deeper;
              }
          }
      }
    if (src instanceof Window)
      return src;
    return src.isLightweight() ? null : src;
  }

  private static int mapButton(int buttons)
  {
    if ((buttons & B_PRIMARY_MOUSE_BUTTON) != 0)
      return MouseEvent.BUTTON1;
    if ((buttons & B_SECONDARY_MOUSE_BUTTON) != 0)
      return MouseEvent.BUTTON3;
    if ((buttons & B_TERTIARY_MOUSE_BUTTON) != 0)
      return MouseEvent.BUTTON2;
    return MouseEvent.NOBUTTON;
  }

  private static int buttonMask(int button)
  {
    switch (button)
      {
      case MouseEvent.BUTTON1:
        return InputEvent.BUTTON1_DOWN_MASK | InputEvent.BUTTON1_MASK;
      case MouseEvent.BUTTON2:
        return InputEvent.BUTTON2_DOWN_MASK | InputEvent.BUTTON2_MASK;
      case MouseEvent.BUTTON3:
        return InputEvent.BUTTON3_DOWN_MASK | InputEvent.BUTTON3_MASK;
      default:
        return 0;
      }
  }

  /**
   * Maps Haiku modifiers to AWT modifiers. On a PC keyboard Haiku's
   * Command key is the Alt key and Option is the Windows key.
   */
  static int mapModifiers(int mods)
  {
    int m = 0;
    if ((mods & B_SHIFT_KEY) != 0)
      m |= InputEvent.SHIFT_DOWN_MASK | InputEvent.SHIFT_MASK;
    if ((mods & B_CONTROL_KEY) != 0)
      m |= InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK;
    if ((mods & B_COMMAND_KEY) != 0)
      m |= InputEvent.ALT_DOWN_MASK | InputEvent.ALT_MASK;
    if ((mods & B_OPTION_KEY) != 0)
      m |= InputEvent.META_DOWN_MASK | InputEvent.META_MASK;
    return m;
  }

  // Keyboard handling.

  private static void key(Window w, boolean down, int rawChar, int unicode,
                          int mods, int key, long when)
  {
    int vk = mapKeyCode(rawChar, key);
    boolean special = isSpecial(rawChar);
    int m = mapModifiers(mods);
    if (down)
      {
        post(new KeyEvent(w, KeyEvent.KEY_PRESSED, when, m, vk,
                          special ? KeyEvent.CHAR_UNDEFINED : (char) unicode));
        if (! special && unicode != 0 && unicode <= 0xFFFF)
          post(new KeyEvent(w, KeyEvent.KEY_TYPED, when, m,
                            KeyEvent.VK_UNDEFINED, (char) unicode));
      }
    else
      post(new KeyEvent(w, KeyEvent.KEY_RELEASED, when, m, vk,
                        special ? KeyEvent.CHAR_UNDEFINED : (char) unicode));
  }

  /** True for keys that produce no KEY_TYPED event. */
  private static boolean isSpecial(int rawChar)
  {
    switch (rawChar)
      {
      case B_LEFT_ARROW:
      case B_RIGHT_ARROW:
      case B_UP_ARROW:
      case B_DOWN_ARROW:
      case B_INSERT:
      case B_HOME:
      case B_END:
      case B_PAGE_UP:
      case B_PAGE_DOWN:
      case B_FUNCTION_KEY:
      case 0:
        return true;
      default:
        return false;
      }
  }

  private static int mapKeyCode(int rawChar, int key)
  {
    switch (rawChar)
      {
      case B_BACKSPACE: return KeyEvent.VK_BACK_SPACE;
      case B_RETURN: return KeyEvent.VK_ENTER;
      case B_TAB: return KeyEvent.VK_TAB;
      case B_ESCAPE: return KeyEvent.VK_ESCAPE;
      case B_LEFT_ARROW: return KeyEvent.VK_LEFT;
      case B_RIGHT_ARROW: return KeyEvent.VK_RIGHT;
      case B_UP_ARROW: return KeyEvent.VK_UP;
      case B_DOWN_ARROW: return KeyEvent.VK_DOWN;
      case B_INSERT: return KeyEvent.VK_INSERT;
      case B_DELETE: return KeyEvent.VK_DELETE;
      case B_HOME: return KeyEvent.VK_HOME;
      case B_END: return KeyEvent.VK_END;
      case B_PAGE_UP: return KeyEvent.VK_PAGE_UP;
      case B_PAGE_DOWN: return KeyEvent.VK_PAGE_DOWN;
      case ' ': return KeyEvent.VK_SPACE;
      case '`': return KeyEvent.VK_BACK_QUOTE;
      case '\'': return KeyEvent.VK_QUOTE;
      case B_FUNCTION_KEY:
        if (key >= B_F1_KEY && key <= B_F12_KEY)
          return KeyEvent.VK_F1 + (key - B_F1_KEY);
        return KeyEvent.VK_UNDEFINED;
      default:
        break;
      }
    if (rawChar >= 'a' && rawChar <= 'z')
      return rawChar - 'a' + KeyEvent.VK_A;
    if (rawChar >= 'A' && rawChar <= 'Z')
      return rawChar;
    if (rawChar >= '0' && rawChar <= '9')
      return rawChar;
    switch (rawChar)
      {
      case ',': case '-': case '.': case '/': case ';': case '=':
      case '[': case '\\': case ']':
        return rawChar; // AWT VK codes equal ASCII for these.
      default:
        return KeyEvent.VK_UNDEFINED;
      }
  }
}
