/* GtkListPeer.java -- Implements ListPeer with GTK
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
import java.awt.*;
import java.awt.peer.*;

public class GtkListPeer extends GtkComponentPeer
  implements ListPeer
{
//    native void create (ComponentPeer parent, String [] items, boolean mode);

  native void create ();
  native void connectHooks ();

  native void getSize (int rows, int dims[]);

  public GtkListPeer (List list)
  {
    super (list);
    
    setMultipleMode (list.isMultipleMode ());

    if (list.getItemCount () > 0)
      append (list.getItems ());
  }

  native void append (String items[]);

  public native void add (String item, int index);
  
  public void addItem (String item, int index)
  {
    add (item, index);
  }
  
  public void clear ()
  {
    removeAll ();
  }
  
  public native void delItems (int start, int end);
  public native void deselect (int index);
  
  public Dimension getMinimumSize (int rows)
  {
    int dims[] = new int[2];

    getSize (rows, dims);
    return (new Dimension (dims[0], dims[1]));
  }

  public Dimension getPreferredSize (int rows)
  {
    int dims[] = new int[2];

    getSize (rows, dims);
    return (new Dimension (dims[0], dims[1]));
  }
  
  public native int[] getSelectedIndexes ();
  public native void makeVisible (int index);

  public Dimension minimumSize (int rows)
  {
    return (getMinimumSize (rows));
  }

  public Dimension preferredSize (int rows)
  {
    return (getPreferredSize (rows));
  }

  public void removeAll ()
  {
    delItems (0, -1);
  }

  public native void select (int index);
  public native void setMultipleMode (boolean b);

  public void setMultipleSelections (boolean b)
  {
    setMultipleMode (b);
  }

  protected void postItemEvent (int item, int stateChange)
  {
    postItemEvent (new Integer (item), stateChange);
  }
}
