/* HaikuFileDialogPeer.java -- part of the Haiku AWT peer
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

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.peer.FileDialogPeer;
import java.io.File;
import java.io.FilenameFilter;

/**
 * A file dialog shown as a native BFilePanel.
 *
 * The dialog's own window is never shown. FileDialog.show() blocks in
 * Dialog.show() until the panel closes; then {@link #panelClosed} stores
 * the result and hides the dialog, which makes show() return.
 *
 * A FilenameFilter is supported: the panel asks for each directory it
 * shows (EV_FILTER), and {@link #filterRequested} answers for every name in
 * it. Folders are always shown.
 */
public class HaikuFileDialogPeer
  extends HaikuDialogPeer
  implements FileDialogPeer
{

  HaikuFileDialogPeer(FileDialog fd)
  {
    super(fd);
  }

  /** The filter of the open panel, or null. */
  private volatile FilenameFilter activeFilter;

  public void show()
  {
    FileDialog fd = (FileDialog) getWindow();
    FilenameFilter filter = fd.getFilenameFilter();
    String file = fd.getFile();
    // A file name such as "*.txt" or "*.gif;*.jpg" is used as a filter.
    if (file != null && file.indexOf('*') >= 0)
      {
        if (filter == null)
          filter = new GlobFilter(file);
        file = null;
      }
    activeFilter = filter;
    HaikuNative.filePanelShow(id, fd.getMode() == FileDialog.SAVE,
                              fd.getTitle(), fd.getDirectory(), file,
                              filter != null);
  }

  /**
   * Called by the event pump when the panel shows a directory that it has
   * no answers for. Runs the FilenameFilter on every name in it.
   */
  void filterRequested()
  {
    String dir;
    while ((dir = HaikuNative.filterNext(id)) != null)
      {
        File d = new File(dir);
        String[] names = d.list();
        if (names == null)
          names = new String[0];
        boolean[] accepted = new boolean[names.length];
        FilenameFilter filter = activeFilter;
        for (int i = 0; i < names.length; i++)
          {
            try
              {
                accepted[i] = filter == null || filter.accept(d, names[i]);
              }
            catch (RuntimeException e)
              {
                accepted[i] = true;
              }
          }
        HaikuNative.filterAnswer(id, dir, names, accepted);
      }
  }

  /** Matches names against patterns such as "*.txt;*.java". */
  private static final class GlobFilter
    implements FilenameFilter
  {
    private final java.util.regex.Pattern pattern;

    GlobFilter(String globs)
    {
      StringBuilder re = new StringBuilder();
      java.util.StringTokenizer t = new java.util.StringTokenizer(globs,
                                                                  ";,");
      while (t.hasMoreTokens())
        {
          String g = t.nextToken().trim();
          if (re.length() > 0)
            re.append('|');
          for (int i = 0; i < g.length(); i++)
            {
              char c = g.charAt(i);
              if (c == '*')
                re.append(".*");
              else if (c == '?')
                re.append('.');
              else if (Character.isLetterOrDigit(c))
                re.append(c);
              else
                re.append('\\').append(c);
            }
        }
      pattern = java.util.regex.Pattern.compile(re.toString(),
                  java.util.regex.Pattern.CASE_INSENSITIVE);
    }

    public boolean accept(File dir, String name)
    {
      return pattern.matcher(name).matches();
    }
  }

  public void hide()
  {
    // The panel hides itself.
  }

  /**
   * Called by the event pump when the panel closed.
   */
  void panelClosed(boolean chosen)
  {
    final String path = chosen ? HaikuNative.fileResult(id) : null;
    final FileDialog fd = (FileDialog) getWindow();
    EventQueue.invokeLater(new Runnable()
      {
        public void run()
        {
          if (path != null)
            {
              File f = new File(path);
              String dir = f.getParent();
              if (dir != null && ! dir.endsWith(File.separator))
                dir += File.separator;
              fd.setDirectory(dir);
              fd.setFile(f.getName());
            }
          else
            fd.setFile(null);
          fd.setVisible(false);
        }
      });
  }

  public void setFile(String file)
  {
    // Used the next time the panel opens.
  }

  public void setDirectory(String dir)
  {
    // Used the next time the panel opens.
  }

  public void setFilenameFilter(FilenameFilter filter)
  {
    // Used the next time the panel opens.
  }
}
