/*
 * GtkTextAreaPeer.java -- Implements TextAreaPeer with GTK
 *
 * Copyright (c) 1998 Free Software Foundation, Inc.
 * Written by James E. Blair <corvus@gnu.org>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later verion.
 *
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this library; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 */

package gnu.java.awt.peer.gtk;
import java.awt.peer.*;
import java.awt.*;

public class GtkTextAreaPeer extends GtkTextComponentPeer
  implements TextAreaPeer
{
  native void create (int scrollbarVisibility);

  void create ()
  {
    create (((TextArea)awtComponent).getScrollbarVisibility ());
  }

  // native void create (Object parent, String text, int scroll);
  native void gtkTextGetSize (int rows, int cols, int dims[]);

  public GtkTextAreaPeer (TextArea ta)
  {
    super (ta);
  }

  public native void insert (String str, int pos);
  public native void replaceRange (String str, int start, int end);

  public Dimension getMinimumSize (int rows, int cols)
  {
    int dims[] = new int[2];

    gtkTextGetSize (rows, cols, dims);

    System.out.println ("TAP: getMinimumSize " + cols + " = " + dims[0] + 
			" x " + dims[1]);
      
    return (new Dimension (dims[0], dims[1]));
  }

  public Dimension getPreferredSize (int rows, int cols)
  {
    int dims[] = new int[2];

    gtkTextGetSize (rows, cols, dims);

    System.out.println ("TAP: getPreferredSize " + cols + " = " + dims[0] + 
			" x " + dims[1]);
      
    return (new Dimension (dims[0], dims[1]));
  }

  /* Deprecated */
  public Dimension minimumSize (int rows, int cols)
  {
    return getMinimumSize (rows, cols);
  }

  public Dimension preferredSize (int rows, int cols)
  {
    return getPreferredSize (rows, cols);
  }

  public void replaceText (String str, int start, int end)
  {
    replaceRange (str, start, end);
  }

  public void insertText (String str, int pos)
  {
    insert (str, pos);
  }
}
