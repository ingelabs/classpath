package javax.swing;

import java.awt.event.*;
import java.util.*;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

public class JTextField extends JEditorPane
{
    Vector actions = new Vector();

    public JTextField(int a)
    {
    }

    public void addActionListener(ActionListener l)
    {
	actions.addElement(l);
    }

    public void removeActionListener(ActionListener l)
    {
	actions.removeElement(l);
    }

    public void selectAll()
    {
    }
}
