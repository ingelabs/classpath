package javax.swing;

import javax.swing.event.*;
import java.util.*;

public class DefaultListSelectionModel implements ListSelectionModel
{
    int mode = SINGLE_SELECTION;

    Vector sel = new Vector();

    Vector listeners;

    Vector get_listeners()
    {
	if (listeners == null)
	    listeners = new Vector();
	return listeners;
    }
    

    public void addListSelectionListener(ListSelectionListener listener)
    {
	get_listeners().addElement(listener);
    }

    public void removeListSelectionListener(ListSelectionListener listener)
    {
	get_listeners().removeElement(listener);
    }
    
    class Range
    {
	int i0, i1;

	Range(int a, int b)
	{
	    if (a > b)
		{
		    i0 = b;
		    i1 = a;
		}
	    else
		{
		    i0 = a; 
		    i1 = b;
		}
	}
    }

    
    public int getMinSelectionIndex()
    {
	if (isSelectionEmpty())
	    return -1;
	
	boolean first = true;
	int min = -1;
	for (int i=0;i<sel.size();i++)
	    {
		Range r = (Range) sel.get(i);

		if (first)
		    {
			min = r.i0;
			first = false;
		    }	
		else
		    {
			if (r.i0 > min)
			    {
				min = r.i0;
			    }
		    }
	    }
	return min;
    }

    public int getMaxSelectionIndex()
    {
	if (isSelectionEmpty())
	    return -1;

	boolean first = true;
	int max = -1;
	for (int i=1;i<sel.size();i++)
	    {
		Range r = (Range) sel.get(i);
		
		if (first)
		    {
			max = r.i1;
		    }
		else
		    {
			if (r.i1 > max)
			    {
				max = r.i1;
			    }
		    }
	    }
	return max;
    }

    public boolean isSelectedIndex(int a)
    {
	for (int i=0;i<sel.size();i++)
	    {
		Range r = (Range) sel.get(i);
		if (r.i0 <= a &&
		    r.i1 >= a)
		    {
			return true;
		    }
	    }
	return false;
    }


    public int getSelectionMode()
    { return mode; }
    public void setSelectionMode(int a)
    {	mode = a;    }

    boolean isSelectionEmpty() 
    {
	return sel.size() == 0;
    }

    public void clearSelection()
    {
	sel.removeAllElements();
    }

    public void setSelectionInterval(int index0, int index1) 
    {
	if (mode == SINGLE_SELECTION)
	    {
		sel.removeAllElements();
	    }

	sel.addElement(new Range(index0, index1));
    }
}
