package javax.swing.event;

import java.util.EventListener;


public interface ListDataListener extends EventListener
{
    void intervalAdded(ListDataEvent e);
    void intervalRemoved(ListDataEvent e);
    void contentsChanged(ListDataEvent e);    
}
