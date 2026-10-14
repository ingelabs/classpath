/* HaikuPrintJob.java -- AWT 1.1 printing with BPrintJob
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PrintGraphics;
import java.awt.PrintJob;
import java.awt.image.BufferedImage;

/**
 * An AWT print job (Toolkit.getPrintJob()). The page setup and print
 * dialogs are Haiku's. Each page is drawn in Java into a picture of the
 * printable area and then spooled with BPrintJob. Page coordinates are
 * points (1/72 inch) from the top left of the printable area.
 */
final class HaikuPrintJob
  extends PrintJob
{

  /** The most dots per inch used for page pictures. */
  static final int MAX_DPI = 150;

  private long job;

  /** Printable area in points. */
  private final int width, height;

  /** Picture dots per point. */
  private final double scale;

  private PageGraphics page;

  private boolean begun;

  private HaikuPrintJob(long job)
  {
    this.job = job;
    float[] info = new float[12];
    HaikuNative.printInfo(job, info);
    width = Math.max(1, Math.round(info[6] - info[4]));
    height = Math.max(1, Math.round(info[7] - info[5]));
    scale = pictureScale(info[10], info[11]);
  }

  /** Shows the dialogs; null if the user cancelled. */
  static HaikuPrintJob create(String title)
  {
    HaikuNative.ensureInitialized();
    long job = HaikuNative.printCreate(title, true, true);
    return job != 0 ? new HaikuPrintJob(job) : null;
  }

  static double pictureScale(float xdpi, float ydpi)
  {
    float dpi = Math.min(Math.max(xdpi, ydpi), MAX_DPI);
    return Math.max(dpi, 72) / 72.0;
  }

  /** A white page picture for an area of w x h points. */
  static BufferedImage newPage(int w, int h, double scale)
  {
    BufferedImage img = new BufferedImage((int) Math.ceil(w * scale),
                                          (int) Math.ceil(h * scale),
                                          BufferedImage.TYPE_INT_RGB);
    Graphics2D g = img.createGraphics();
    g.setColor(Color.white);
    g.fillRect(0, 0, img.getWidth(), img.getHeight());
    g.dispose();
    return img;
  }

  /** Spools a page picture. */
  static boolean spool(long job, BufferedImage img)
  {
    int w = img.getWidth();
    int h = img.getHeight();
    return HaikuNative.printPage(job, img.getRGB(0, 0, w, h, null, 0, w),
                                 w, h);
  }

  /** Starts a new page; the graphics finish the page when disposed. */
  public synchronized Graphics getGraphics()
  {
    if (job == 0)
      return null;
    if (page != null)
      page.dispose();
    if (! begun)
      {
        begun = true;
        HaikuNative.printBegin(job);
      }
    BufferedImage img = newPage(width, height, scale);
    page = new PageGraphics(img, this);
    page.scale(scale, scale);
    page.setColor(Color.black);
    page.setBackground(Color.white);
    return page;
  }

  synchronized void pageDone(PageGraphics g)
  {
    if (g != page || job == 0)
      return;
    page = null;
    spool(job, g.image());
  }

  public Dimension getPageDimension()
  {
    return new Dimension(width, height);
  }

  public int getPageResolution()
  {
    return 72;
  }

  public boolean lastPageFirst()
  {
    return false;
  }

  /** Prints the pages. */
  public synchronized void end()
  {
    if (job == 0)
      return;
    if (page != null)
      page.dispose();
    long j = job;
    job = 0;
    HaikuNative.printEnd(j, begun);
  }

  /** The graphics of one page. */
  static final class PageGraphics
    extends HaikuGraphics2D
    implements PrintGraphics
  {
    private final HaikuPrintJob printJob;
    private final BufferedImage img;
    private boolean root = true;

    PageGraphics(BufferedImage img, HaikuPrintJob job)
    {
      super(img, null);
      this.img = img;
      printJob = job;
    }

    BufferedImage image()
    {
      return img;
    }

    public PrintJob getPrintJob()
    {
      return printJob;
    }

    public Graphics create()
    {
      PageGraphics copy = (PageGraphics) super.create();
      copy.root = false;
      return copy;
    }

    public void dispose()
    {
      super.dispose();
      if (root)
        printJob.pageDone(this);
    }
  }
}
