package javax.swing;

import java.util.*;
import javax.swing.*;
import javax.swing.plaf.*;

import java.awt.*;


/**
 * UIDefaults is a database where all settings and interface bindings are
 * stored into. An PLAF implementation fills one of these (see for example
 * plaf/basic/BasicDefaults.java) with "JButton" -> new BasicButtonUI().
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */



public class UIDefaults extends Hashtable
{
    public ComponentUI getUI(JComponent a)
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
