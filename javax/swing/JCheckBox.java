package javax.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;
import javax.accessibility.*;

/**
 * An instance of JCheckbox can be added to a panel, frame etc
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */



public class JCheckBox extends JToggleButton
{
    public JCheckBox()
    {
	this(null, null);
    }
    public JCheckBox(Action a)
    {
	this();
	setAction(a);
    }

    public JCheckBox(Icon icon)
    { 
	this(null, icon);
    }    
  
    public JCheckBox(String text)
    {
	this(text, null);
    }
      
    public JCheckBox(String text, Icon icon)
    {
	super(text, icon);
    }

    
    public AccessibleContext getAccessibleContext()
    {
	//Gets the AccessibleContext associated with this JCheckBox. 
	return null;
    }
  
    public String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JCheckBox";
    }
  
    protected  String paramString()
    {
	return "JCheckBox";
    }
}



