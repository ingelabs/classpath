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
    public GtkFramePeer(Frame f)
    {
      super(toplevelType,f);
      Dimension d=f.getSize();
      System.out.println("FramePeer contructor: "+d.width+" x "+d.height);
    }
    
    public void setIconImage (Image image) 
    {
      /* TODO: Waiting on Toolkit Image routines */
    }
  
    public void setMenuBar (MenuBar bar) 
    {
      /* TODO: Waiting on MenuComponentPeer */
    }

    public void setTitle(String title)
    {
	gtkWindowSetTitle(title);
    }

    public void setResizable(boolean resizable)
    {
	int r=resizable?1:0;

	gtkWindowSetPolicy(r,r,0); //shrink,grow,auto
    }
}


