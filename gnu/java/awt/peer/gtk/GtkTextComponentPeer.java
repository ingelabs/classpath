/* GtkTextComponentPeer.java -- Implements TextComponentPeer with GTK
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

public class GtkTextComponentPeer extends GtkComponentPeer
  implements TextComponentPeer
{
  GtkTextComponentPeer (TextComponent tc)
  {
    super (tc);

    setText (tc.getText ());
  }
  
  public native int getCaretPosition ();
  public void setCaretPosition (int pos)
  {
    set ("text_position", pos);
  }
  public native int getSelectionStart ();
  public native int getSelectionEnd ();
  public native String getText ();
  public native void select (int start, int end);

  public void setEditable (boolean state)
  {
    set ("editable", state);
  }

  public native void setText (String text);

  public void getArgs (Component component, GtkArgList args)
  {
    super.getArgs (component, args);

    TextComponent tc = (TextComponent) component;

    args.add ("text_position", tc.getCaretPosition ());
    args.add ("editable", tc.isEditable ());
  }
}
