package javax.swing.border;

import java.awt.*;


public class EmptyBorder extends AbstractBorder
{
    
    protected int l,r,b,t;

    public EmptyBorder()
    {
    }

    public EmptyBorder(int left,
		int right,
		int top, 
		int bottom)
    {
	this.l = left;
	this.r = r;
	this.t = t;
	this.b = b;
    }


    public Insets getBorderInsets(Component  c,
				  Insets s)
    {
	if (s == null)
	    s = new Insets(0,0,0,0);

	s.left = l;
	s.right = r;
	s.top = t;
	s.bottom = b;
	
	return s;
    }
    
    public boolean isBorderOpaque()
    {
	return false;
    }

    
    public void paintBorder(Component  c, 
			    Graphics  g,
			    int  x, int  y, int  width, int  height)
    {
    }
}

