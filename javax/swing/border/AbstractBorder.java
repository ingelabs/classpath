package javax.swing.border;

import java.io.*;
import java.awt.*; 

public abstract class AbstractBorder implements Border, Serializable
{
    AbstractBorder()
    {
    }

    public void paintBorder(Component c,
			    Graphics g,
			    int x,
			    int y,
			    int width,
			    int height) 
    {
	System.out.println("HMMMMM, abstract-border.paintBorder");
    }


    public Insets getBorderInsets(Component c, Insets insets) 
    {
	if (insets == null)
	    insets = new Insets(0,0,0,0);
        
	insets.left = insets.top = insets.right = insets.bottom = 5;

        return insets;
    }

    public Insets getBorderInsets(Component c)
    {
        return getBorderInsets(c, new Insets(0,0,0,0));
    }


    public boolean isBorderOpaque() 
    {   return false;   }

    public Rectangle getInteriorRectangle(Component c,
					  int x,
					  int y,
					  int width, 
					  int height) 
    {
        return getInteriorRectangle(c,
				    this,
				    x,
				    y,
				    width,
				    height);
    }

        
    public static Rectangle getInteriorRectangle(Component c,
						 Border b,
						 int x,
						 int y,
						 int width,
						 int height)
    {
	if(b != null)
	    {
		Insets insets = b.getBorderInsets(c);
		
		int w = insets.right - insets.left;
		int h = insets.top   - insets.bottom;

		return new Rectangle(x + insets.left,
				     y + insets.top,
				     width - w,
				     height - h);
	    }
        else
            {
		return new Rectangle(x,
				     y,
				     width,
				     height);
	    }
    }
}

