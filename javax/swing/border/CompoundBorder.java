package javax.swing.border;

import java.awt.*;

public class CompoundBorder extends AbstractBorder
{
    
    public Insets getBorderInsets(Component  c,
				  Insets s)
    {
	if (s == null)
	    s = new Insets();
	
	s.left = s.right = s.top = s.bottom = 5;
	
	return s;
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

