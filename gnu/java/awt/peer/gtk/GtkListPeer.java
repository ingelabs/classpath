/*
 * GtkListPeer.java -- Implements ListPeer with GTK
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
import java.awt.peer.*;

public class GtkListPeer extends GtkComponentPeer
  implements ListPeer
{
  native void gtkListNew (ComponentPeer parent,
			  Object list, String [] items, boolean mode);
  native void gtkListInsert (Object list, String item, int index);    
  native void gtkListClearItems (Object list, int start, int end);
  native void gtkListSelectItem (Object list, int index);
  native void gtkListUnselectItem (Object list, int index);
  native void gtkListGetSize (Object list, int rows, int dims[]);
  native int[] gtkListGetSelected (Object list);
  native void gtkListScrollVertical (Object list, int index);
  native void gtkListSetSelectionMode (Object list, boolean mode);

  Object myGtkList;

  public GtkListPeer (List l, ComponentPeer cp)
  {
    super (l);
    String items[] = l.getItems();
      
    myGtkList = new Object();

    gtkListNew (cp, myGtkList, items, l.isMultipleMode());
    syncAttributes ();
  }
  
  public void add (String item, int index)
  {
    gtkListInsert (myGtkList, item, index);
  }
  
  public void addItem (String item, int index)
  {
    add (item, index);
  }
  
  public void clear ()
  {
    removeAll ();
  }
  
  public void delItems (int start, int end)
  {
    gtkListClearItems (myGtkList, start, end);
  }

  public void deselect (int index)
  {
    gtkListUnselectItem (myGtkList, index);
  }
  
  public Dimension getMinimumSize (int rows)
  {
    int dims[] = new int[2];

    gtkListGetSize (myGtkList, rows, dims);
    return (new Dimension (dims[0], dims[1]));
  }

  public Dimension getPreferredSize (int rows)
  {
    int dims[] = new int[2];

    gtkListGetSize (myGtkList, rows, dims);
    return (new Dimension (dims[0], dims[1]));
  }
  
  public int[] getSelectedIndexes ()
  {
    return (gtkListGetSelected (myGtkList));
  }
  
  public void makeVisible (int index)
  {
    gtkListScrollVertical (myGtkList, index);
  }

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
    gtkListClearItems (myGtkList, 0, -1);
  }

  public void select (int index)
  {
    gtkListSelectItem (myGtkList, index);
  }

  public void setMultipleMode (boolean b)
  {
    gtkListSetSelectionMode (myGtkList, b);
  }

  public void setMultipleSelections (boolean b)
  {
    setMultipleMode (b);
  }

  protected void postItemEvent (int item, int stateChange)
  {
    postItemEvent (new Integer (item), stateChange);
  }
}
