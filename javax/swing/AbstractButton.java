package javax.swing;

import java.awt.*;
import java.awt.event.*;

import javax.swing.event.*;
import javax.swing.plaf.*;

import java.util.*;
import java.beans.*;


public abstract class AbstractButton extends JComponent
			implements ItemSelectable, SwingConstants
{
	Icon default_icon, pressed_button, disabled_button,
	selected_button, disabled_selected_button, current_icon;
	String text;

	int vert_align = CENTER;
	int hori_align = CENTER;
	int hori_text_pos = CENTER;
	int vert_text_pos = CENTER;

	boolean paint_border = true, paint_focus;
	Action action_taken;
	ButtonModel model;
	Insets margin;


	public static final String FOCUS_PAINTED_CHANGED_PROPERTY = "focusPainted";

	static private class JFocusListener implements FocusListener
	{
		AbstractButton c;

		JFocusListener(AbstractButton c)
		{
			this.c = c;
		}

		public void focusLost(FocusEvent event)
		{
			c.getModel().setArmed(false);

			System.out.println("LOST FOCUS");
			if (c.isFocusPainted())
			{
				c.repaint();
			}
		}
		public void focusGained(FocusEvent event)
		{
			System.out.println("GAIN FOCUS");
		}
	}


	/**********************************************
	 *
	 * 
	 *       Constructors
	 *
	 *
	 ****************/

	AbstractButton()
	{
		this("",null);
	}

	AbstractButton(String text,
	               Icon icon)
	{
		this.text    = text;
		setIcon(icon);

		setAlignmentX(LEFT_ALIGNMENT);
		setAlignmentY(CENTER_ALIGNMENT);

		addFocusListener( new JFocusListener(this) );

		setModel(new DefaultButtonModel(this));

		updateUI(); // get a proper ui
	}


	/**********************************************
	 *
	 * 
	 *       Actions etc
	 *
	 *
	 ****************/

	ButtonModel getModel()
	{	return model;    }
	void setModel(ButtonModel newModel)
	{	model = newModel;    }

	String getActionCommand()
	{	return getModel().getActionCommand();    }
	void setActionCommand(String aCommand)
	{   getModel().setActionCommand(aCommand);   }

	void addActionListener(ActionListener l)
	{	getModel().addActionListener(l);    }
	void removeActionListener(ActionListener l)
	{	getModel().removeActionListener(l);    }

	void addChangeListener(ChangeListener l)
	{   getModel().addChangeListener(l);     }
	void removeChangeListener(ChangeListener l)
	{  getModel().removeChangeListener(l);    }

        public void addItemListener(ItemListener l)
	{  getModel().addItemListener(l);    }
        public void removeItemListener(ItemListener l)
	{  getModel().removeItemListener(l);  }

	int getHorizontalAlignment()
	{	return hori_align;    }
	int getHorizontalTextPosition()
	{	return hori_text_pos;    }
	int getVerticalAlignment()
	{	return vert_align;   }
	int getVerticalTextPosition()
	{	return vert_text_pos;  }


	protected  void fireItemStateChanged(ItemEvent event)
	{    getModel().fireItemStateChanged(event);    }
	protected  void fireStateChanged(ChangeEvent event)
	{	 getModel().fireStateChanged(event);    }
	protected void fireActionPerformed(ActionEvent event)
	{	getModel().fireActionPerformed(event);    }

	void setVerticalAlignment(int alignment)
	{	vert_align = alignment;    }
	void setHorizontalAlignment(int alignment)
	{   hori_align = alignment;   }
	void setVerticalTextPosition(int textPosition)
	{	vert_text_pos = textPosition;    }
	void setHorizontalTextPosition(int textPosition)
	{   hori_text_pos = textPosition;   }

	int getMnemonic()
	{	return getModel().getMnemonic();    }
	void setMnemonic(char mne)
	{	getModel().setMnemonic(mne);    }
	void setMnemonic(int mne)
	{	getModel().setMnemonic(mne);    }

	void setRolloverEnabled(boolean b)
	{    getModel().setRollover(b);    }
	boolean isRolloverEnabled()
	{    return getModel().isRollover();     }


	boolean isBorderPainted()
	{	return paint_border;    }
	void setBorderPainted(boolean b)
	{
		if (b != paint_border)
		{
			paint_border = b;
			revalidate();
			repaint();
		}
	}

	Action getAction()
	{	return action_taken;    }
	void setAction(Action a)
	{
		action_taken = a;
		revalidate();
		repaint();
	}

	void setSelected(boolean b)
	{	getModel().setSelected(b);    }
	boolean isSelected()
	{	return getModel().isSelected();     }


	Icon getIcon()
	{	return default_icon;    }
	void setIcon(Icon defaultIcon)
	{
		if (default_icon == defaultIcon)
			return;

		default_icon = defaultIcon;
		if (default_icon != null)
		{
			default_icon.setParent(this);
		}
		revalidate();
		repaint();
	}

	String getText()
	{	return text;    }
	void setLabel(String label)
	{	setText(label);    }
	String getLabel()
	{	return getText();    }
	void setText(String text)
	{
		this.text = text;
		revalidate();
		repaint();
	}


	Insets getMargin()
	{      return margin; }
	void setMargin(Insets m)
	{
		margin = m;
		revalidate();
		repaint();
	}

	public void setEnabled(boolean b)
	{
		super.setEnabled(b);
		getModel().setEnabled(b);
		repaint();
	}

	Icon getPressedIcon()
	{	return pressed_button;    }
	void setPressedIcon(Icon pressedIcon)
	{
		pressed_button = pressedIcon;
		revalidate();
		repaint();
	}


	Icon getDisabledIcon()
	{	return disabled_button;    }
	void setDisabledIcon(Icon disabledIcon)
	{
		disabled_button = disabledIcon;
		revalidate();
		repaint();
	}

	public boolean isFocusPainted()
	{   return paint_focus;   }
	public void setFocusPainted(boolean b)
	{
		boolean old = paint_focus;
		paint_focus = b;

		firePropertyChange(FOCUS_PAINTED_CHANGED_PROPERTY,
		                   old,
		                   b);
		if (hasFocus())
		{
			revalidate();
			repaint();
		}
	}

	public boolean isFocusTraversable()
	{
		//Identifies whether or not this component can receive the focus.
		return true;
	}


	protected  int checkHorizontalKey(int key, String exception)
	{
		//       Verify that key is a legal value for the horizontalAlignment properties.
		return 0;
	}
	protected  int checkVerticalKey(int key, String exception)
	{
		//       Ensures that the key is a valid.
		return 0;
	}
	protected  void configurePropertiesFromAction(Action a)
	{
		//Factory method which sets the ActionEvent source's properties according to values from the Action instance.
	}

	protected  ActionListener createActionListener()
	{
		return new ActionListener()
		       {
			       public void actionPerformed(ActionEvent e) { }
		       };
	}

	protected  PropertyChangeListener createActionPropertyChangeListener(Action a)
	{
		//Factory method which creates the PropertyChangeListener used to update the ActionEvent source as properties change on its Action instance.
		return null;
	}
	protected  ChangeListener createChangeListener()
	{
		//       Subclasses that want to handle ChangeEvents differently can override this to return another ChangeListener implementation.
		return new ChangeListener()
		       {
			       public void stateChanged(ChangeEvent e) { }
		       };
	}

	protected  ItemListener createItemListener()
	{
		return new ItemListener()
		       {
			       public void itemStateChanged(ItemEvent e) { }
		       };
	}


	void doClick()
	{
		doClick(100);
	}
	void doClick(int pressTime)
	{
		Toolkit.tlkBeep ();
		//Programmatically perform a "click".
	}


	Icon getDisabledSelectedIcon()
	{
		//Returns the icon used by the button when it's disabled and selected.
		return disabled_selected_button;
	}


	Icon getRolloverIcon()
	{
		//       Returns the rollover icon for the button.
		return null;
	}

	Icon getRolloverSelectedIcon()
	{
		//       Returns the rollover selection icon for the button.
		return null;
	}
	Icon getSelectedIcon()
	{
		//       Returns the selected icon for the button.
		return selected_button;
	}


	public Object[] getSelectedObjects()
	{
		//Returns an array (length 1) containing the label or null if the button is not selected.
		return null;
	}


	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h)
	{
		//This is overridden to return false if the current Icon's Image is not equal to the passed in Image img.
		return current_icon == img;
	}

	boolean isContentAreaFilled()
	{
		//       Checks whether the "content area" of the button should be filled.
		return false;
	}



	protected  void paintBorder(Graphics g)
	{
		//       Paint the button's border if BorderPainted property is true.
		if (isBorderPainted())
			super.paintBorder(g);
	}
	protected  String paramString()
	{
		//        Returns a string representation of this AbstractButton.
		return "AbstractButton";
	}


	void setContentAreaFilled(boolean b)
	{
		//Sets whether the button should paint the content area or leave it transparent.
	}


	void setDisabledSelectedIcon(Icon disabledSelectedIcon)
	{
		//          Sets the disabled selection icon for the button.
	}

	void setRolloverIcon(Icon rolloverIcon)
	{
		//       Sets the rollover icon for the button.
	}
	void setRolloverSelectedIcon(Icon rolloverSelectedIcon)
	{
		//       Sets the rollover selected icon for the button.
	}


	void setSelectedIcon(Icon selectedIcon)
	{
		//       Sets the selected icon for the button.
	}


	public void setUI(ButtonUI ui)
	{	//       Sets the L&F object that renders this component.
		super.setUI(ui);
	}

	ButtonUI getUI()
	{
		//Returns the L&F object that renders this component.
		return (ButtonUI) ui;
	}

	void updateUI()
	{
		/*
		  //          Notification from the UIFactory that the L&F has changed.
		  if (getUI() == null)
		  {
		  setUI(getUI());
		  }
		*/
	}

	protected void processActionEvent(ActionEvent e)
	{
		System.out.println("PROCESS-ACTION-EVENT: " + e);
	}


	protected void processMouseEvent(MouseEvent e)
	{
		//	System.out.println("PROCESS-MOUSE-EVENT: " + e + ", PRESSED-IN-MODEL="+getModel().isPressed());

		switch (e.getID())
		{
		case MouseEvent.MOUSE_MOVED:
			{
				break;
			}
		case MouseEvent.MOUSE_PRESSED:
			{
				if (! isEnabled())
				{
					System.out.println("button not enabled, ignoring press");
				}
				else
				{
					System.out.println("telling model:press: " + getModel());
					getModel().setPressed(true);
					repaint();
				}
				break;
			}

		case MouseEvent.MOUSE_RELEASED:
			{
				if (! isEnabled())
				{
					System.out.println("button not enabled, ignoring release");
				}
				else
				{
					int flags = 0;

					System.out.println("        XXX--> " + getActionCommand());

					fireActionPerformed(new ActionEvent(this,
					                                    ActionEvent.ACTION_PERFORMED,
					                                    getActionCommand(),
					                                    flags));

					//System.out.println("telling model:release");
					getModel().setPressed(false);
					repaint();
				}
				break;
			}
		case MouseEvent.MOUSE_CLICKED:
			{
				break;
			}
		}
	}
}











