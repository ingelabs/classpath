package javax.swing;

import java.util.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.event.*;

public 
class DefaultButtonModel implements ButtonModel, java.io.Serializable
{
    Vector actions          = new Vector();

    Vector items    = new Vector();
    Vector changes  = new Vector();
    ButtonGroup group;
    JComponent comp;

    
    DefaultButtonModel(JComponent a)
    {
	comp = a;
    }


    public Object[] getSelectedObjects()
    {
	return null;
    }


    public void fireItemStateChanged(ItemEvent event)
    {
	for (int i=0;i<items.size();i++)
	    {
		ItemListener a = (ItemListener) items.get(i);
		a.itemStateChanged(event);
	    }
    }
    public void fireStateChanged(ChangeEvent event)
    {
	for (int i=0;i<changes.size();i++)
	    {
		ChangeListener a = (ChangeListener) changes.get(i);
		a.stateChanged(event);
	    }
    }
    public void fireActionPerformed(ActionEvent event)
    {
	for (int i=0;i<actions.size();i++)
	    {
		ActionListener a = (ActionListener) actions.get(i);
		a.actionPerformed(event);
	    }
    }

    boolean arm;
    public boolean isArmed()          { return arm; }
    public void setArmed(boolean b)   { arm = b; }

    boolean enabled = true;
    public boolean isEnabled()         { return enabled; }
    public void setEnabled(boolean b)  { enabled = b; }

    boolean pressed;
    public void setPressed(boolean b)  
    {
	pressed = b; 
    }
    public boolean isPressed()         { return pressed; }


    public void removeActionListener(ActionListener l) { actions.removeElement(l); }
    public void addActionListener(ActionListener l)    
    {	
	comp.eventMask |= AWTEvent.ACTION_EVENT_MASK;
	actions.addElement(l);    
    }

    public void addItemListener(ItemListener l)        { items.addElement(l); }
    public void removeItemListener(ItemListener l)     { items.removeElement(l); }

    public void addChangeListener(ChangeListener l)    { changes.addElement(l); }
    public void removeChangeListener(ChangeListener l) { changes.removeElement(l); }

    boolean roll;
    public void setRollover(boolean b) { roll = b; }
    public boolean isRollover()        { return roll; }

    int mne;  
    public int  getMnemonic()        { return mne; }
    public void setMnemonic(int key) { mne = key; }

    String com;
    public void setActionCommand(String s) { com = s; }
    public String getActionCommand()       { return com; }

    public void setGroup(ButtonGroup group)
    {
	this.group = group;
    }

    boolean sel;
    public void setSelected(boolean b) 
    { 
	if (group != null)
	    {
		if (b == true)
		    {
			System.out.println("selected button in group:"+this);
			group.setSelected(this, b);
			sel = true;
		    }
		else
		    {
			System.out.println("deselected button in group: " + this);
			sel = false;
		    }
	    } 
	else
	    {
		sel = b;
	    }
    }
    public boolean isSelected()        { return sel; }
}







