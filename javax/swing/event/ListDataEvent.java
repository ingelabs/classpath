package javax.swing.event;


import java.util.EventObject;


public class ListDataEvent extends EventObject 
{
    // Ronald: what does constents change mean ?
    // the size of the list of the contents of any
    // of its list items?
    
    public static final int CONTENTS_CHANGED = 0;
    public static final int INTERVAL_ADDED   = 1;
    public static final int INTERVAL_REMOVED = 2;
    
    int type, index0, index1;
    
    public int getType() { return type; }
    public int getIndex0() { return index0; }
    public int getIndex1() { return index1; }

    public ListDataEvent(Object source, int type, int index0, int index1)
    {
	super(source);

        this.type = type;
        this.index0 = index0;
        this.index1 = index1;
    }
}
