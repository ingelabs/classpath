package javax.swing;

import java.io.*;
import java.util.*;
import javax.swing.event.*;


public class ButtonGroup implements Serializable 
{
    Vector v = new Vector();
    ButtonModel sel;
    
    public ButtonGroup() {}
    
    public void add(AbstractButton b) 
    {
	b.getModel().setGroup(this);
	v.addElement(b);
    }
    
    public void remove(AbstractButton b)
    {
	b.getModel().setGroup(null);
	v.removeElement(b);
    }


    public Enumeration getElements() {
        return v.elements();
    }

    public ButtonModel getSelection() {
        return sel;
    }

    AbstractButton FindButton(ButtonModel m)
    {
	for (int i=0;i<v.size();i++)
	    {
	    AbstractButton a = (AbstractButton) v.get(i);
	    if (a.getModel()== m)
	    {
		return a;
	    }
	}
	return null;
    }

    public void setSelected(ButtonModel m, boolean b)
    {
	if ((m == sel) &&
	    (b == true))
	    {
		// clicked on sam item twice.
		System.out.println("PRESSED TWICE:" + m + ", sel="+sel);
		return;
	    }	
	
	if (sel != null)
	    {

		System.out.println("DESELECTING: " + sel);
		sel.setSelected(!b);

		AbstractButton but = FindButton(sel);
		if (but != null)
		    {
			System.out.println("REPAINT-REQUIST: " + but.text);
			//but.revalidate();
			but.repaint();
		    }
	    }
	else
	    {
		System.out.println("NO SELECTION YET");
	    }
	
	sel = m;
    }
    
    public boolean isSelected(ButtonModel m) 
    {
        return (m == sel);
    }

    public int getButtonCount() 
    {
	return v.size();
    }

}









