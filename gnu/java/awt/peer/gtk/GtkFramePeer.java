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

public class GtkFramePeer extends GtkWindowPeer
    implements FramePeer
{
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

  public void setBounds (int x, int y, int width, int height)
  {
    Insets insets = ((Frame)awtComponent).getInsets ();
    super.setBounds (x, y, 
		     width - insets.left - insets.right, 
		     height - insets.top - insets.bottom + 
		     getMenuBarHeight ());
  }

  protected void postConfigureEvent (int x, int y, int width, int height,
				     int top, int left, int bottom, int right)
  {
    if (((Frame)awtComponent).getMenuBar () != null)
      {
	top += getMenuBarHeight ();
      }

    super.postConfigureEvent (x, y, 
			      width + left + right,
			      height + top + bottom - getMenuBarHeight(), 
			      top, left, bottom, right);
  }
}


