package javax.swing;

import javax.swing.event.*;
import java.util.*;

// fire up change events before or after
// the element removed/added ?

public class AbstractListModel implements ListModel
{
    Vector elts      = new Vector();
    
    public int getSize()
    {
	return elts.size();
    }
    
    public Object getElementAt(int index)
    {
	return elts.elementAt(index);
    }
    
    public void addElement(Object a)
    {
	int q = elts.size();
	elts.addElement(a);
	fire_up_change_events(ListDataEvent.INTERVAL_ADDED,
			      q,
			      q);
    }

    public void insertElementAt(Object a, int index)
    {
	elts.insertElementAt(a, index);
	fire_up_change_events(ListDataEvent.INTERVAL_ADDED,
			      index, index);
    }
    
    void remove(int a)
    {
	elts.removeElementAt(a);
	fire_up_change_events(ListDataEvent.INTERVAL_REMOVED,
			      a, a);
    }

    /*********************************************
     *
     *  handle data change events
     *
     ************/

    Vector listeners = new Vector();

    void fire_up_change_events(int type,
			       int lower,
			       int upper)
    {
	for (int i=0; i<listeners.size(); i++)
	    {
		ListDataListener l = (ListDataListener) listeners.get(i);

		l.contentsChanged(new ListDataEvent(this,
						    type,
						    lower,
						    upper));
	    }
    }

    public void addListDataListener(ListDataListener l)
    {
	listeners.addElement(l);
    }

    public void removeListDataListener(ListDataListener l)
    {
	listeners.removeElement(l);
    }
}









