package javax.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;

public class JToggleButton extends AbstractButton
{
    public JToggleButton()
    {
	this(null, null);
    }
    public JToggleButton(Action a)
    {
	this();
	setAction(a);
    }

    public JToggleButton(Icon icon)
    { 
	this(null, icon);
    }    
  
    public JToggleButton(String text)
    {
	this(text, null);
    }
      
    public JToggleButton(String text, Icon icon)
    {
	this(text, icon, false);
    }

    public JToggleButton (String text, Icon icon, boolean selected) 
    {
	super(text, icon);

        // Create the model
        setModel(new ToggleButtonModel(this));
	
        model.setSelected(selected);
    }


    
    public AccessibleContext getAccessibleContext()
    {
	//Gets the AccessibleContext associated with this JToggleButton. 
	return null;
    }
  
    public String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JToggleButton";
    }
  
    protected  String paramString()
    {
	return "JToggleButton";
    }
  
  
    public void updateUI()
    {	
	ButtonUI b = (ButtonUI)UIManager.getUI(this);
	setUI(b);
    }
}



