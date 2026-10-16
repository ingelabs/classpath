/* HaikuPrinterJob.java -- Java 2 printing with BPrintJob
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

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

/**
 * A java.awt.print.PrinterJob that prints with Haiku's print server.
 * PrinterJob.getPrinterJob() returns it when the system property
 * java.awt.printerjob names this class (the Haiku toolkit sets it).
 */
public class HaikuPrinterJob
  extends PrinterJob
{

  private long job;

  private Printable printable;

  private PageFormat format;

  private Pageable pageable;

  private String jobName = "Java";

  private int copies = 1;

  private volatile boolean cancelled;

  public HaikuPrinterJob()
  {
    HaikuNative.ensureInitialized();
  }

  public int getCopies()
  {
    return copies;
  }

  public void setCopies(int copies)
  {
    this.copies = copies;
  }

  public String getJobName()
  {
    return jobName;
  }

  public void setJobName(String name)
  {
    jobName = name;
  }

  public String getUserName()
  {
    return System.getProperty("user.name");
  }

  public void cancel()
  {
    cancelled = true;
  }

  public boolean isCancelled()
  {
    return cancelled;
  }

  /** The paper of the print job, or US Letter before a dialog. */
  public PageFormat defaultPage(PageFormat pf)
  {
    PageFormat result = (PageFormat) pf.clone();
    Paper paper = new Paper();
    if (job != 0)
      {
        float[] i = new float[12];
        HaikuNative.printInfo(job, i);
        paper.setSize(i[2] - i[0], i[3] - i[1]);
        paper.setImageableArea(i[4] - i[0], i[5] - i[1], i[6] - i[4],
                               i[7] - i[5]);
      }
    result.setPaper(paper);
    return result;
  }

  public PageFormat pageDialog(PageFormat pf)
  {
    boolean ok;
    if (job == 0)
      {
        job = HaikuNative.printCreate(jobName, true, false);
        ok = job != 0;
      }
    else
      ok = HaikuNative.printDialog(job, true);
    return ok ? defaultPage(pf) : pf;
  }

  public boolean printDialog()
  {
    if (job == 0)
      {
        job = HaikuNative.printCreate(jobName, false, true);
        return job != 0;
      }
    return HaikuNative.printDialog(job, false);
  }

  public void setPageable(Pageable p)
  {
    pageable = p;
    printable = null;
  }

  public void setPrintable(Printable p)
  {
    setPrintable(p, defaultPage());
  }

  public void setPrintable(Printable p, PageFormat pf)
  {
    printable = p;
    format = pf;
    pageable = null;
  }

  public PageFormat validatePage(PageFormat pf)
  {
    return (PageFormat) pf.clone();
  }

  /**
   * Prints all pages (or the range chosen in the print dialog). Without
   * a dialog before, the print server's settings are used.
   */
  public void print()
    throws PrinterException
  {
    if (printable == null && pageable == null)
      throw new PrinterException("nothing to print");
    if (job == 0)
      job = HaikuNative.printCreate(jobName, false, false);
    if (job == 0)
      throw new PrinterException("cannot start a print job");
    cancelled = false;
    float[] info = new float[12];
    HaikuNative.printInfo(job, info);
    int first = Math.max(1, (int) info[8]);
    int last = (int) info[9];
    if (last < first || last > 9999)
      last = Integer.MAX_VALUE;
    int w = Math.max(1, Math.round(info[6] - info[4]));
    int h = Math.max(1, Math.round(info[7] - info[5]));
    double scale = HaikuPrintJob.pictureScale(info[10], info[11]);
    boolean ok = HaikuNative.printBegin(job);
    if (! ok)
      {
        HaikuNative.printEnd(job, false);
        job = 0;
        throw new PrinterException("the print server refused the job");
      }
    try
      {
        int count = pageable != null ? pageable.getNumberOfPages()
                                     : Integer.MAX_VALUE;
        for (int index = 0; ok && ! cancelled && index < count
                            && index + 1 <= last; index++)
          {
            Printable p = pageable != null ? pageable.getPrintable(index)
                                           : printable;
            PageFormat pf = pageable != null ? pageable.getPageFormat(index)
                                             : format;
            if (pf == null)
              pf = defaultPage();
            BufferedImage img = HaikuPrintJob.newPage(w, h, scale);
            Graphics2D g = img.createGraphics();
            g.scale(scale, scale);
            // Page coordinates start at the paper corner.
            g.translate(-(info[4] - info[0]), -(info[5] - info[1]));
            g.setColor(java.awt.Color.black);
            g.setBackground(java.awt.Color.white);
            int r = p.print(g, pf, index);
            g.dispose();
            if (r == Printable.NO_SUCH_PAGE)
              break;
            if (index + 1 >= first)
              ok = HaikuPrintJob.spool(job, img);
          }
      }
    finally
      {
        long j = job;
        job = 0;
        HaikuNative.printEnd(j, ok && ! cancelled);
      }
    if (! ok)
      throw new PrinterException("printing failed");
  }
}
