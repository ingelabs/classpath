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

  protected void postExposeEvent (int x, int y, int width, int height)
  {
    q.postEvent (new PaintEvent (awtComponent, PaintEvent.UPDATE,
				 new Rectangle (x, y, width, height)));
  }

  public void handleEvent (AWTEvent e)
  {
    int id = e.getID();

    switch (id)
    {
    case PaintEvent.PAINT:
      awtComponent.paint (getGraphics ());
      break;
    case PaintEvent.UPDATE:
      awtComponent.update (getGraphics ());
      break;
    }
  }
}
