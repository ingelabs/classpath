/* AbstractListModel.java -- 
   Copyright (C) 2002 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */

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









