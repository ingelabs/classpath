package javax.swing.border;

import java.awt.*;

public class MatteBorder extends EmptyBorder
{
    Color c;

    public MatteBorder()
    {
    }

    
    public MatteBorder(int top,
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

