package javax.swing;

import java.util.*;
import java.awt.event.*;
import javax.swing.event.*;

public 
class ToggleButtonModel extends DefaultButtonModel
{
    ToggleButtonModel(JComponent c)
    {
	super(c);
    }

    public void setPressed(boolean b)  
    {
	if (! isEnabled())
	    return;
	
	if (! b)
	    {
		return;
	    }
	
	setSelected(b);
    }
}
