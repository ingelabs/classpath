package javax.swing;

import java.awt.*;
import java.awt.peer.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.event.*;
import javax.swing.border.*;
import javax.swing.plaf.*;

import java.util.*;
import java.beans.*;

public abstract class JComponent extends Container implements Serializable
{
	Dimension pref,min,max;
	Border border;
	JToolTip tooltip;
	String tool_tip_text;
	boolean use_double_buffer, opaque;
	protected ComponentUI ui;

	Vector ancestor_list;
	Vector veto_list;
	Vector change_list;
	Hashtable prop_hash;

	JComponent()
	{
		super();
		super.setLayout(new FlowLayout());

		//eventMask |= AWTEvent.COMP_KEY_EVENT_MASK;
		eventMask |= AWTEvent.KEY_EVENT_MASK;

		//updateUI(); // get a proper ui
	}


	// protected EventListenerList listenerList
	public boolean contains(int x, int y)
	{
		//return dims.contains(x,y);
		return super.contains(x,y);
	}


	public  void addNotify()
	{
		//Notification to this component that it now has a parent component.
		super.addNotify();
	}


	Hashtable get_prop_hash()
	{
		if (prop_hash == null)
			prop_hash = new Hashtable();
		return prop_hash;
	}
	Vector get_veto_list()
	{
		if (veto_list == null)
			veto_list = new Vector();
		return veto_list;
	}
	Vector get_change_list()
	{
		if (change_list == null)
			change_list = new Vector();
		return change_list;
	}
	Vector get_ancestor_list()
	{
		if (ancestor_list == null)
			ancestor_list = new Vector();
		return ancestor_list;
	}

	Object getClientProperty(Object key)
{	return get_prop_hash().get(key);    }

	void putClientProperty(Object key, Object value)
	{    get_prop_hash().put(key, value);   }


	void removeAncestorListener(AncestorListener listener)
	{  get_ancestor_list().removeElement(listener);  }

	void removePropertyChangeListener(PropertyChangeListener listener)
	{  get_change_list().removeElement(listener);   }

	void removePropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{  /* FIXME */   get_change_list().removeElement(listener);   }

	void removeVetoableChangeListener(VetoableChangeListener listener)
	{  get_veto_list().removeElement(listener);   }

	void addAncestorListener(AncestorListener listener)
	{   get_ancestor_list().addElement(listener);  }

	void addPropertyChangeListener(PropertyChangeListener listener)
	{  get_change_list().addElement(listener);   }

	void addPropertyChangeListener(String propertyName, PropertyChangeListener listener)
	{ /* FIXME */ get_change_list().addElement(listener);   }

	void addVetoableChangeListener(VetoableChangeListener listener)
	{  get_veto_list().addElement(listener);    }

	void computeVisibleRect(Rectangle rect)
	{
		//Returns the Component's "visible rect rectangle" - the intersection of the visible rectangles for this component and all of its ancestors.
		//super.computeVisibleRect(rect);
	}


	void firePropertyChange(String propertyName, boolean oldValue, boolean newValue)
	{
		//Reports a bound property change.
	}
	void firePropertyChange(String propertyName, byte oldValue, byte newValue)
	{
		//    Reports a bound property change.
	}
	void firePropertyChange(String propertyName, char oldValue, char newValue)
	{
		//Reports a bound property change.
	}

	void firePropertyChange(String propertyName, double oldValue, double newValue)
	{
		//Reports a bound property change.
	}

	void firePropertyChange(String propertyName, float oldValue, float newValue)
	{
		//       Reports a bound property change.
	}
	void firePropertyChange(String propertyName, int oldValue, int newValue)
	{
		//       Reports a bound property change.
	}
	void firePropertyChange(String propertyName, long oldValue, long newValue)
	{
		//Reports a bound property change. protected
	}

	void firePropertyChange(String propertyName, Object oldValue, Object newValue)
	{
		//       Support for reporting bound property changes.
	}
	void firePropertyChange(String propertyName, short oldValue, short newValue)
	{
		//       Reports a bound property change.
	}


	protected  void fireVetoableChange(String propertyName, Object oldValue, Object newValue)
	{
		//       Support for reporting constrained property changes.
	}

	AccessibleContext getAccessibleContext()
	{
		//       Get the AccessibleContext associated with this JComponent
		return null;
	}
	ActionListener getActionForKeyStroke(KeyStroke aKeyStroke)
	{
		//Return the object that will perform the action registered for a given keystroke.
		return null;
	}
	public float getAlignmentX()
	{
		//    Overrides Container.getAlignmentX to return the vertical alignment.
		return 0;
	}

