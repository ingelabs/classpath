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
     Sun's Motif implementation always returns 0 here, but
     going by the X11 man pages, it seems as though we should return
     font.ascent + font.descent.
  */
  public int getLeading ()
  {
    return metrics[ASCENT] + metrics[DESCENT];
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
