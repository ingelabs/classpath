package javax.swing;

import java.awt.*;
import javax.swing.plaf.*;


import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

/**
 * An instance of JPanel can be added to a panel, frame etc
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */



public class JPanel extends JComponent
{
    public JPanel()
    {
	this(new FlowLayout(),
	     true);
    }
    
    public JPanel(boolean double_buffered)
    {
	this(new FlowLayout(),
	     double_buffered);
    }
    
    public JPanel(LayoutManager layout)
    {
	this(layout,
	     true);
    }
    
    
    public JPanel(LayoutManager layout,
	   boolean isDoubleBuffered)
    {
	if (layout == null)
	    {
		System.err.println("NO LAYOUT SET !!!");
		layout = new FlowLayout();
	    }
	setLayout(layout); 
	setOpaque(true); 

	updateUI();	
    } 

    public String getUIClassID()
    {	return "JPanel";    }


    public void setUI(PanelUI ui) {
        super.setUI(ui);
    }
    
    public PanelUI getUI() {
        return (PanelUI)ui;
    }
    
    public void updateUI() {
        setUI((PanelUI)UIManager.getUI(this));
    }


    public AccessibleContext getAccessibleContext()
    {
	return null;
    }
    
   protected  String paramString()
    {
	return "JPanel";
    }
}






