/* GtkScrollPanePeer.java -- Implements ScrollPanePeer with GTK
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

This file is part of the peer AWT libraries of GNU Classpath.

This library is free software; you can redistribute it and/or modify
it under the terms of the GNU Library General Public License as published 
by the Free Software Foundation, either version 2 of the License, or
(at your option) any later verion.

This library is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Library General Public License for more details.

You should have received a copy of the GNU Library General Public License
along with this library; if not, write to the Free Software Foundation
Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA. */


package gnu.java.awt.peer.gtk;
import java.awt.peer.*;
import java.awt.*;

public class GtkScrollPanePeer extends GtkContainerPeer
  implements ScrollPanePeer
{
  native void create ();

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

  native void setPolicy (int policy);
  native public void childResized (int width, int height);
  native public int getHScrollbarHeight ();
  native public int getVScrollbarWidth ();
  native public void setScrollPosition (int x, int y);

//    public Dimension getPreferredSize ()
//    {
//      return new Dimension (60, 60);
//    }

  public void setUnitIncrement (Adjustable adj, int u)
  {
    if (adj.getOrientation()==Adjustable.HORIZONTAL)
      gtkScrolledWindowSetHScrollIncrement (u);
    else
      gtkScrolledWindowSetVScrollIncrement (u);
  }

  public void setValue (Adjustable adj, int v)
  {
//      System.out.println("SPP: setVal: "+adj+":"+v);
//      Point p=myScrollPane.getScrollPosition ();
//      if (adj.getOrientation()==Adjustable.HORIZONTAL)
//        gtkScrolledWindowSetScrollPosition (v,p.y);
//      else
//        gtkScrolledWindowSetScrollPosition (p.x,v);
//      adj.setValue(v);
  }
}
