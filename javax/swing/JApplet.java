package javax.swing;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;



public class JApplet extends Applet
{

    public final static int HIDE_ON_CLOSE        = 0;
    public final static int EXIT_ON_CLOSE        = 1;
    public final static int DISPOSE_ON_CLOSE     = 2;
    public final static int DO_NOTHING_ON_CLOSE  = 3;

    private int close_action = EXIT_ON_CLOSE;
    private boolean checking;  
    protected  JRootPane         rootPane;

    public JApplet()
    {
	frameInit();
    }
  
    public JApplet(String title)
    {
	frameInit();
    }

    protected  void frameInit()
    {
      super.setLayout(new BorderLayout(1, 1));
      getRootPane(); // will do set/create
    }

  public Dimension getPreferredSize()
  {
    Dimension d = super.getPreferredSize();
    System.out.println("JFrame.getPrefSize(): " + d + " , comp="+countComponents() + ", layout=" + getLayout());
    return d;
  }

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


    /////////////////////////////////////////////////////////////////////////////////
    protected  void addImpl(Component comp, Object constraints, int index)
    {   super.addImpl(comp, constraints, index);    }
  
    AccessibleContext getAccessibleContext()
    {    return null;  }
  
    int getDefaultCloseOperation()
    {    return close_action;   }

    
    JMenuBar getJMenuBar()
    {    return getRootPane().getJMenuBar();   }
    
    void setJMenuBar(JMenuBar menubar)
    {    getRootPane().setJMenuBar(menubar); }
    
    
    protected  String paramString()
    {   return "JFrame";     }

    protected  void processKeyEvent(KeyEvent e)
    {   super.processKeyEvent(e);    }

    protected  void processWindowEvent(WindowEvent e)
    {
        //      System.out.println("PROCESS_WIN_EV-1: " + e);

	//        super.processWindowEvent(e); 

        //      System.out.println("PROCESS_WIN_EV-2: " + e);
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
                                //dispose();
                                break;
                            }
                        case HIDE_ON_CLOSE:

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
    

    public void remove(Component comp)
    {   getContentPane().remove(comp);  }
  

    void setDefaultCloseOperation(int operation)
    {  close_action = operation;   }



    protected  boolean isRootPaneCheckingEnabled()
    {    return checking;        }


    protected  void setRootPaneCheckingEnabled(boolean enabled)
    { checking = enabled;  }

    public void update(Graphics g)
    {   paint(g);  }
}
