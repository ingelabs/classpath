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

  native void gtkTextNew (Object edit, String text, int hscroll, int vscroll);
  native void gtkTextGetSize (Object edit, int rows, int cols, int dims[]);
  native String gtkTextGetText (Object edit);
  native void gtkTextSetText (Object edit, String text);
  native void gtkTextInsert (Object edit, String text, int pos);
  native void gtkTextReplace (Object edit, String text, int start, int end);

  public GtkTextAreaPeer (TextArea ta, ComponentPeer cp)
    {
      super (ta);
      String text = ta.getText();
      int hscroll=0;
      int vscroll=0;

      switch (ta.getScrollbarVisibility())
	{
	case TextArea.SCROLLBARS_BOTH:
	  hscroll=vscroll=1;
	  break;
	case TextArea.SCROLLBARS_HORIZONTAL_ONLY:
	  hscroll=1;
	  break;
	case TextArea.SCROLLBARS_VERTICAL_ONLY:
	  vscroll=1;
	  break;
	}

      System.out.println ("TAP: new: "+ text + " - " + hscroll + " - " + vscroll);

      editable = new Object();

      gtkTextNew (editable, text, hscroll, vscroll);

      Point p=ta.getLocation();
      gtkFixedPut (cp,p.x,p.y);
    }

  public Dimension getMinimumSize (int rows, int cols)
    {
      int dims[] = new int[2];

      gtkTextGetSize (editable, rows, cols, dims);

      System.out.println ("TAP: getMinimumSize " + cols + " = " + dims[0] + 
			  " x " + dims[1]);
      
      return (new Dimension (dims[0], dims[1]));
    }

  public Dimension getPreferredSize (int rows, int cols)
    {
      int dims[] = new int[2];

      gtkTextGetSize (editable, rows, cols, dims);

      System.out.println ("TAP: getPreferredSize " + cols + " = " + dims[0] + 
			  " x " + dims[1]);
      
      return (new Dimension (dims[0], dims[1]));
    }
  
  public String getText ()
    {
      System.out.println ("TAP: getText");
      return (gtkTextGetText(editable));
    }

  public void setText (String text)
    {
      System.out.println ("TAP: setText");
      gtkTextSetText (editable, text);
    }

  public void insert (String str, int pos)
    {
      gtkTextInsert (editable, str, pos);
    }

  public void replaceRange (String str, int start, int end)
    {
      gtkTextReplace (editable, str, start, end);
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
