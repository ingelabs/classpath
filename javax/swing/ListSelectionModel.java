package javax.swing;


import javax.swing.event.*;

public interface ListSelectionModel
{
    final static int SINGLE_SELECTION = 0;
    final static int SINGLE_INTERVAL_SELECTION = 1;
    final static int MULTIPLE_INTERVAL_SELECTION = 1;

    void setSelectionMode(int a);
    int getSelectionMode();

    void clearSelection();
    
    int getMinSelectionIndex();
    int getMaxSelectionIndex();
    boolean isSelectedIndex(int a);

    void setSelectionInterval(int index0, int index1);

    

    void addListSelectionListener(ListSelectionListener listener);
    void removeListSelectionListener(ListSelectionListener listener);    

}
