package javax.swing;

import javax.swing.event.*;

public interface ListModel
{    
  int getSize();
  Object getElementAt(int index);
  void addListDataListener(ListDataListener l);
  void removeListDataListener(ListDataListener l);
}