	public float getAlignmentY()
	{
		//       Overrides Container.getAlignmentY to return the horizontal alignment.
		return 0;
	}
	boolean getAutoscrolls()
	{
		//Returns true if this component automatically scrolls its contents when dragged, (when contained in a component that supports scrolling, like JViewport
		return false;
	}

	public void setBorder(Border border)
	{
		//System.out.println("set border called !, new border = " + border);
		this.border = border;
		revalidate();
		repaint();
	}

	public Border getBorder()
	{	return border;    }


	Rectangle getBounds(Rectangle rv)
	{
		if (rv == null)
			return new Rectangle(x,y,width,height);
		else
		{
			rv.setBounds(x,y,width, height);
			return rv;
		}
	}

	protected  Graphics getComponentGraphics(Graphics g)
	{      return g;       }

	int getConditionForKeyStroke(KeyStroke aKeyStroke)
	{
		//Return the condition that determines whether a registered action occurs in response to the specified keystroke.
		return 0;
	}
	int getDebugGraphicsOptions()
	{
		return 0;
	}

	public Graphics getGraphics()
	{	return super.getGraphics();    }


	//    static MantaNative void DebugMe(Border b);

	public Insets getInsets()
	{
		//	System.out.println("watch this border");
		//	DebugMe(border);
		//	System.out.println("border = " + border);

		if (border == null)
		{
			//System.out.println("compares to null !");
			return super.getInsets();
		}
		//	System.out.println("compare failed !");
		return getBorder().getBorderInsets(this);
	}

	Insets getInsets(Insets insets)
	{
		if (insets == null)
			return getInsets();
		return new Insets(getInsets());
	}
	Point getLocation(Point rv)
	{
		//Store the x,y origin of this component into "return value" rv and return rv.

		if (rv == null)
			return new Point(x,y);

		rv.setLocation(x,
		               y);
		return rv;
	}

	public Dimension getMaximumSize()
	{
		if (max != null)
		{
			//System.out.println("HAVE_MAX_SIZE =  " + max);
			return max;
		}
		if (ui != null)
		{
			Dimension s = ui.getMaximumSize(this);
			if (s != null)
			{
				//System.out.println("        UI-MAX = " + s + ", UI = " + ui + ", IM="+this);
				return s;
			}
		}
		Dimension p = super.getMaximumSize();
		//System.out.println("               MAX = " + p + ", COMP="+this);
		return p;
	}

	public Dimension getMinimumSize()
	{
		if (min != null)
		{
			//System.out.println("HAVE_MIN_SIZE =  " + min);
			return min;
		}
		if (ui != null)
		{
			Dimension s = ui.getMinimumSize(this);
			if (s != null)
			{
				//	System.out.println("        UI-MIN = " + s + ", UI = " + ui + ", IM="+this);
				return s;
			}
		}
		Dimension p = super.getMinimumSize();
		//	System.out.println("              MIN = " + p + ", COMP="+this);
		return p;
	}

	public Dimension getPreferredSize()
	{
		if (pref != null)
		{
			//System.out.println("HAVE_PREF_SIZE =  " + pref);
			return pref;
		}

		if (ui != null)
		{
			Dimension s = ui.getPreferredSize(this);
			if (s != null)
			{
				//System.out.println("        UI-PREF = " + s + ", UI = " + ui + ", IM="+this);
				return s;
			}
		}
		Dimension p = super.getPreferredSize();
		//	System.out.println("              PREF = " + p + ", COMP="+this);
		return p;
	}

	Component getNextFocusableComponent()
	{
		//          Return the next focusable component or null if the focus manager should choose the next focusable component automatically
		return null;
	}


	KeyStroke[] getRegisteredKeyStrokes()
	{
		//          Return the KeyStrokes that will initiate registered actions.
		return null;
	}

	JRootPane getRootPane()
	{
		JRootPane p = SwingUtilities.getRootPane(this);
		System.out.println("root = " + p);
		return p;
	}

	Dimension getSize(Dimension rv)
	{
		//	System.out.println("JComponent, getsize()");
		if (rv == null)
			return new Dimension(getWidth(),
			                     getHeight());
		else
		{
			rv.setSize(getWidth(),
			           getHeight());
			return rv;
		}
	}



	/*********************************************************************
	 *
	 *
	 *  tooltips:
	 *
	 *
	 **************************************/

	JToolTip createToolTip()
	{
		if (tooltip == null)
			tooltip = new JToolTip(tool_tip_text);
		return tooltip;
	}

	public Point getToolTipLocation(MouseEvent event)
{	return null;    }

	void setToolTipText(String text)
	{	tool_tip_text = text;    }

	String getToolTipText()
	{	return tool_tip_text;    }

	public String getToolTipText(MouseEvent event)
	{	return tool_tip_text;    }

