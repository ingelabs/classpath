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
  static final int bogusType=0;
  static final int toplevelType=1;
  static final int dialogType=2;
  static final int popupType=3;

  native void gtkWindowNew(int type, int w, int h);    
  native void gtkWindowSetTitle(String title);    
  native void gtkWindowSetPolicy(int shrink, int grow, int autos);    
  
  public GtkWindowPeer(int type, Window w)
  {
    super (w);
    System.out.println("WindowPeer int cons");
    
    /* A bogusType indicates that we should not create a GTK window. */
    
    if(type!=bogusType)
      {
	Dimension d=w.getSize();
	gtkWindowNew(type,d.width,d.height);
      }
  }

  public GtkWindowPeer(Window w)
  {
    this(popupType,w);
    System.out.println("WindowPeer default cons");
  }
  
  native public void toBack();
  native public void toFront();

  native public void setBounds (int x, int y, int width, int height);

  protected void postConfigureEvent (int x, int y, int width, int height)
  {
    awtComponent.setBounds (x, y, width, height);
  }
}
