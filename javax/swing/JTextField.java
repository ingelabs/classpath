package javax.swing;

import java.awt.event.*;
import java.util.*;


public class JTextField extends JEditorPane
{
    Vector actions = new Vector();

    JTextField(int a)
    {
    }

    void addActionListener(ActionListener l)
    {
	actions.addElement(l);
    }

    void removeActionListener(ActionListener l)
    {
	actions.removeElement(l);
    }

    void selectAll()
    {
    }
}
