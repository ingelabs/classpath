package javax.swing;

import java.awt.*;

public class BoxLayout implements LayoutManager2
{
    GridLayout      gridbag;
    
    final static int X_AXIS = 0;
    final static int Y_AXIS = 1;

    int way = X_AXIS;

    BoxLayout(JComponent p,
	      int way)
    {
	int width = 0;
	int height = 0;

	this.way = way;

	if (way == X_AXIS)
	    {
		width = 1;
	    }
	else
	    {
		height = 1;
	    }
	

	gridbag = new GridLayout(width, height);
    }
    
    BoxLayout(int way)
    {
	this(null,way);
    }
    

    public void addLayoutComponent(String name, Component comp)
    {
	if (way == X_AXIS)
	    {
		gridbag.setColumns( gridbag.getColumns() + 1);
	    }
	else
	    {
		gridbag.setRows( gridbag.getRows() + 1);
	    }
    }

    public void removeLayoutComponent(Component comp)
    {
	gridbag.removeLayoutComponent(comp);
	if (way == X_AXIS)
	    {
		gridbag.setColumns( gridbag.getColumns() - 1);
	    }
	else
	    {
		gridbag.setRows( gridbag.getRows() - 1);
	    }
    }

    public Dimension preferredLayoutSize(Container parent)
    {
	return gridbag.preferredLayoutSize(parent);
    }

    public Dimension minimumLayoutSize(Container parent)
    {
	return gridbag.minimumLayoutSize(parent);
    }

    public void layoutContainer(Container parent)
    {	
	gridbag.layoutContainer(parent);
    }

    public void addLayoutComponent ( Component child, Object constraints )
    {
	addLayoutComponent("", child);
    }

    public float getLayoutAlignmentX ( Container parent )
    {
	return 0;
    }

    public float getLayoutAlignmentY ( Container parent )
    {
	return 0;
    }

    public void invalidateLayout ( Container parent )
    {
    }

    public Dimension maximumLayoutSize ( Container parent )
    {
	return preferredLayoutSize(parent);
    }
}



