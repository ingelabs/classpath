/* GdkFontMetrics.java
   Copyright (C) 1999 Free Software Foundation, Inc.

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

public class GdkFontMetrics extends FontMetrics
{
  private final int native_state = java.lang.System.identityHashCode (this);

  private static final int ASCENT = 0, MAX_ASCENT = 1, 
                       DESCENT = 2, MAX_DESCENT = 3, 
                       MAX_ADVANCE = 4;

  private int[] metrics;
  private native int[] initState (String xlfd, int pts);

  public GdkFontMetrics (Font font)
  {
    super (font);
    metrics = initState (((GtkFontPeer)font.getPeer ()).getXLFD (), 
			 font.getSize ());
  }

  native public int stringWidth (String str);

  public int charWidth (char ch)
  {
    return stringWidth (new String (new char[] { ch }));
  }

  public int charsWidth (char data[], int off, int len)
  {
    return stringWidth (new String (data, off, len));
  }

  /* 
     Sun's Motif implementation always returns 0 or 1 here (???), but
     going by the X11 man pages, it seems as though we should return
     font.ascent + font.descent.
  */
  public int getLeading ()
  {
    return 1;
//      return metrics[ASCENT] + metrics[DESCENT];
  }

  public int getAscent ()
  {
    return metrics[ASCENT];
  }

  public int getMaxAscent ()
  {
    return metrics[MAX_ASCENT];
  }

  public int getDescent ()
  {
    return metrics[DESCENT];
  }

  public int getMaxDescent ()
  {
    return metrics[MAX_DESCENT];
  }

  public int getMaxAdvance ()
  {
    return metrics[MAX_ADVANCE];
  }
}
