package javax.swing.border;

import java.awt.*;

public class BevelBorder extends EmptyBorder
{
    Color c;

    public BevelBorder()
    {
    }

    
    public BevelBorder(int top,
		int left,
		int bottom, 
		int right,
		Color color)
    {
        super(top, left, bottom, right);
        this.c = color;
    }

    public boolean isBorderOpaque()
    {
	return false;
    }
    
    public void paintBorder(Component  c,
			    Graphics  g, 
			    int  x, 
			    int  y, 
			    int  width, 
			    int  height)
    {
    }
}

