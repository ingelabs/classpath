package gnu.java.awt.peer.gtk;

import java.awt.*;
import java.awt.image.*;

public class GdkGraphics extends Graphics
{
  private final int native_state = java.lang.System.identityHashCode (this);

  Color color, xorColor;
  GtkComponentPeer component;
  Font font;
  Rectangle clip;

  static final int GDK_COPY = 0, GDK_XOR = 2;

  native int[] initState (GtkComponentPeer component);
  native void initState (int width, int height);

  GdkGraphics (int width, int height)
  {
    initState (width, height);
    color = Color.black;
    clip = new Rectangle (0, 0, width, height);
    font = new Font ("Dialog", Font.PLAIN, 10);
  }

  GdkGraphics (GtkComponentPeer component)
  {
    this.component = component;
    int rgb[] = initState (component);
    color = new Color (rgb[0], rgb[1], rgb[2]);
    font = new Font ("Dialog", Font.PLAIN, 10);
    Dimension d = component.awtComponent.getSize ();
    clip = new Rectangle (0, 0, d.width, d.height);
  }

  public native void clearRect (int x, int y, int width, int height);

  native void clipRect (int x1, int y1, int width1, int height1,
			int x2, int y2, int width2, int height2);

  public void clipRect (int x, int y, int width, int height)
  {
    clipRect (clip.x, clip.y, clip.width, clip.height,
	      x, y, width, height);

    clip.x = x;
    clip.y = y;
    clip.width = width;
    clip.height = height;
  }

  native public void copyArea (int x, int y, int width, int height, 
			       int dx, int dy);

  public Graphics create ()
  {
    System.out.println ("create new graphics");
    return new GdkGraphics (component);
  }

  native public void dispose ();

  native void copyPixmap (Graphics g, int x, int y, int width, int height);
  public boolean drawImage (Image img, int x, int y, 
			    Color bgcolor, ImageObserver observer)
  {
    if (img instanceof GtkOffScreenImage)
      {
	copyPixmap (img.getGraphics (), 
		    x, y, img.getWidth (null), img.getHeight (null));
	return true;
      }

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

  native public void drawLine (int x1, int y1, int x2, int y2);

  native public void drawArc (int x, int y, int width, int height,
			      int startAngle, int arcAngle);
  native public void fillArc (int x, int y, int width, int height, 
			      int startAngle, int arcAngle);
  native public void drawOval(int x, int y, int width, int height);
  native public void fillOval(int x, int y, int width, int height);

  native public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints);
  native public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints);

  native public void drawPolyline(int[] xPoints, int[] yPoints, int nPoints);

  native public void drawRect(int x, int y, int width, int height);
  native public void fillRect (int x, int y, int width, int height);

  native void drawString (String str, int x, int y, String fname, int size);
  public void drawString (String str, int x, int y)
  {
    drawString (str, x, y,
		((GtkFontPeer)font.getPeer ()).getXLFD (), font.getSize ());
  }

  public void drawRoundRect(int x, int y, int width, int height, 
			    int arcWidth, int arcHeight)
  {
    System.out.println ("drawRoundRect called [UNIMPLEMENTED]");
  }

  public void fillRoundRect (int x, int y, int width, int height, 
			     int arcWidth, int arcHeight)
  {
    System.out.println ("fillRoundRect called [UNIMPLEMENTED]");
  }

  public Shape getClip ()
  {
    return getClipBounds ();
  }

  public Rectangle getClipBounds ()
  {
    return new Rectangle (clip); // Rectangles are not immutable
  }

  public Color getColor ()
  {
    return color;
  }

  public Font getFont ()
  {
    return font;
  }

  public FontMetrics getFontMetrics (Font font)
  {
    return new GdkFontMetrics (font);
  }

  native void setClipRectangle (int x, int y, int width, int height);

  public void setClip (int x, int y, int width, int height)
  {
    clip.x = x;
    clip.y = y;
    clip.width = width;
    clip.height = height;
    
    setClipRectangle (x, y, width, height);
  }

  public void setClip (Rectangle clip)
  {
    setClip (clip.x, clip.y, clip.width, clip.height);
  }

  public void setClip (Shape clip)
  {
    setClip (clip.getBounds ());
  }

  native private void setFGColor (int red, int green, int blue);

  public void setColor (Color c)
  {
    color = c;

    if (xorColor == null) /* paint mode */
      setFGColor (color.getRed (), color.getGreen (), color.getBlue ());
    else		  /* xor mode */
      setFGColor (color.getRed   () ^ xorColor.getRed (),
		  color.getGreen () ^ xorColor.getGreen (),
		  color.getBlue  () ^ xorColor.getBlue ());
  }

  public void setFont (Font font)
  {
    this.font = font;
  }

  native void setFunction (int gdk_func);

  public void setPaintMode ()
  {
    xorColor = null;

    setFunction (GDK_COPY);
    setFGColor (color.getRed (), color.getGreen (), color.getBlue ());
  }

  public void setXORMode (Color c)
  {
    xorColor = c;

    setFunction (GDK_XOR);
    setFGColor (color.getRed   () ^ xorColor.getRed (),
		color.getGreen () ^ xorColor.getGreen (),
		color.getBlue  () ^ xorColor.getBlue ());
  }

  native public void translate (int x, int y);
}
