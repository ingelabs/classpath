/* GtkFramePeer.java -- Implements FramePeer with GTK
   Copyright (C) 1999 Free Software Foundation, Inc.

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
import java.awt.*;
import java.awt.peer.FramePeer;
import java.awt.event.*;

public class GtkFramePeer extends GtkWindowPeer
    implements FramePeer
{
  int menuBarHeight = 0;
  native int getMenuBarHeight ();

  public GtkFramePeer (Frame frame)
  {
    super (frame);
  }

  void create ()
  {
    create (GTK_WINDOW_TOPLEVEL);
  }

  public void getArgs (Component component, GtkArgList args)
  {
    super.getArgs (component, args);

    Frame frame = (Frame) component;

    args.add ("title", frame.getTitle ());
    args.add ("allow_shrink", frame.isResizable ());
    args.add ("allow_grow", frame.isResizable ());
  }
  public void setIconImage (Image image) 
  {
      /* TODO: Waiting on Toolkit Image routines */
  }

  public Graphics getGraphics ()
  {
    GdkGraphics g = new GdkGraphics (this);
    g.translateNative (-insets.left, -insets.top);
    return g;
  }

  public void setBounds (int x, int y, int width, int height)
  {
    super.setBounds (0, 0, width - insets.left - insets.right,
		     height - insets.top - insets.bottom 
		     + getMenuBarHeight ());
  }

  protected void postConfigureEvent (int x, int y, int width, int height,
				     int top, int left, int bottom, int right)
  {
    if (((Frame)awtComponent).getMenuBar () != null)
      {
	menuBarHeight = getMenuBarHeight ();
	top += menuBarHeight;
      }

    super.postConfigureEvent (0, 0,
			      width + left + right,
			      height + top + bottom - menuBarHeight,
			      top, left, bottom, right);
  }

  protected void postMouseEvent(int id, long when, int mods, int x, int y, 
				int clickCount, boolean popupTrigger)
  {
    super.postMouseEvent (id, when, mods, 
			  x + insets.left, y + insets.top, 
			  clickCount, popupTrigger);
  }

  protected void postExposeEvent (int x, int y, int width, int height)
  {
//      System.out.println ("x + insets.left:" + (x + insets.left));
//      System.out.println ("y + insets.top :" + (y + insets.top));
    q.postEvent (new PaintEvent (awtComponent, PaintEvent.PAINT,
				 new Rectangle (x + insets.left, 
						y + insets.top, 
						width, height)));
  }
}


