/*
 * GtkChoicePeer.java -- Implements ChoicePeer with GTK
 *
 * Copyright (c) 1998, 1999 Free Software Foundation, Inc.
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
import java.awt.event.*;

public class GtkChoicePeer extends GtkComponentPeer
  implements ChoicePeer
{
  native void create ();

  public GtkChoicePeer (Choice c)
  {
    super (c);

    int count = c.getItemCount ();
    if (count > 0)
      {
	String items[] = new String[count];
	for (int i = 0; i < count; i++)
	  items[i] = c.getItem (i);
	  
	append (items);
      }
  }

  native void append (String items[]);

  native public void add (String item, int index);
  native public void remove (int index);
  native public void select (int position);
  
  public void addItem (String item, int position)
  {
    add (item, position);
  }
  
  /*
  public void handleEvent (AWTEvent event)
  {
    if (event instanceof ItemEvent)
      ((Choice) awtComponent).select ((String) ((ItemEvent)event).getItem ());
    super.handleEvent (event);
  }
  */

  protected void postItemEvent (Object item, int stateChange)
  {
    if (stateChange == ItemEvent.SELECTED)
      ((Choice) awtComponent).select ((String) item);
    super.postItemEvent (item, stateChange);
  }
}
