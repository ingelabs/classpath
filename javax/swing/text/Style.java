package javax.swing.text;

import javax.swing.event.*;

public interface Style
{
    void addChangeListener(ChangeListener l);
    String getName();
    void removeChangeListener(ChangeListener l);
}
