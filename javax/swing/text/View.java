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

    final static int X_AXIS = 0;
    final static int Y_AXIS = 1;
    
    float width, height;
    Element elt;
    View parent;


    Vector v = new Vector();

    int getViewCount() 
    {
	return v.size();
    }

    View getView(int a)
    {
	return (View) v.get(a);
    }
    
    void remove(int i)
    {
	v.removeElementAt(i);
    }
    
    void insert(int off, View view)
    {
	v.insertElementAt(view, off);	
    }	    
    void append(View view)
    {
	v.addElement(view);
    }
	
    void paint(Graphics g, Shape allocation)
    {
	System.out.println("view.paint() !!!!");
    }

    void setParent(View a)
    {
	parent = a;
    }
    View getParent()
    {
	return parent;
    }
    
    void setSize(int w, int h)
    {
	width  = w;
	height = h;
    }

    View(Element elem)
    {
	elt = elem;
    }

    Document getDocument()
    {
	return getElement().getDocument();
    }
    
    Element getElement()
    {
        return elt;
    }

    float getPreferredSpan(int a)
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

