/*
 * GtkWindowPeer.java -- Implements WindowPeer with GTK
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

public class GtkWindowPeer extends GtkContainerPeer
  implements WindowPeer
{
  static protected final int GTK_WINDOW_TOPLEVEL = 0;
  static protected final int GTK_WINDOW_DIALOG = 1;
  static protected final int GTK_WINDOW_POPUP = 2;

  native void create (int type);

  void create ()
  {
    create (GTK_WINDOW_POPUP);
  }

  native void connectHooks ();

  public GtkWindowPeer (Window window)
  {
    super (window);

    Dimension d = window.getSize ();
    setBounds (0, 0, d.width, d.height);
  }

  public void getArgs (Component component, GtkArgList args)
  {
    args.add ("visible", component.isVisible ());
    args.add ("sensitive", component.isEnabled ());
  }
  
  native public void toBack ();
  native public void toFront ();

  native public void setBounds (int x, int y, int width, int height);

  public void setTitle (String title)
  {
    set ("title", title);
  }

  native public void setResizable (boolean r);

  native public void setMenuBarPeer (MenuBarPeer bar);

  public void setMenuBar (MenuBar bar)
  {
    if (bar == null)
      setMenuBarPeer (null);
    else
      setMenuBarPeer ((MenuBarPeer) bar.getPeer ());
  }

  protected void postConfigureEvent (int x, int y, int width, int height,
				     int top, int left, int bottom, int right)
  {
    /* 
       If our borders change (which often happens when we opaque resize),
       we need to make sure that a new layout will happen, since Sun
       forgets to handle this case.
    */
    if (insets.top != top
	|| insets.left != left
	|| insets.bottom != bottom
	|| insets.right != right)
      {
	System.out.println ("invalidating");
	awtComponent.invalidate ();
      }
    
    insets.top = top;
    insets.left = left;
    insets.bottom = bottom;
    insets.right = right;

    awtComponent.setBounds (x, y, width, height);
    awtComponent.validate ();
  }
  
  public void setVisible (boolean b)
  {
    System.out.println ("setVisible called: " + b);
    super.setVisible (b);
  }
}
