package javax.swing.text;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public abstract class View implements SwingConstants
{
    static int BadBreakWeight;    
    static int ExcellentBreakWeight;
    static int ForcedBreakWeight;
    static int GoodBreakWeight;

    public final static int X_AXIS = 0;
    public final static int Y_AXIS = 1;
    
    float width, height;
    Element elt;
    View parent;

    /** 
     * this vector contains the views ordered at offsets...
     */
    Vector v = new Vector();


    public View(Element elem)
    {
	elt = elem;
    }

    public int getViewCount() 
    {
	return v.size();
    }

    public View getView(int a)
    {
	return (View) v.get(a);
    }
    
    public void remove(int i)
    {
	v.removeElementAt(i);
    }
    
    public void insert(int off, View view)
    {
	v.insertElementAt(view, off);	
    }	   
    
    public void append(View view)
    {
	v.addElement(view);
    }
	
    public void paint(Graphics g, Shape allocation)
    {
	System.out.println("view.paint() !!!!");
    }

    public void setParent(View a)
    {
	parent = a;
    }
    
    public View getParent()
    {
	return parent;
    }
    
    public void setSize(int w, int h)
    {
	width  = w;
	height = h;
    }

    public Document getDocument()
    {
	return getElement().getDocument();
    }
    
    public Element getElement()
    {
        return elt;
    }

    public float getPreferredSpan(int a)
    {
	switch (a)
	    {
	    case X_AXIS:  return width;
	    case Y_AXIS:  return height;
	    default:
		{
		    System.err.println("I sure wish Java had enums !!! ");
		    return 0;
		}
	    }
    }
}

