/* GtkWindowPeer.java -- Implements WindowPeer with GTK
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


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
  
  native public void setVisible (boolean b);
}
