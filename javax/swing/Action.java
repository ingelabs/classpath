package javax.swing;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;


/**
 * Empty
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */

public abstract 
interface Action extends ActionListener
{
  static String DEFAULT = "";
  static String LONG_DESCRIPTION="";
  static String NAME = "";
  static String SHORT_DESCRIPTION="";
  static String SMALL_ICON=""; 


  void addPropertyChangeListener(PropertyChangeListener listener);
  Object getValue(String key);
  boolean isEnabled();
  void putValue(String key, Object value);
  void removePropertyChangeListener(PropertyChangeListener listener);
  void setEnabled(boolean b);
}
