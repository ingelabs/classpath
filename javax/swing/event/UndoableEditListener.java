package javax.swing.event;

import java.util.*;
import javax.swing.text.*;


public interface UndoableEditListener extends EventListener
{
    void undoableEditHappened(UndoableEditEvent e);
}
 
