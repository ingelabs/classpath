package javax.swing;

import java.awt.*;
import javax.swing.plaf.*;

public class JPanel extends JComponent
{
    JPanel()
    {
	this(new FlowLayout(),
	     true);
    }
    
    JPanel(boolean double_buffered)
    {
	this(new FlowLayout(),
	     double_buffered);
    }
    
    JPanel(LayoutManager layout)
    {
	this(layout,
	     true);
    }
    
    
    JPanel(LayoutManager layout,
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

    String getUIClassID()
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


  AccessibleContext getAccessibleContext()
    {
	return null;
    }
    
   protected  String paramString()
    {
	return "JPanel";
    }
}






