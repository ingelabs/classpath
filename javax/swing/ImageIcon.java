package javax.swing;

import java.awt.*;
import java.awt.image.*;



public class ImageIcon implements Icon
{
    Image image;
    String file, descr;
    Component observer;

  public ImageIcon(String s)
    {
	this(s, "");
    }

  public ImageIcon(String file,
	      String descr)
    {
        this.file = file;
        this.descr = descr;

        image = Toolkit.getDefaultToolkit().getImage(file);
        if (image == null) {
            return;
        }
        //loadImage(image);
    }

    // not in SUN's spec !!!
    public void setParent(Component p)
    {
	observer = p;
    }

    public Image getImage() 
    {  return image;    }

    public String getDescription() 
    {  return descr;    }
    public void setDescription(String description) 
    {  this.descr = description;    }

    public int getIconHeight()
    {	return image.getHeight(observer);    }
    public int getIconWidth()
    {	return image.getWidth(observer);    }

    public void paintIcon(Component c, 
			  Graphics g,
			  int x, 
			  int y)
    {
	g.drawImage(image, x, y, observer);
    }
}
