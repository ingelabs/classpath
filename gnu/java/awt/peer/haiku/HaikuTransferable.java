/* HaikuTransferable.java -- data from a Haiku message
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

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The data in a Haiku message: the clipboard, a drag or a drop. Text
 * ("text/plain") is a String, files ("refs") are a file list, pictures
 * (archived bitmaps or any image type a translator reads) are an Image,
 * and other MIME types are an InputStream (and a String for text types).
 *
 * The data is read when it is asked for. If the message was replaced in
 * the meantime, getTransferData() throws an IOException.
 */
final class HaikuTransferable
  implements Transferable
{

  private final int src;

  private final int generation;

  private final DataFlavor[] flavors;

  /** The MIME type in the message for each flavor. */
  private final HashMap<DataFlavor,String> types =
    new HashMap<DataFlavor,String>();

  private HaikuTransferable(int src)
  {
    this.src = src;
    generation = HaikuNative.msgGeneration(src);
    String[] names = HaikuNative.msgTypes(src);
    ArrayList<DataFlavor> l = new ArrayList<DataFlavor>();
    boolean image = false;
    for (int i = 0; i < names.length; i++)
      {
        String n = names[i];
        if (n.equals("text/plain"))
          add(l, DataFlavor.stringFlavor, n);
        else if (n.equals("refs"))
          add(l, DataFlavor.javaFileListFlavor, n);
        else if (n.startsWith("image/"))
          {
            if (! image)
              add(l, DataFlavor.imageFlavor, n);
            image = true;
            if (n.indexOf('/') > 0 && ! n.equals("image/bitmap")
                && ! n.equals("image/x-be-bitmap"))
              addMime(l, n);
          }
        else if (n.indexOf('/') > 0)
          addMime(l, n);
      }
    flavors = l.toArray(new DataFlavor[l.size()]);
  }

  /** The data of a message source, or null if it has nothing usable. */
  static HaikuTransferable read(int src)
  {
    HaikuTransferable t = new HaikuTransferable(src);
    return t.flavors.length > 0 ? t : null;
  }

  private void add(List<DataFlavor> l, DataFlavor f, String type)
  {
    if (! types.containsKey(f))
      {
        l.add(f);
        types.put(f, type);
      }
  }

  private void addMime(List<DataFlavor> l, String mime)
  {
    try
      {
        add(l, new DataFlavor(mime + "; class=java.io.InputStream"), mime);
        if (mime.startsWith("text/"))
          add(l, new DataFlavor(mime + "; class=java.lang.String"), mime);
      }
    catch (Exception e)
      {
        // Not a MIME type that Java understands.
      }
  }

  public DataFlavor[] getTransferDataFlavors()
  {
    return (DataFlavor[]) flavors.clone();
  }

  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return types.containsKey(flavor);
  }

  public Object getTransferData(DataFlavor flavor)
    throws UnsupportedFlavorException, IOException
  {
    String type = types.get(flavor);
    if (type == null)
      throw new UnsupportedFlavorException(flavor);
    if (HaikuNative.msgGeneration(src) != generation)
      throw new IOException("the data is no longer available");
    if (flavor.equals(DataFlavor.javaFileListFlavor))
      {
        String[] paths = HaikuNative.msgFiles(src);
        ArrayList<File> files = new ArrayList<File>();
        for (int i = 0; i < paths.length; i++)
          files.add(new File(paths[i]));
        return files;
      }
    if (flavor.equals(DataFlavor.imageFlavor))
      {
        int[] dims = new int[2];
        int[] argb = HaikuNative.msgImage(src, dims);
        if (argb == null)
          throw new IOException("cannot read the picture");
        BufferedImage img = new BufferedImage(dims[0], dims[1],
                                              BufferedImage.TYPE_INT_ARGB);
        img.setRGB(0, 0, dims[0], dims[1], argb, 0, dims[0]);
        return img;
      }
    byte[] data = HaikuNative.msgData(src, type);
    if (data == null)
      throw new IOException("no data for " + type);
    Class<?> c = flavor.getRepresentationClass();
    if (c == String.class)
      {
        String cs = flavor.getParameter("charset");
        return new String(data, cs != null && ! flavor.equals(DataFlavor.stringFlavor)
                                ? cs : "UTF-8");
      }
    return new ByteArrayInputStream(data);
  }

  /** Copies an image to ARGB pixels; dims gets width and height. */
  static int[] pixels(Image img, int[] dims)
  {
    BufferedImage b;
    if (img instanceof BufferedImage)
      b = (BufferedImage) img;
    else
      {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        if (w <= 0 || h <= 0)
          return null;
        b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        java.awt.Graphics g = b.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
      }
    dims[0] = b.getWidth();
    dims[1] = b.getHeight();
    return b.getRGB(0, 0, dims[0], dims[1], null, 0, dims[0]);
  }

  /**
   * Makes a Haiku message from a Transferable: text, files, a picture,
   * and the other MIME types whose data is bytes, text or a stream.
   */
  static long toMessage(Transferable t, int what)
  {
    long m = HaikuNative.msgNew(what);
    if (t == null)
      return m;
    DataFlavor[] fs = t.getTransferDataFlavors();
    java.util.HashSet<String> done = new java.util.HashSet<String>();
    for (int i = 0; i < fs.length; i++)
      {
        DataFlavor f = fs[i];
        try
          {
            String mime = f.getPrimaryType() + "/" + f.getSubType();
            if (f.equals(DataFlavor.stringFlavor))
              {
                if (done.add("text/plain"))
                  HaikuNative.msgAddData(m, "text/plain",
                    ((String) t.getTransferData(f)).getBytes("UTF-8"));
              }
            else if (f.equals(DataFlavor.imageFlavor))
              {
                if (done.add("image"))
                  {
                    int[] dims = new int[2];
                    int[] argb = pixels((Image) t.getTransferData(f), dims);
                    if (argb != null)
                      HaikuNative.msgAddImage(m, argb, dims[0], dims[1]);
                  }
              }
            else if (f.equals(DataFlavor.javaFileListFlavor))
              {
                if (done.add("refs"))
                  {
                    List<?> files = (List<?>) t.getTransferData(f);
                    String[] paths = new String[files.size()];
                    for (int k = 0; k < paths.length; k++)
                      paths[k] = ((File) files.get(k)).getAbsolutePath();
                    HaikuNative.msgAddFiles(m, paths);
                  }
              }
            else if (! mime.startsWith("application/x-java")
                     && ! done.contains(mime))
              {
                byte[] b = bytes(t.getTransferData(f), f);
                if (b != null)
                  {
                    HaikuNative.msgAddData(m, mime, b);
                    done.add(mime);
                  }
              }
          }
        catch (Exception e)
          {
            // Leave this flavor out.
          }
      }
    return m;
  }

  private static byte[] bytes(Object o, DataFlavor f)
    throws IOException
  {
    if (o instanceof byte[])
      return (byte[]) o;
    if (o instanceof String)
      {
        String cs = f.getParameter("charset");
        return ((String) o).getBytes(cs != null ? cs : "UTF-8");
      }
    java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
    if (o instanceof java.io.InputStream)
      {
        java.io.InputStream in = (java.io.InputStream) o;
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) > 0)
          out.write(buf, 0, n);
        return out.toByteArray();
      }
    if (o instanceof java.io.Reader)
      {
        java.io.Reader in = (java.io.Reader) o;
        StringBuilder sb = new StringBuilder();
        char[] buf = new char[4096];
        int n;
        while ((n = in.read(buf)) > 0)
          sb.append(buf, 0, n);
        return sb.toString().getBytes("UTF-8");
      }
    return null;
  }
}
