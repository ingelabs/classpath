package javax.swing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.plaf.*;


/**
 * An instance of JButton can be added to a panel, frame etc
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */


public class JButton extends AbstractButton implements Accessible 
{
    boolean def, is_def;

    
    public JButton()
    {
	this(null, null);
    }

    public JButton(Action a)
    {
	this();
	setAction(a);
    }

    public JButton(Icon icon)
    { 
	this(null, icon);
    }    
  
    public JButton(String text)
    {
	this(text, null);
    }
      
    public JButton(String text, Icon icon)
    {
	super(text, icon);
    }

    public Object[] getSelectedObjects()
    {
	return null;
    }
  
    protected  void configurePropertiesFromAction(Action a)
    {
	//Factory method which sets the AbstractButton's properties according to values from the Action instance. 
    }
    
    public AccessibleContext getAccessibleContext()
    {
	//Gets the AccessibleContext associated with this JButton. 
	return null;
    }
  
    public String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JButton";
    }
  
    public boolean isDefaultButton()
    {
	//Returns whether or not this button is the default button on the RootPane.  
	return is_def;
    }
  
    public boolean isDefaultCapable()
    {
	//Returns whether or not this button is capable of being the default button on the RootPane. 
	return def;
    }

    protected  String paramString()
    {
	return "JButton";
    }
    
    public void removeNotify()
    {
	//Overrides JComponent.removeNotify to check if this button is currently set as the default button on the RootPane, and if so, sets the RootPane's default button to null to ensure the RootPane doesn't hold onto an invalid button reference.  
    }
    
    public void setDefaultCapable(boolean defaultCapable)
    {	def = defaultCapable;    }
    
    public void updateUI()
    {
	ButtonUI b = (ButtonUI)UIManager.getUI(this);
	setUI(b);
    }
}



