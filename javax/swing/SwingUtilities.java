package javax.swing;

import java.awt.*;


public class SwingUtilities
{
    static JRootPane getRootPane(Component a)
    {
	if (a instanceof JRootPane)
	    return (JRootPane) a;
	
	a = a.getParent();

	if (a != null)
	    {
		return getRootPane(a);
	    }

	return null;
    }

    static void updateComponentTreeUI(JFrame comp)
    {
    }

    static public String layoutCompoundLabel(JComponent c, 
					     FontMetrics fm,
					     String text,
					     Icon i,
					     int vert_a, 
					     int hor_i, 
					     int vert_text_pos,
					     int hor_text_pos, 
					     Rectangle vr,
					     Rectangle ir, 
					     Rectangle tr,
					     int gap)
    {
	// view rect 'vr' already ok, 
	// we need to compute ir (icon rect) and tr (text-rect)
	
	int next_x = 0;//vr.x;
	int next_y = 0;//vr.y;
	
	ir.height = ir.width = ir.y = ir.x = 0;

	if (i != null)
	    {
		ir.x = vr.x;
		ir.y = vr.y;
		ir.width = i.getIconWidth();
		ir.height = i.getIconWidth();


		next_x += gap + i.getIconWidth();
		next_y += gap + i.getIconHeight();
	    }
	
	tr.x = next_x;
	tr.y = vr.y + (vr.height/2);

	tr.width  = fm.stringWidth(text);
	tr.height = fm.getHeight() +  fm.getAscent()/2;

	return text;
    }
}









