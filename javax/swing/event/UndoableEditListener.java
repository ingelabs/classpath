package javax.swing.event;

import javax.swing.text.*;


public interface UndoableEditListener extends EventListener
{
    void undoableEditHappened(UndoableEditEvent e);
}
 
