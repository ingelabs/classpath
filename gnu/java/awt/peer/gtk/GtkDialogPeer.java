/*
 * GtkDialogPeer.java -- Implements DialogPeer with GTK
 *
 * Copyright (c) 1998, 1999 Free Software Foundation, Inc.
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
import java.awt.*;
import java.awt.peer.*;

public class GtkDialogPeer extends GtkWindowPeer
    implements DialogPeer
{
  native void gtkDialogNew(int width, int height, boolean visible,
			   boolean resizable, String title, boolean modal,
			   Object parent);
  
  public GtkDialogPeer (Dialog w, ComponentPeer parent)
    {
      super (bogusType, w);
      
      Dimension d = w.getSize();
      System.out.println ("DIALOG:  modal =" + w.isModal());
      gtkDialogNew (d.width, d.height, w.isVisible (), w.isResizable (),
		    w.getTitle (), w.isModal(), parent);
    }

  public GtkDialogPeer (int type, Dialog w)
    {
      super (bogusType, w);
    }

  public void setTitle(String title)
    {
      System.out.println("setting title");
      gtkWindowSetTitle(title);
    }
  
  public void setResizable(boolean resizable)
    {
      int r=resizable?1:0;
      
      gtkWindowSetPolicy(r,r,0); //shrink,grow,auto
    }
}
