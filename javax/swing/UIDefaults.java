package javax.swing;

import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;

import java.awt.*;

public class UIDefaults extends Hashtable
{
    ComponentUI getUI(JComponent a)
    {
	String pp = a.getUIClassID(); //a.getClass().getName();

	ComponentUI p = (ComponentUI) get(pp);
	if (p == null)
	    {
		System.out.println("failed to locate UI:" + pp);
	    }
	else
	    {
		System.out.println("UI Located ----> " + pp + " === " + p);
	    }
	return p;
    }

    public Insets getInsets(Object key) 
    {
	Insets s = (Insets) get(key);
	System.out.println("insets----> " + s);
        return s;
    }
}
