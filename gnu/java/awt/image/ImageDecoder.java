package gnu.java.awt.image;

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;

public abstract class ImageDecoder implements ImageProducer 
{
  Vector consumers;

  public ImageDecoder ()
  {
    consumers = new Vector ();
  }

  public void addConsumer (ImageConsumer ic) 
  {
    consumers.addElement (ic);
  }

  public boolean isConsumer (ImageConsumer ic)
  {
    return consumers.contains (ic);
  }
  
  public void removeConsumer (ImageConsumer ic)
  {
    consumers.removeElement (ic);
  }

  public void startProduction (ImageConsumer ic)
  {
    addConsumer (ic);
    Vector list = (Vector) consumers.clone ();
    try 
      {
	produce (list);
      } 
    catch (IOException e)
      {
	for (int i = 0; i < list.size (); i++)
	  {
	    ImageConsumer ic2 = (ImageConsumer) list.elementAt (i);
	    ic2.imageComplete (ImageConsumer.IMAGEERROR);
	  }
      }
  }

  public void requestTopDownLeftRightResend (ImageConsumer ic) 
  { 
  }

  public abstract void produce (Vector v) throws IOException;
}
