package javax.swing.event;

import java.util.*;
import javax.swing.undo.*;


public class UndoableEditEvent extends EventObject
{ 
    UndoableEdit e;

    UndoableEditEvent(Object source, UndoableEdit edit)
    {
	super(source);
	e = edit;
    }
 
    UndoableEdit getEdit()
    {
	return e;
    }

}
