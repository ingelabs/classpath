package javax.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;
import javax.accessibility.*;

public class JRadioButton extends JToggleButton
{
    public JRadioButton()
    {
	this(null, null);
    }
    public JRadioButton(Action a)
    {
	this();
	setAction(a);
    }

    public JRadioButton(Icon icon)
    { 
	this(null, icon);
    }    
  
    public JRadioButton(String text)
    {
	this(text, null);
    }
      
    public JRadioButton(String text, Icon icon)
    {
	super(text, icon);
    }

    
    public AccessibleContext getAccessibleContext()
    {
	//Gets the AccessibleContext associated with this JRadioButton. 
	return null;
    }
  
    public String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JRadioButton";
    }
  
    protected  String paramString()
    {
	return "JRadioButton";
    }
}



