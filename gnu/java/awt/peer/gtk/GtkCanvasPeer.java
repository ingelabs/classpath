package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.event.PaintEvent;
import java.awt.peer.*;

public class GtkCanvasPeer extends GtkComponentPeer implements CanvasPeer
{
  native void gtkCanvasNew (ComponentPeer parent,
			    int width, int height);

  public GtkCanvasPeer (Canvas c, ComponentPeer cp)
  {
    super (c);
    Dimension d = c.getSize();
    gtkCanvasNew (cp, d.width, d.height);

    Point p = c.getLocation ();
    syncAttributes ();
  }

  public Graphics getGraphics ()
  {
    return new GdkGraphics (this);
  }
}
