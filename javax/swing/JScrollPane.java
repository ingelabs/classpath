package javax.swing;

import java.awt.*;
import javax.swing.plaf.*;


public class JScrollPane extends JComponent
{
    protected JViewport  columnHeader;
    protected JViewport  rowHeader;

    protected Component  lowerLeft;
    protected Component  lowerRight;
    protected Component  upperLeft;
    protected Component  upperRight;

    protected JScrollBar horizontalScrollBar;
    protected int        horizontalScrollBarPolicy;
    protected JScrollBar verticalScrollBar;
    protected int        verticalScrollBarPolicy;

    protected JViewport  viewport;


    public JScrollPane() 
    {
	this(null, 0, 0);
    }
    
    public JScrollPane(Component view) 
    {
	this(view, 0, 0);
    }
    
    
    public JScrollPane(int vsbPolicy, int hsbPolicy) 
    {
	this(null, 0, 0);
    }

    public JScrollPane(Component view, int vsbPolicy, int hsbPolicy) 
    {
	setViewportView(view);
        setOpaque(true);
	updateUI();
    }

    public String getUIClassID()
    {
	//Returns a string that specifies the name of the L&F class that renders this component.  
	return "JScrollPane";
    }

    public JViewport getViewport()
    {
	return viewport;
    }

    public JViewport createViewport()
    {
	return new JViewport();
    }
    
    public void setViewport(JViewport v)
    {
	if (viewport != null)
	    remove(viewport);

	viewport = v;	
	
	add(v);
	
	revalidate();
	repaint();
    }
    
   public  void updateUI()
    {
	ScrollPaneUI b = (ScrollPaneUI)UIManager.getUI(this);
	setUI(b);
    }


    public void setViewportView(Component view)
    {
	if (getViewport() == null)
	    {
		setViewport(createViewport());
	    }
	
	if (view != null)
	    {
		getViewport().setView(view);
	    }
    }
}
