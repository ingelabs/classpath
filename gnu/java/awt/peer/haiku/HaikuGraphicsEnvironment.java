/* HaikuGraphicsEnvironment.java -- part of the Haiku AWT peer
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


import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.Locale;

/**
 * The Haiku graphics environment: one screen, Java-rendered images.
 */
public class HaikuGraphicsEnvironment
  extends GraphicsEnvironment
{

  private HaikuGraphicsDevice device;

  public HaikuGraphicsEnvironment()
  {
  }

  public GraphicsDevice[] getScreenDevices()
  {
    return new GraphicsDevice[] { getDefaultScreenDevice() };
  }

  public synchronized GraphicsDevice getDefaultScreenDevice()
  {
    if (device == null)
      device = new HaikuGraphicsDevice();
    return device;
  }

  public Graphics2D createGraphics(BufferedImage image)
  {
    return new HaikuGraphics2D(image, null);
  }

  public Font[] getAllFonts()
  {
    String[] names = getAvailableFontFamilyNames();
    Font[] fonts = new Font[names.length];
    for (int i = 0; i < names.length; i++)
      fonts[i] = new Font(names[i], Font.PLAIN, 1);
    return fonts;
  }

  public String[] getAvailableFontFamilyNames()
  {
    return getAvailableFontFamilyNames(Locale.getDefault());
  }

  public String[] getAvailableFontFamilyNames(Locale l)
  {
    // The Java logical fonts, then Haiku's font families.
    String[] logical = { "Dialog", "DialogInput", "Monospaced", "SansSerif",
                         "Serif" };
    String[] haiku = HaikuFontPeer.haikuFamilies();
    String[] all = new String[logical.length + haiku.length];
    System.arraycopy(logical, 0, all, 0, logical.length);
    System.arraycopy(haiku, 0, all, logical.length, haiku.length);
    return all;
  }
}
