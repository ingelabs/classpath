package javax.swing.event;


import java.util.*;


public class ListSelectionEvent extends EventObject
{
    int first, last;
    boolean adj;

    ListSelectionEvent(Object source,
		       int firstIndex, 
		       int lastIndex,
		       boolean isAdjusting)
    {
	super(source);

	first = firstIndex;
	last = lastIndex;
	adj = isAdjusting;
    }

    boolean getValueIsAdjusting()
    {
	return adj;
    }
}
