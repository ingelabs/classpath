package javax.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;
import java.util.Locale;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;


/**
 * UIDefaults is a database where all settings and interface bindings are
 * stored into. An PLAF implementation fills one of these (see for example
 * plaf/basic/BasicDefaults.java) with "JButton" -> new BasicButtonUI().
 *
 * @author Ronald Veldema (rveldema@cs.vu.nl)
 */
public class UIDefaults extends Hashtable
{
  public interface ActiveValue
  {
    Object createValue(UIDefaults table);
  } // interface ActiveValue

  public static class LazyInputMap implements LazyValue
  {
    public LazyInputMap(Object[] bindings)
    {
    }
    public Object createValue(UIDefaults table)
    {
      throw new Error("not implemented");
    }
  } // class LazyInputMap

  public interface LazyValue
  {
    Object createValue(UIDefaults table);
  } // interface LazyValue

  public static class ProxyLazyValue
  {
    public ProxyLazyValue(String s)
    {
      throw new Error("not implemented");
    }
    public ProxyLazyValue(String c, String m)
    {
      throw new Error("not implemented");
    }
    public ProxyLazyValue(String c, Object[] o)
    {
      throw new Error("not implemented");
    }
    public ProxyLazyValue(String c, String m, Object[] o)
    {
      throw new Error("not implemented");
    }
    public Object createValue(UIDefaults table)
    {
      throw new Error("not implemented");
    }
  } // class ProxyLazyValue

  public UIDefaults()
  {
  }

  public UIDefaults(Object[] entries)
  {
    // XXX
  }

  public Object get(Object key)
  {
    // XXX Obey 1.4 specs
    return super.get(key);
  }

  public Object get(Object key, Locale l)
  {
    throw new Error("not implemented");
  }

  public Object put(Object key, Object value)
  {
    throw new Error("not implemented");
  }

  public void putDefaults(Object[] list)
  {
    throw new Error("not implemented");
  }

  public Font getFont(Object key)
  {
    Object o = get(key);
    return o instanceof Font ? (Font) o : null;
  }

  public Font getFont(Object key, Locale l)
  {
    Object o = get(key, l);
    return o instanceof Font ? (Font) o : null;
  }

  public Color getColor(Object key)
  {
    Object o = get(key);
    return o instanceof Color ? (Color) o : null;
  }

  public Color getColor(Object key, Locale l)
  {
    Object o = get(key, l);
    return o instanceof Color ? (Color) o : null;
  }

  public Icon getIcon(Object key)
  {
    Object o = get(key);
    return o instanceof Icon ? (Icon) o : null;
  }

  public Icon getIcon(Object key, Locale l)
  {
    Object o = get(key, l);
    return o instanceof Icon ? (Icon) o : null;
  }

  public Border getBorder(Object key)
  {
    Object o = get(key);
    return o instanceof Border ? (Border) o : null;
  }

  public Border getBorder(Object key, Locale l)
  {
    Object o = get(key, l);
    return o instanceof Border ? (Border) o : null;
  }

  public String getString(Object key)
  {
    Object o = get(key);
    return o instanceof String ? (String) o : null;
  }

  public String getString(Object key, Locale l)
  {
    Object o = get(key, l);
    return o instanceof String ? (String) o : null;
  }

  public int getInt(Object key)
  {
    Object o = get(key);
    return o instanceof Integer ? ((Integer) o).intValue() : 0;
  }

  public int getInt(Object key, Locale l)
  {
    Object o = get(key, l);
    return o instanceof Integer ? ((Integer) o).intValue() : 0;
  }

  public boolean getBoolean(Object key)
  {
    return Boolean.TRUE.equals(get(key));
  }

  public boolean getBoolean(Object key, Locale l)
  {
    return Boolean.TRUE.equals(get(key, l));
  }

  public Insets getInsets(Object key) 
  {
    Object o = get(key);
    return o instanceof Insets ? (Insets) o : null;
  }

  public Insets getInsets(Object key, Locale l) 
  {
    Object o = get(key, l);
    return o instanceof Insets ? (Insets) o : null;
  }

  public Dimension getDimension(Object key) 
  {
    Object o = get(key);
    return o instanceof Dimension ? (Dimension) o : null;
  }

  public Dimension getDimension(Object key, Locale l) 
  {
    Object o = get(key, l);
    return o instanceof Dimension ? (Dimension) o : null;
  }

  public Class getUIClass(String id, ClassLoader loader)
  {
    throw new Error("not implemented");
  }

  public Class getUIClass(String id)
  {
    throw new Error("not implemented");
  }

  protected void getUIError(String msg)
  {
    // Does nothing unless overridden.
  }

  public ComponentUI getUI(JComponent a)
  {
    String pp = a.getUIClassID();
    ComponentUI p = (ComponentUI) get(pp);
    if (p == null)
      getUIError("failed to locate UI:" + pp);
    return p;
  }

  public void addPropertyChangeListener(PropertyChangeListener l)
  {
    throw new Error("not implemented");
  }

  public void removePropertyChangeListener(PropertyChangeListener l)
  {
    throw new Error("not implemented");
  }

  public PropertyChangeListener[] getPropertyChangeListeners()
  {
    throw new Error("not implemented");
  }

  protected void firePropertyChange(String property, Object o, Object n)
  {
    throw new Error("not implemented");
  }

  public void addResourceBundle(String name)
  {
    throw new Error("not implemented");
  }

  public void removeResourceBundle(String name)
  {
    throw new Error("not implemented");
  }

  public void setDefaultLocale(Locale l)
  {
    throw new Error("not implemented");
  }

  public Locale getDefaultLocale()
  {
    throw new Error("not implemented");
  }
} // class UIDefaults
