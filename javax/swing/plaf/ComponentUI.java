package javax.swing.plaf;

import java.awt.*;
import javax.swing.border.*;
import javax.swing.*;


public abstract class ComponentUI
{
    boolean contains(JComponent c, int x, int y)
    {
	return c.inside(x,y);
    }

    // this SHOULD thow an error:
    static ComponentUI createUI(JComponent c)
    {
	Exception e = new Exception("createUI from ComponentUI should never be called");
	e.printStackTrace();
	System.exit(1);
	return null;
    }

    Accessible getAccessibleChild(JComponent c, int i)
    {
	//Return the nth Accessible child of the object. 
	return null;
    }
  
    int getAccessibleChildrenCount(JComponent c)
    {
	//Returns the number of accessible children in the object. 
	return 0;
    }
  
    Dimension getMaximumSize(JComponent c)
    {
	return getPreferredSize(c);
    }

    Dimension getMinimumSize(JComponent c)
    {
	return getPreferredSize(c);
    }

    Dimension getPreferredSize(JComponent c)
    {
	return null;
    }

    void installUI(JComponent c)
    {
	String id = c.getUIClassID() + ".border";

	Border s = UIManager.getBorder( id );
	
	if (s != null)
	    {
		c.setBorder( s );
		//System.out.println("OK-INSTALL: " + this + ", ID=" + id + ",B="+s);
	    }
	else
	    {
		///System.out.println("FAIL-INSTALL: " + this + ", " + id);
	    }	
    }

    void paint(Graphics g, JComponent c)
    {
	//  System.out.println("UI-COMPONENT-> unimplemented paint: " + c + ", UI="+this);
    }

    void uninstallUI(JComponent c)
    {	
    }

    void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(),c.getHeight());
        }
        paint(g, c);
    }
         
}

