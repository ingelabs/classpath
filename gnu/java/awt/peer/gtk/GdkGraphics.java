package gnu.java.awt.peer.gtk;

import java.awt.*;
import java.awt.image.*;

public class GdkGraphics extends Graphics
{
  private final int native_state = java.lang.System.identityHashCode (this);

  int xOrigin = 0, yOrigin = 0;
  Color color;

  native int[] initState (GtkComponentPeer component);

  GdkGraphics (GtkComponentPeer component)
  {
    int rgb[] = initState (component);
    color = new Color (rgb[0], rgb[1], rgb[2]);
  }

  native private void clearRectNative (int x, int y, int width, int height);
  public void clearRect (int x, int y, int width, int height)
  {
    clearRectNative (x + xOrigin, y + yOrigin, width, height);
  }

  public void clipRect (int x, int y, int width, int height)
  {
  }

  public void copyArea (int x, int y, int width, int height, int dx, int dy)
  {
  }

  public Graphics create ()
  {
    return null;
  }

  native public void dispose ();

  native private void drawArcNative (int x, int y, int width, int height, 
				     int startAngle, int arcAngle);
  public void drawArc (int x, int y, int width, int height, 
		       int startAngle, int arcAngle)
  {
    drawArcNative (x + xOrigin, y + yOrigin, width, height, 
		   startAngle, arcAngle);
  }

  public boolean drawImage (Image img, int x, int y, 
			    Color bgcolor, ImageObserver observer)
  {
    return false;
  }

  public boolean drawImage (Image img, int x, int y, ImageObserver observer)
  {
    return false;
  }

  public boolean drawImage (Image img, int x, int y, int width, int height, 
			    Color bgcolor, ImageObserver observer)
  {
    return false;
  }

  public boolean drawImage (Image img, int x, int y, int width, int height, 
			    ImageObserver observer)
  {
    return false;
  }

  public boolean drawImage (Image img, int dx1, int dy1, int dx2, int dy2, 
			    int sx1, int sy1, int sx2, int sy2, 
			    Color bgcolor, ImageObserver observer)
  {
    return false;
  }

  public boolean drawImage(Image img, int dx1, int dy1, int dx2, int dy2, 
			   int sx1, int sy1, int sx2, int sy2, 
			   ImageObserver observer) 
  {
    return false;
  }

  native private void drawLineNative (int x1, int y1, int x2, int y2);
  public void drawLine (int x1, int y1, int x2, int y2)
  {
    drawLineNative (x1 + xOrigin, y1 + yOrigin, x2 + xOrigin, y2 + yOrigin);
  }

  public void drawOval(int x, int y, int width, int height)
  {
  }

  public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints)
  {
  }

  public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints)
  {
  }

  public void drawRoundRect(int x, int y, int width, int height, 
			    int arcWidth, int arcHeight)
  {
  }

  public void drawString (String str, int x, int y)
  {
  }

  public void fillArc (int x, int y, int width, int height, 
		       int startAngle, int arcAngle)
  {
  }

  public void fillOval(int x, int y, int width, int height)
  {
  }

  public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints)
  {
  }

  private native void fillRectNative (int x, int y, int width, int height);
  public void fillRect (int x, int y, int width, int height)
  {
    fillRectNative (x + xOrigin, y + yOrigin, width, height);
  }

  public void fillRoundRect (int x, int y, int width, int height, 
			     int arcWidth, int arcHeight)
  {
  }

  public Shape getClip ()
  {
    return getClipBounds ();
  }

  public Rectangle getClipBounds ()
  {
    return null;
  }

  public Color getColor ()
  {
    return color;
  }

  public Font getFont ()
  {
    return null;
  }

  public FontMetrics getFontMetrics (Font f)
  {
    return null;
  }

  public void setClip (int x, int y, int width, int height)
  {
  }

  public void setClip (Shape clip)
  {
  }

  private native void setColorNative (int red, int green, int blue);
  public void setColor (Color c)
  {
    setColorNative (c.getRed (), c.getGreen (), c.getBlue ());
    color = new Color (c.getRGB ());
  }

  public void setFont (Font font)
  {
  }

  public void setPaintMode ()
  {
  }

  public void setXORMode (Color c1)
  {
  }

  public void translate (int x, int y)
  {
    xOrigin += x;
    yOrigin += y;
  }
}
