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
  }

  public Graphics getGraphics ()
  {
    return new GdkGraphics (this);
  }

  public void handleEvent (AWTEvent event)
  {
    int id = event.getID();
      
    switch (id)
      {
      case PaintEvent.PAINT:
      case PaintEvent.UPDATE:
	{
	  try 
	    {
	      Graphics g = getGraphics ();
	      g.setClip (((PaintEvent)event).getUpdateRect());
		
	      if (id == PaintEvent.PAINT)
		awtComponent.paint (g);
	      else
		awtComponent.update (g);
	      
	      g.dispose ();
	    } 
	  catch (InternalError e)
	    { 
	      System.err.println (e);
	    }
	}
	break;
      }
  }
}
