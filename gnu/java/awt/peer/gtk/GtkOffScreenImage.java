package gnu.java.awt.peer.gtk;

import java.awt.*;
import java.awt.image.*;

public class GtkOffScreenImage extends Image
{
  int width, height;
  ImageProducer source;
  Graphics g;
  
  public GtkOffScreenImage (ImageProducer source, Graphics g,
			    int width, int height)
  {
    this.width = width;
    this.height = height;

    this.source = source;
    this.g = g;
  }

  public int getWidth (ImageObserver observer)
  {
    return width;
  }

  public int getHeight (ImageObserver observer)
  {
    return height;
  }

  public ImageProducer getSource ()
  {
    return source;
  }

  public Graphics getGraphics ()
  {
    return g;
  }

  public Object getProperty (String name, ImageObserver observer)
  {
    return Image.UndefinedProperty;
  }

  public void flush ()
  {
  }
}
