package javax.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;



public class JCheckBox extends JToggleButton
{
    JCheckBox()
    {
	this(null, null);
    }
    JCheckBox(Action a)
    {
	this();
	setAction(a);
    }

    JCheckBox(Icon icon)
    { 
	this(null, icon);
    }    
  
    JCheckBox(String text)
    {
	this(text, null);
    }
      
    JCheckBox(String text, Icon icon)
    {
	super(text, icon);
    }

    
    AccessibleContext getAccessibleContext()
    {
	//Gets the AccessibleContext associated with this JCheckBox. 
	return null;
    }
  
    String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JCheckBox";
    }
  
    protected  String paramString()
    {
	return "JCheckBox";
    }
}



