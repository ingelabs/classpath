package javax.swing;

import java.awt.*;
import java.awt.event.*;

public class JRootPane extends JComponent
{
    //  The class used to obtain the accessible role for this object.
    static protected class AccessibleJRootPane
    {
    }
  
    //A custom layout manager  
    static protected class RootLayout extends BorderLayout
    {
      public Dimension preferredLayoutSize ( Container c )
	{	    
	  Dimension p = super.preferredLayoutSize(c);
	  System.out.println("              PREF-SIZE from RootLayout = " + p);
	  return p;
	}        
    }
  
    /***********************************************************/

  
  //The glass pane that overlays the menu bar and content pane, so it can intercept mouse movements and such.
    protected  Component glassPane;
  
    //The layered pane that manages the menu bar and content pane.
    protected  JLayeredPane layeredPane;
  
    // The menu bar.
    protected  JMenuBar menuBar;
  
    protected Container contentPane;

    /********************************************************/

    String getUIClassID()
    {	return "JPanel";    }

    
    void setJMenuBar(JMenuBar m)
    {  menuBar = m; }

    JMenuBar getJMenuBar()
    {  return menuBar; }
    

    public Container getContentPane()
    {
	if (contentPane == null)
	    {
		setContentPane(createContentPane());
	    }
	return contentPane;
    }

    public void setContentPane(Container p)
    {
	contentPane = p;    
	getLayeredPane().add(contentPane, 0);
    }

    protected void addImpl(Component comp,
			  Object constraints,
			  int index)
    {
	super.addImpl(comp, constraints, index);
	//System.out.println("don't do that !");
    } 

    public Component getGlassPane() 
    {
	if (glassPane == null)
	    setGlassPane(createGlassPane());
	return glassPane;    
    }

    public void setGlassPane(Component f)
    {
	if (glassPane != null)
	    remove(glassPane);

	glassPane = f; 

	glassPane.setVisible(false);
	add(glassPane, 0);
    }

    public JLayeredPane getLayeredPane() 
    {
	if (layeredPane == null)
	    setLayeredPane(createLayeredPane());
	return layeredPane;    
    }
    public void setLayeredPane(JLayeredPane f)
    {
	if (layeredPane != null)
	    remove(layeredPane);
	
	layeredPane = f; 
	add(f, -1);
    }
    

    /********************************************************/

    JRootPane()
    {
	setLayout(createRootLayout());
	
	getGlassPane();
	getLayeredPane();
	getContentPane();

	setDoubleBuffered(true);
	updateUI();
    }

    protected LayoutManager createRootLayout() {
        return new RootLayout();
    } 

    JComponent createContentPane()
    {
	JPanel p = new JPanel();
	p.setName(this.getName()+".contentPane");
	p.setLayout(new BorderLayout());
	//	p.setVisible(true);

	System.out.println("Created ContentPane: " + p);
	return p;
    }

    Component createGlassPane()
    {
	JPanel p = new JPanel();
	p.setName(this.getName()+".glassPane");
	p.setLayout(new BorderLayout());
	p.setVisible(false);
	
	System.out.println("created the glasspane: "+p);
	return p;
    }

    JLayeredPane createLayeredPane()
    {
	JLayeredPane l = new JLayeredPane();
	return l;
    }    
}











