/*
 * GtkTextFieldPeer.java -- Implements TextFieldPeer with GTK
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

public class GtkTextFieldPeer extends GtkTextComponentPeer
  implements TextFieldPeer
{

  native void gtkEntryNew (ComponentPeer parent, String text);
  native void gtkEntryGetSize (int cols, int dims[]);
  native void gtkEntrySetEchoChar (char c);
  native String gtkEntryGetText ();
  native void gtkEntrySetText (String text);

  public GtkTextFieldPeer (TextField tf, ComponentPeer cp)
  {
    super (tf);
    String text = tf.getText();
      
    gtkEntryNew (cp, text);
    syncAttributes ();
  }

  public Dimension getMinimumSize (int cols)
  {
    int dims[] = new int[2];

    gtkEntryGetSize (cols, dims);

    System.out.println ("TFP: getMinimumSize " + cols + " = " + dims[0] + 
			" x " + dims[1]);
      
    return (new Dimension (dims[0], dims[1]));
  }

  public Dimension getPreferredSize (int cols)
  {
    int dims[] = new int[2];

    gtkEntryGetSize (cols, dims);

    System.out.println ("TFP: getPreferredSize " + cols + " = " + dims[0] + 
			" x " + dims[1]);
      
    return (new Dimension (dims[0], dims[1]));
  }
  
  public void setEchoChar (char c)
  {
    gtkEntrySetEchoChar (c);
  }

  public String getText ()
  {
    System.out.println ("TFP: getText");
    return (gtkEntryGetText());
  }

  public void setText (String text)
  {
    System.out.println ("TFP: setText");
    gtkEntrySetText (text);
  }

  /* Deprecated */

  public Dimension minimumSize (int cols)
  {
    return getMinimumSize (cols);
  }

  public Dimension preferredSize (int cols)
  {
    return getPreferredSize (cols);
  }

  public void setEchoCharacter (char c)
  {
    setEchoChar (c);
  }
}
