/*
 * GtkFramePeer.java -- Implements FramePeer with GTK
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
import java.awt.*;
import java.awt.peer.FramePeer;
import java.awt.event.*;

public class GtkFramePeer extends GtkWindowPeer
    implements FramePeer
{
  int menuBarHeight = 0;
  native int getMenuBarHeight ();

  public GtkFramePeer (Frame f)
  {
    super (toplevelType, f);
    setTitle (f.getTitle ());
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


