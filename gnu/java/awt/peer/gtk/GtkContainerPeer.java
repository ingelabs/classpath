/*
 * GtkContainerPeer.java -- Implements ContainerPeer with GTK
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
import java.awt.Insets;
import java.awt.peer.ContainerPeer;

public class GtkContainerPeer extends GtkComponentPeer
  implements ContainerPeer
{
  Insets myInsets;

  public GtkContainerPeer()
    {
      myInsets=new Insets(0,0,0,0);
    }

  public void beginValidate() 
    {
	System.out.println("container beginvalidate");
	/* We should probably put some code here to "freeze" 
	   the native container widget.  Until that happens, we
	   may see the layout manager moving widgets. */
    }

  public void endValidate() 
    {
	System.out.println("container endvalidate");
	/* The way I am interpreting this is that now that the Container
	   has validated its peers, we should cause the container widget
	   to unfreeze and draw the container's children. -JB */
	GtkWidgetShowChildren();
    }

  public Insets getInsets() 
    {
      return myInsets;
    }

  public Insets insets() 
    {
      return getInsets();
    }
}
