/* HaikuInputMethods.java -- Haiku input methods for AWT
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

import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.InputMethodEvent;
import java.awt.event.KeyEvent;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.im.InputMethodHighlight;
import java.awt.im.InputMethodRequests;
import java.text.AttributedString;

import javax.swing.text.JTextComponent;

/**
 * Connects Haiku input methods (B_INPUT_METHOD_EVENT) to AWT.
 *
 * Components with InputMethodRequests get InputMethodEvents with the
 * composed text inline. Other components (all of GNU Classpath's Swing
 * text components) get the confirmed text as KEY_TYPED events, and the
 * composed text is shown in a small window below the caret.
 */
final class HaikuInputMethods
{

  private HaikuInputMethods()
  {
  }

  private static void post(java.awt.AWTEvent e)
  {
    Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(e);
  }

  private static Component focusOwner(Window w)
  {
    Component c = KeyboardFocusManager.getCurrentKeyboardFocusManager()
                  .getFocusOwner();
    return c != null ? c : w;
  }

  /**
   * The caret of a component in screen coordinates. For text components
   * this is the caret rectangle; else the bottom left corner.
   */
  static Rectangle caret(Component c)
  {
    JTextComponent text = null;
    if (c instanceof JTextComponent)
      text = (JTextComponent) c;
    else if (c.getPeer() instanceof HaikuTextFieldPeer)
      text = ((HaikuTextFieldPeer) c.getPeer()).textComponent();
    Point origin;
    try
      {
        origin = c.getLocationOnScreen();
      }
    catch (Exception e)
      {
        return new Rectangle(0, 0, 1, 16);
      }
    if (text != null)
      {
        try
          {
            Rectangle r = text.modelToView(text.getCaretPosition());
            if (r != null)
              {
                // An AWT text field draws its Swing field at its own origin.
                return new Rectangle(origin.x + r.x, origin.y + r.y,
                                     Math.max(r.width, 1), r.height);
              }
          }
        catch (Exception e)
          {
            // Use the component corner.
          }
      }
    int h = 16;
    if (c.getFont() != null)
      {
        FontMetrics fm = c.getFontMetrics(c.getFont());
        if (fm != null)
          h = fm.getHeight();
      }
    return new Rectangle(origin.x + 2, origin.y + Math.max(0, c.getHeight() - h),
                         1, h);
  }

  /** Called by the event pump for EV_IM. */
  static void event(HaikuWindowPeer peer, Window w, int opcode,
                    boolean confirmed, int selStart, int selEnd)
  {
    Component c = focusOwner(w);
    InputMethodRequests req = c.getInputMethodRequests();
    switch (opcode)
      {
      case HaikuNative.B_INPUT_METHOD_CHANGED:
        changed(peer, w, c, req, HaikuNative.imText(peer.id), confirmed,
                selStart, selEnd);
        break;
      case HaikuNative.B_INPUT_METHOD_LOCATION_REQUEST:
        locationRequest(peer, c, req, HaikuNative.imText(peer.id));
        break;
      case HaikuNative.B_INPUT_METHOD_STOPPED:
        HaikuNative.imShowComposition(null, 0, 0, 0, 0);
        if (req != null)
          post(new InputMethodEvent(c, InputMethodEvent.INPUT_METHOD_TEXT_CHANGED,
                                    new AttributedString("").getIterator(),
                                    0, null, null));
        break;
      default:
        break;
      }
  }

  private static void changed(HaikuWindowPeer peer, Window w, Component c,
                              InputMethodRequests req, String text,
                              boolean confirmed, int selStart, int selEnd)
  {
    if (confirmed)
      {
        HaikuNative.imShowComposition(null, 0, 0, 0, 0);
        if (req != null)
          {
            post(new InputMethodEvent(c, InputMethodEvent.INPUT_METHOD_TEXT_CHANGED,
                                      new AttributedString(text).getIterator(),
                                      text.length(), null, null));
            return;
          }
        long when = System.currentTimeMillis();
        for (int i = 0; i < text.length(); i++)
          post(new KeyEvent(w, KeyEvent.KEY_TYPED, when, 0,
                            KeyEvent.VK_UNDEFINED, text.charAt(i)));
        return;
      }
    if (req != null)
      {
        AttributedString as = new AttributedString(text);
        if (text.length() > 0)
          {
            as.addAttribute(TextAttribute.INPUT_METHOD_HIGHLIGHT,
                            InputMethodHighlight.UNSELECTED_RAW_TEXT_HIGHLIGHT);
            if (selEnd > selStart)
              as.addAttribute(TextAttribute.INPUT_METHOD_HIGHLIGHT,
                              InputMethodHighlight.SELECTED_RAW_TEXT_HIGHLIGHT,
                              selStart, selEnd);
          }
        post(new InputMethodEvent(c, InputMethodEvent.INPUT_METHOD_TEXT_CHANGED,
                                  as.getIterator(), 0,
                                  TextHitInfo.leading(selEnd), null));
        return;
      }
    Rectangle r = caret(c);
    HaikuNative.imShowComposition(text, r.x, r.y + r.height + 2, selStart,
                                  selEnd);
  }

  /** Tells the input method where each composed character is. */
  private static void locationRequest(HaikuWindowPeer peer, Component c,
                                      InputMethodRequests req, String text)
  {
    int n = Math.max(1, text.length());
    int[] xs = new int[n];
    int[] ys = new int[n];
    float height;
    if (req != null)
      {
        Rectangle r = null;
        for (int i = 0; i < n; i++)
          {
            r = req.getTextLocation(TextHitInfo.leading(i));
            xs[i] = r.x;
            ys[i] = r.y;
          }
        height = r != null ? r.height : 16;
      }
    else
      {
        // The composed text is in the composition window below the caret.
        Rectangle r = caret(c);
        FontMetrics fm = c.getFont() != null
                         ? c.getFontMetrics(c.getFont()) : null;
        int x = r.x;
        for (int i = 0; i < n; i++)
          {
            xs[i] = x + 4;
            ys[i] = r.y + r.height + 2;
            x += fm != null && i < text.length() ? fm.charWidth(text.charAt(i))
                                                 : 8;
          }
        height = r.height;
      }
    HaikuNative.imLocationReply(peer.handle, xs, ys, height);
  }
}
