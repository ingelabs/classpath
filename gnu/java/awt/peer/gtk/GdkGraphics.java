package gnu.java.awt.peer.gtk;

import java.awt.*;
import java.awt.image.*;

public class GdkGraphics extends Graphics
{
  private final int native_state = java.lang.System.identityHashCode (this);

  int xOrigin = 0, yOrigin = 0;
  Color color, xorColor, xorFGColor;
  GtkComponentPeer component;
  Font font;
  boolean paintMode;

  static final int GDK_COPY = 0, GDK_XOR = 2;

  native int[] initState (GtkComponentPeer component);

  GdkGraphics (GtkComponentPeer component)
  {
    this.component = component;
    int rgb[] = initState (component);
    color = new Color (rgb[0], rgb[1], rgb[2]);
    font = new Font ("Dialog", Font.PLAIN, 10);
    paintMode = true;
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
    return new GdkGraphics (component);
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
    GtkImage image = (GtkImage) img;
    new GtkImagePainter (image, this, x, y, bgcolor);
    return image.isLoaded ();
  }

  public boolean drawImage (Image img, int x, int y, ImageObserver observer)
  {
    return drawImage (img, x, y, component.getBackground(), observer);
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

  native void drawString (String str, int x, int y, String fname, int size);
  public void drawString (String str, int x, int y)
  {
    drawString (str, x + xOrigin, y + yOrigin, 
		((GtkFontPeer)font.getPeer ()).getXLFD (), font.getSize ());
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
    return font;
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

  private native void setFGColor (int red, int green, int blue);
  public void setColor (Color c)
  {
    color = new Color (c.getRGB ());

    if (paintMode)
      setFGColor (color.getRed (), color.getGreen (), color.getBlue ());
    else
      setXORMode (xorColor);
  }

  public void setFont (Font font)
  {
    this.font = font;
  }

  native void setFunction (int gdk_func);

  public void setPaintMode ()
  {
    paintMode = true;

    setFunction (GDK_COPY);
    setFGColor (color.getRed (), color.getGreen (), color.getBlue ());
  }

  public void setXORMode (Color c)
  {
    paintMode = false;

    xorColor = new Color (c.getRGB ());
    xorFGColor = new Color (color.getRed   () ^ xorColor.getRed (),
			    color.getGreen () ^ xorColor.getGreen (),
			    color.getBlue  () ^ xorColor.getBlue ());

    setFunction (GDK_XOR);
    setFGColor (xorFGColor.getRed (), 
		xorFGColor.getGreen (), 
		xorFGColor.getBlue ());
  }

  public void translate (int x, int y)
  {
    xOrigin += x;
    yOrigin += y;
  }
}
