/* HaikuCursor.java -- a custom cursor for the Haiku AWT peer
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

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * A custom cursor made with Toolkit.createCustomCursor(). It holds a
 * native BCursor.
 */
final class HaikuCursor
  extends Cursor
{

  /** The native BCursor, or 0 if it could not be made. */
  final long handle;

  HaikuCursor(Image image, Point hotSpot, String name)
  {
    super(name);
    int w = image.getWidth(null);
    int h = image.getHeight(null);
    long hdl = 0;
    if (w > 0 && h > 0)
      {
        BufferedImage argb = new BufferedImage(w, h,
                                               BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = argb.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        int[] pixels = argb.getRGB(0, 0, w, h, null, 0, w);
        int hx = Math.max(0, Math.min(w - 1, hotSpot.x));
        int hy = Math.max(0, Math.min(h - 1, hotSpot.y));
        hdl = HaikuNative.cursorCreate(pixels, w, h, hx, hy);
      }
    handle = hdl;
  }
}
