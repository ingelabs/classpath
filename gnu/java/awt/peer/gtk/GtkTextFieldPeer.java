/* GtkTextFieldPeer.java -- Implements TextFieldPeer with GTK
   Copyright (C) 1998, 1999 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */


package gnu.java.awt.peer.gtk;
import java.awt.peer.*;
import java.awt.*;

public class GtkTextFieldPeer extends GtkTextComponentPeer
  implements TextFieldPeer
{

//    native void create (ComponentPeer parent, String text);

  native void create ();
  native void createHooks ();

  native void gtkEntryGetSize (int cols, int dims[]);

  public GtkTextFieldPeer (TextField tf)
  {
    super (tf);

    if (tf.echoCharIsSet ())
      setEchoChar (tf.getEchoChar ());
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
  
  public native void setEchoChar (char c);

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
