package gnu.java.awt.peer.gtk;

import java.awt.*;
import java.awt.image.*;
import java.util.*;

public class GtkImagePainter implements Runnable, ImageConsumer
{
  GtkImage image;
  GdkGraphics gc;
  int startX, startY;
  int redBG;
  int greenBG;
  int blueBG;

  public
  GtkImagePainter (GtkImage image, GdkGraphics gc, int x, int y, Color bgcolor)
  {
    this.image = image;
    this.gc = (GdkGraphics) gc.create ();
    startX = x;
    startY = y;
    redBG = bgcolor.getRed ();
    greenBG = bgcolor.getGreen ();
    blueBG = bgcolor.getBlue ();
    
    new Thread (this).start ();
  }

  public void
  run ()
  {
    image.startProduction (this);
    gc.dispose ();
  }

  /* Convert pixel data into a format that gdkrgb can understand */
  static int[] 
  convertPixels (int[] pixels, ColorModel model)
  {
    if (model instanceof DirectColorModel)
      return pixels;
    
    int ret[] = new int[pixels.length];

    for (int i = 0; i < pixels.length; i++)
      ret[i] = model.getRGB (pixels[i]);

    return ret;
  }

  static int[]
  convertPixels (byte[] pixels, ColorModel model)
  {
    int ret[] = new int[pixels.length];

    if (model instanceof DirectColorModel)
      {
	for (int i = 0; i < pixels.length; i++)
	  ret[i] = pixels[i];
      }
    else
      {
	for (int i = 0; i < pixels.length; i++)
	  ret[i] = model.getRGB (pixels[i]);
      }

    return ret;
  }

  native void
  drawPixels (GdkGraphics gc, int bg_red, int bg_green, int bg_blue, 
	      int x, int y, int width, int height, int[] pixels, int offset, 
	      int scansize);

 
  public void 
  setPixels (int x, int y, int width, int height, ColorModel model,
	     int[] pixels, int offset, int scansize)
  {
    drawPixels (gc, redBG, greenBG, blueBG,
	        startX + x, startY + y,
		width, height, convertPixels (pixels, model), offset,
		scansize);
  }

  public void 
  setPixels (int x, int y, int width, int height, ColorModel model, 
	     byte[] pixels, int offset, int scansize)
  {
    drawPixels (gc, redBG, greenBG, blueBG,
		startX + x, startY + y,
		width, height, convertPixels (pixels, model), offset,
		scansize);
  }

  public void 
  setDimensions (int width, int height)
  {
  }

  public void 
  setProperties (Hashtable props)
  {
  }

  public void 
  setColorModel (ColorModel model)
  {
  }

  public void 
  setHints (int flags)
  {
  }

  public void 
  imageComplete (int status)
  {
  }
}