	/*********************************************************************
	 *
	 *
	 *    things to do with visibility:
	 *
	 *
	 **************************************/


	Container getTopLevelAncestor()
	{
		//      Returns the top-level ancestor of this component (either the containing Window or Applet), or null if this component has not been added to any container.
		System.out.println("JComponent, getTopLevelAncestor()");
		return null;
	}

	Rectangle getVisibleRect()
	{
		///    Returns the Component's "visible rectangle" - the intersection of this components visible rectangle:
		System.out.println("JComponent, getVisibleRect()");
		return null;
	}


	int getHeight()
	{
		//System.out.println("JComponent, getHeight()");
		return height;
	}

	int getWidth()
	{
		//System.out.println("JComponent, getWidth()");
		return width;
	}

	int getX()
	{
		//System.out.println("JComponent, getX()");
		return x;
	}

	int getY()
	{
		//System.out.println("JComponent, getY()");
		return y;
	}

	void grabFocus()
	{
		//      Set the focus on the receiving component.
	}

	boolean hasFocus()
	{
		//      Returns true if this Component has the keyboard focus.
		return false;
	}

	public boolean isDoubleBuffered()
	{	return use_double_buffer;    }

	boolean isFocusCycleRoot()
	{
		//      Override this method and return true if your component is the root of of a component tree with its own focus cycle.
		return false;
	}

	public boolean isFocusTraversable()
	{
		//      Identifies whether or not this component can receive the focus.
		return false;
	}

	static boolean isLightweightComponent(Component c)
	{
		return c.getPeer() instanceof LightweightPeer;
	}

	boolean isManagingFocus()
	{
		//      Override this method and return true if your JComponent manages focus.
		return false;
	}

	boolean isOpaque()
	{	return opaque;    }

	boolean isOptimizedDrawingEnabled()
	{
		//      Returns true if this component tiles its children,
		return true;
	}

	boolean isPaintingTile()
	{
		//      Returns true if the receiving component is currently painting a tile.
		return false;
	}

	boolean isRequestFocusEnabled()
	{
		//      Return whether the receiving component can obtain the focus by calling requestFocus
		return false;
	}

	boolean isValidateRoot()
	{
		//      If this method returns true, revalidate() calls by descendants of this component will cause the entire tree beginning with this root to be validated.
		return false;
	}

	public void paint(Graphics g)
	{
		//	System.out.println("SWING_PAINT:" + this);

		paintBorder(g);
		paintComponent(g);
		paintChildren(g);
	}

	protected  void paintBorder(Graphics g)
	{
		//	System.out.println("PAINT_BORDER      x XXXXXXX x x x x x x x x x x x x:" + getBorder() + ", THIS="+this);

		//       Paint the component's border.
		if (getBorder() != null)
		{
			//System.out.println("PAINT_BORDER      x XXXXXXX x x x x x x x x x x x x:" + getBorder() + ", THIS="+this);

			getBorder().paintBorder(this,
			                        g,
			                        0,
			                        0,
			                        getWidth(),
			                        getHeight());
		}
	}

	protected  void paintChildren(Graphics g)
	{
		//      Paint this component's children.
		super.paintChildren(g);
	}

	protected  void paintComponent(Graphics g)
	{
		//      If the UI delegate is non-null, call its paint method.
		if (ui != null)
		{
			ui.paint(g, this);
		}
	}

	void paintImmediately(int x, int y, int w, int h)
	{
		//      Paint the specified region in this component and all of its descendants that overlap the region, immediately.
	}

	void paintImmediately(Rectangle r)
	{
		///      Paint the specified region now.
		paintImmediately(r.x,
		                 r.y,
		                 r.width,
		                 r.height);
	}
	protected  String paramString()
	{
		//      Returns a string representation of this JComponent.
		return "JComponent";
	}
	protected  void processComponentKeyEvent(KeyEvent e)
	{
		//     Process any key events that the component itself recognizes.
	    //System.out.println("COMP_KEY-EVENT: " + e);
	}
	protected  void processFocusEvent(FocusEvent e)
	{
		//      Processes focus events occurring on this component by dispatching them to any registered FocusListener objects.
	    //System.out.println("FOCUS_EVENT: " + e);
	}

	protected  void processKeyEvent(KeyEvent e)
	{
		//      Override processKeyEvent to process events protected
	    //System.out.println("KEY-EVENT: " + e);
	}

        public void processMouseMotionEvent(MouseEvent e)
	{
	    //      Processes mouse motion events occurring on this component by dispatching them to any registered MouseMotionListener objects.
	    //System.out.println("COMP_MOUSE-EVENT: " + e + ", MEMORY = " + Runtime.getRuntime().freeMemory());
	}

