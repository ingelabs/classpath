package javax.swing.event;

import java.util.*;

public interface DocumentListener extends EventListener
{
    void changedUpdate(DocumentEvent e);
    void insertUpdate(DocumentEvent e);
    void removeUpdate(DocumentEvent e);
}
 
