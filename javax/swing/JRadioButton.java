package javax.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;

public class JRadioButton extends JToggleButton
{
    JRadioButton()
    {
	this(null, null);
    }
    JRadioButton(Action a)
    {
	this();
	setAction(a);
    }

    JRadioButton(Icon icon)
    { 
	this(null, icon);
    }    
  
    JRadioButton(String text)
    {
	this(text, null);
    }
      
    JRadioButton(String text, Icon icon)
    {
	super(text, icon);
    }

    
    AccessibleContext getAccessibleContext()
    {
	//Gets the AccessibleContext associated with this JRadioButton. 
	return null;
    }
  
    String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JRadioButton";
    }
  
    protected  String paramString()
    {
	return "JRadioButton";
    }
}



