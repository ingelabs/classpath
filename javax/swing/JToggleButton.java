package javax.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;

public class JToggleButton extends AbstractButton
{
    JToggleButton()
    {
	this(null, null);
    }
    JToggleButton(Action a)
    {
	this();
	setAction(a);
    }

    JToggleButton(Icon icon)
    { 
	this(null, icon);
    }    
  
    JToggleButton(String text)
    {
	this(text, null);
    }
      
    JToggleButton(String text, Icon icon)
    {
	this(text, icon, false);
    }

    JToggleButton (String text, Icon icon, boolean selected) 
    {
	super(text, icon);

        // Create the model
        setModel(new ToggleButtonModel(this));
	
        model.setSelected(selected);
    }


    
    AccessibleContext getAccessibleContext()
    {
	//Gets the AccessibleContext associated with this JToggleButton. 
	return null;
    }
  
    String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JToggleButton";
    }
  
    protected  String paramString()
    {
	return "JToggleButton";
    }
  
  
    void updateUI()
    {	
	ButtonUI b = (ButtonUI)UIManager.getUI(this);
	setUI(b);
    }
}



