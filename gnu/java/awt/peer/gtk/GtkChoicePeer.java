/*
 * GtkChoicePeer.java -- Implements ChoicePeer with GTK
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
import java.awt.event.*;

public class GtkChoicePeer extends GtkComponentPeer
  implements ChoicePeer
{

  native void gtkOptionMenuNew (ComponentPeer parent,
				String [] items, boolean visible);
  native void gtkOptionMenuAdd (String item, int index);
  native void gtkOptionMenuRemove (int index);
  native void gtkOptionMenuSelect (int index);

  public GtkChoicePeer (Choice c, ComponentPeer parent)
  {
    super (c);
    
    System.out.println("choicepeer: <init>");
    
    int count = c.getItemCount();
    
    String [] items=new String[count];
    
    for (int i = 0; i < count; i++)
      items[i] = c.getItem(i);
	  
    gtkOptionMenuNew (parent, items, c.isVisible ());
  }

  public void add (String item, int index)
  {
    gtkOptionMenuAdd (item, index);
  }
  
  public void addItem (String item, int position)
  {
    gtkOptionMenuAdd (item, position);
  }
  
  public void remove (int index)
  {
    gtkOptionMenuRemove (index);      
  }

  public void select (int position)
  {
    gtkOptionMenuSelect (position);
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
