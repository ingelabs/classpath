/*
 * GtkTextComponentPeer.java -- Implements TextComponentPeer with GTK
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

public class GtkTextComponentPeer extends GtkComponentPeer
  implements TextComponentPeer
{
  Object editable;

  native int gtkEditableGetPosition (Object edit);
  native void gtkEditableSetPosition (Object edit, int pos);
  native int gtkEditableGetSelectionStart (Object edit);
  native int gtkEditableGetSelectionEnd (Object edit);
  native void gtkEditableSelectRegion (Object edit, int start, int end);
  native void gtkEditableSetEditable (Object edit, int state);

  GtkTextComponentPeer()
    {
      editable=this;
    }
  
  public int getCaretPosition ()
    {
      return gtkEditableGetPosition (editable);
    }
  
  public void setCaretPosition (int pos)
    {
      gtkEditableSetPosition (editable, pos);
    }
  
  public int getSelectionStart ()
    {
      return gtkEditableGetSelectionStart (editable);
    }
  
  public int getSelectionEnd ()
    {
      return gtkEditableGetSelectionEnd (editable);
    }

  public String getText ()
    {
      return null;
    }

  public void select (int start, int end)
    {
      gtkEditableSelectRegion (editable, start, end);
    }

  public void setEditable (boolean state)
    {
      gtkEditableSetEditable (editable, state? 1 : 0);
    }

  public void setText (String text)
    {
    }
}
