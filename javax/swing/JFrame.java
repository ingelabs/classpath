package javax.swing;

import java.awt.*;
import java.awt.event.*;

import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;

/**
 * Unlike JComponent derivatives, JFrame inherits from
 * java.awt.Frame. But also lets a look-and-feel component to its work.
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */

public class JFrame extends Frame
{
    public final static int HIDE_ON_CLOSE        = 0;
    public final static int EXIT_ON_CLOSE        = 1;
    public final static int DISPOSE_ON_CLOSE     = 2;
    public final static int DO_NOTHING_ON_CLOSE  = 3;

    protected  AccessibleContext accessibleContext;

    private int close_action = EXIT_ON_CLOSE;    
    

    /***************************************************
     *
     *  initia
     *
     *
     *************/
    

    public JFrame()
    {
	super("JFrame");
	frameInit();
    }
  
    public JFrame(String title)
    {
	super(title);
	frameInit();
    }
  

    /***************************************************
     *
     *
     *  methods, this part is shared with JDialog, JFrame
     *
     *
     *************/

  
    private boolean checking;
    protected  JRootPane         rootPane;


    protected  void frameInit()
    {
      super.setLayout(new BorderLayout(1, 1));
      getRootPane(); // will do set/create
    }
  
  public Dimension getPreferredSize()
  {
    Dimension d = super.getPreferredSize();
    return d;
  }

    JMenuBar getJMenuBar()
    {    return getRootPane().getJMenuBar();   }
    
    void setJMenuBar(JMenuBar menubar)
    {    getRootPane().setJMenuBar(menubar); }
    

  public  void setLayout(LayoutManager manager)
  {    super.setLayout(manager);  }

    void setLayeredPane(JLayeredPane layeredPane) 
    {   getRootPane().setLayeredPane(layeredPane);   }
  
    JLayeredPane getLayeredPane()
    {   return getRootPane().getLayeredPane();     }
  
    JRootPane getRootPane()
    {
	if (rootPane == null)
	    setRootPane(createRootPane());
	return rootPane;          
    }

    void setRootPane(JRootPane root)
    {
	if (rootPane != null)
	    remove(rootPane);
	    
	rootPane = root; 
	add(rootPane, BorderLayout.CENTER);
    }

    JRootPane createRootPane()
    {   return new JRootPane();    }

    Container getContentPane()
    {    return getRootPane().getContentPane();     }

    void setContentPane(Container contentPane)
    {    getRootPane().setContentPane(contentPane);    }
  
    Component getGlassPane()
    {    return getRootPane().getGlassPane();   }
  
    void setGlassPane(Component glassPane)
    {   getRootPane().setGlassPane(glassPane);   }

    
    protected  void addImpl(Component comp, Object constraints, int index)
    {	super.addImpl(comp, constraints, index);    }


    public void remove(Component comp)
    {   getContentPane().remove(comp);  }
  
    protected  boolean isRootPaneCheckingEnabled()
    {    return checking;        }


    protected  void setRootPaneCheckingEnabled(boolean enabled)
    { checking = enabled;  }


    public void update(Graphics g)
    {   paint(g);  }

    protected  void processKeyEvent(KeyEvent e)
    {	super.processKeyEvent(e);    }

    /////////////////////////////////////////////////////////////////////////////////
  
    AccessibleContext getAccessibleContext()
    {    return null;  }
  
    int getDefaultCloseOperation()
    {    return close_action;   }

    
    
    protected  String paramString()
    {   return "JFrame";     }


    protected  void processWindowEvent(WindowEvent e)
    {
	//	System.out.println("PROCESS_WIN_EV-1: " + e);
	super.processWindowEvent(e); 
	//	System.out.println("PROCESS_WIN_EV-2: " + e);
	switch (e.getID())
	    {
	    case WindowEvent.WINDOW_CLOSING:
		{
		    switch(close_action)
			{
			case EXIT_ON_CLOSE:
			    {
				System.out.println("user requested exit on close");
				System.exit(1);
				break;
			    }
			case DISPOSE_ON_CLOSE:
			    {
				System.out.println("user requested dispose on close");
				dispose();
				break;
			    }
			case HIDE_ON_CLOSE:
			    {
				setVisible(false);
				break;
			    }
			case DO_NOTHING_ON_CLOSE:
			    break;
			}
		    break;
		}
		
	    case WindowEvent.WINDOW_CLOSED:
	    case WindowEvent.WINDOW_OPENED:
	    case WindowEvent.WINDOW_ICONIFIED:
	    case WindowEvent.WINDOW_DEICONIFIED:
	    case WindowEvent.WINDOW_ACTIVATED:
	    case WindowEvent.WINDOW_DEACTIVATED:
		break;
	    }
    }   
 

    void setDefaultCloseOperation(int operation)
    {  close_action = operation;   }

}