	void registerKeyboardAction(ActionListener anAction,
	                            KeyStroke aKeyStroke,
	                            int aCondition)
	{
		registerKeyboardAction(anAction,
		                       null,
		                       aKeyStroke,
		                       aCondition);
	}

	void registerKeyboardAction(ActionListener anAction,
	                            String aCommand,
	                            KeyStroke aKeyStroke,
	                            int aCondition)
	{
		//  Register a new keyboard action.
	}


	public void removeNotify()
	{
		//      Notification to this component that it no longer has a parent component.
	}

	public void repaint(long tm, int x, int y, int width, int height)
	{
		//   Adds the specified region to the dirty region list if the component is showing.
		//System.out.println("JC: repaint");
		super.repaint(tm, x,y,width,height);
	}

	void repaint(Rectangle r)
	{
		//      Adds the specified region to the dirty region list if the component is showing.
		repaint(0,
		        r.x,
		        r.y,
		        r.width,
		        r.height);
	}

	boolean requestDefaultFocus()
	{
		//      Request the focus for the component that should have the focus by default.
		return false;
	}

	public void requestFocus()
	{
		//      Set focus on the receiving component if isRequestFocusEnabled returns true
		super.requestFocus();
	}

	void resetKeyboardActions()
	{
		//      Unregister all keyboard actions
	}

	public void reshape(int x, int y, int w, int h)
	{
		///      Moves and resizes this component.
		super.reshape(x,y,w,h);
	}

	void revalidate()
	{
		//     Support for deferred automatic layout.
		if (getParent() == null)
			invalidate();
	}

	void scrollRectToVisible(Rectangle aRect)
	{
		//      Forwards the scrollRectToVisible() message to the JComponent's parent.
	}

	void setAlignmentX(float alignmentX)
	{
		//      Set the the vertical alignment.
	}

	void setAlignmentY(float alignmentY)
	{
		//      Set the the horizontal alignment.
	}

	void setAutoscrolls(boolean autoscrolls)
	{
		//      If true this component will automatically scroll its contents when dragged, if contained in a component that supports scrolling, such as JViewport
	}


	void setDebugGraphicsOptions(int debugOptions)
	{
		//      Enables or disables diagnostic information about every graphics operation performed within the component or one of its children.
	}

	void setDoubleBuffered(boolean aFlag)
	{
		use_double_buffer = aFlag;
	}

	public void setEnabled(boolean enabled)
	{
		// Sets whether or not this component is enabled.
		super.setEnabled(enabled);
		repaint();
	}

	public void setFont(Font font)
	{
		super.setFont(font);
		revalidate();
		repaint();
	}
	public void setBackground(Color bg)
	{
		super.setBackground(bg);
		revalidate();
		repaint();
	}
	public void setForeground(Color fg)
	{
		super.setForeground(fg);
		revalidate();
		repaint();
	}

	void setMaximumSize(Dimension maximumSize)
	{	max = maximumSize;    }

	void setMinimumSize(Dimension minimumSize)
	{   min = minimumSize; }

	void setPreferredSize(Dimension preferredSize)
	{   pref = preferredSize;   }

	void setNextFocusableComponent(Component aComponent)
	{
		//       Specifies the next component to get the focus after this one, for example, when the tab key is pressed.
	}

	void setOpaque(boolean isOpaque)
	{
		opaque = isOpaque;
		revalidate();
		repaint();
	}


	void setRequestFocusEnabled(boolean aFlag)
	{
	}


	public void setVisible(boolean aFlag)
	{
		//    Makes the component visible or invisible.

		super.setVisible(aFlag);
		if (getParent() != null)
		{
			Rectangle dims = getBounds();
			getParent().repaint(dims.x,
			                    dims.y,
			                    dims.width,
			                    dims.height);
		}
	}

	void unregisterKeyboardAction(KeyStroke aKeyStroke)
	{
		//          Unregister a keyboard action.
	}


	public void update(Graphics g)
	{
		paint(g);
	}



	/******************************************
	 *
	 *
	 *  UI management
	 * 
	 *
	 *********/

	String getUIClassID()
	{
		///          Return the UIDefaults key used to look up the name of the swing.
		return "JComponent";
	}

	protected void setUI(ComponentUI newUI)
	{
		if (ui != null)
		{
			ui.uninstallUI(this);
		}

		//          Set the look and feel delegate for this component.
		ui = newUI;

		if (ui != null)
		{
			ui.installUI(this);
		}

		revalidate();
		repaint();
	}

	void updateUI()
	{
		//        Resets the UI property to a value from the current look and feel.
		System.out.println("update UI not overwritten in class: " + this);
	}

}







