package gnu.java.awt.peer.gtk;
import java.awt.*;
import java.awt.event.PaintEvent;
import java.awt.peer.*;

public class GtkCanvasPeer extends GtkComponentPeer implements CanvasPeer
{
  native void gtkCanvasNew (int width, int height, boolean visible);

  public GtkCanvasPeer (Canvas c, ComponentPeer cp)
  {
    super (c);
    Dimension d = c.getSize();
    gtkCanvasNew (d.width, d.height, c.isVisible ());

    Point p = c.getLocation ();
    System.out.println ("canvaspeer: location: "+p.x+","+p.y);
    
    gtkFixedPut (cp, p.x, p.y);
  }

  public Graphics getGraphics ()
  {
    return new GdkGraphics (this);
  }
}
