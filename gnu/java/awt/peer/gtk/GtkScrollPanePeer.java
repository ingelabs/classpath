/*
 * GtkScrollPanePeer.java -- Implements ScrollPanePeer with GTK
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

public class GtkScrollPanePeer extends GtkContainerPeer
  implements ScrollPanePeer
{
  int policy;
  int sbHeight, sbWidth;
  ScrollPane myScrollPane;

  private static final int GTK_POLICY_ALWAYS = 0,
                           GTK_POLICY_AUTOMATIC = 1,
                           GTK_POLICY_NEVER = 2;

  native void gtkScrolledWindowNew(ComponentPeer parent,
				   int policy, int w, int h, int[] dims);
  native void gtkScrolledWindowSetScrollPosition(int x, int y);
  native void gtkScrolledWindowSetHScrollIncrement (int u);
  native void gtkScrolledWindowSetVScrollIncrement (int u);
  native void gtkScrolledWindowSetSize(int w, int h);
  
  public GtkScrollPanePeer (ScrollPane sp)
  {
    super (sp);

    setPolicy (sp.getScrollbarDisplayPolicy ());
  }

  native void setPolicyNative (int policy);

  void setPolicy (int policy)
  {
    switch (policy)
      {
      case ScrollPane.SCROLLBARS_ALWAYS:
	policy = GTK_POLICY_ALWAYS;
	break;
      case ScrollPane.SCROLLBARS_AS_NEEDED:
	policy = GTK_POLICY_AUTOMATIC;
	break;
      case ScrollPane.SCROLLBARS_NEVER:
	policy = GTK_POLICY_NEVER;
	break;
      }

    setPolicyNative (policy);
  }

  public void childResized (int w, int h)
  {
    System.out.println("SPP: child resized to: "+ w + " x " + h);

    gtkScrolledWindowSetSize(w, h);
  }

  public int getHScrollbarHeight ()
  {
    return sbHeight;
  }

  public int getVScrollbarWidth ()
  {
    return sbWidth;
  }

  native public void setScrollPosition (int x, int y);

  public void setUnitIncrement (Adjustable adj, int u)
  {
    /* Er... What calls this? */
      
    System.out.println("SPP: setUI: "+adj+":"+u);
    if (adj.getOrientation()==Adjustable.HORIZONTAL)
      gtkScrolledWindowSetHScrollIncrement (u);
    else
      gtkScrolledWindowSetVScrollIncrement (u);
  }

  public void setValue (Adjustable adj, int v)
  {
    System.out.println("SPP: setVal: "+adj+":"+v);
    Point p=myScrollPane.getScrollPosition ();
    if (adj.getOrientation()==Adjustable.HORIZONTAL)
      gtkScrolledWindowSetScrollPosition (v,p.y);
    else
      gtkScrolledWindowSetScrollPosition (p.x,v);
    adj.setValue(v);
  }
}
