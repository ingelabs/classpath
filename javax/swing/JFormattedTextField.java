/* JFormattedTextField.java --
   Copyright (C) 2003, 2004 Free Software Foundation, Inc.

This file is part of GNU Classpath.

GNU Classpath is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2, or (at your option)
any later version.

GNU Classpath is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
General Public License for more details.

You should have received a copy of the GNU General Public License
along with GNU Classpath; see the file COPYING.  If not, write to the
Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
02110-1301 USA.

Linking this library statically or dynamically with other modules is
making a combined work based on this library.  Thus, the terms and
conditions of the GNU General Public License cover the whole
combination.

As a special exception, the copyright holders of this library give you
permission to link this library with independent modules to produce an
executable, regardless of the license terms of these independent
modules, and to copy and distribute the resulting executable under
terms of your choice, provided that you also meet, for each linked
independent module, the terms and conditions of the license of that
module.  An independent module is a module which is not derived from
or based on this library.  If you modify this library, you may extend
this exception to your version of the library, but you are not
obligated to do so.  If you do not wish to do so, delete this
exception statement from your version. */


package javax.swing;

import java.awt.event.FocusEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.InternationalFormatter;
import javax.swing.text.NavigationFilter;
import javax.swing.text.NumberFormatter;

/**
 * A text field that makes use of a formatter to display and edit a specific
 * type of data. The value that is displayed can be an arbitrary object. The
 * formatter is responsible for displaying the value in a textual form and
 * it may allow editing of the value.
 *
 * Formatters are usually obtained using an instance of
 * {@link AbstractFormatterFactory}. This factory is responsible for providing
 * an instance of {@link AbstractFormatter} that is able to handle the
 * formatting of the value of the JFormattedTextField.
 *
 * @author Michael Koch
 *
 * @since 1.4
 */
public class JFormattedTextField extends JTextField
{
  private static final long serialVersionUID = 5464657870110180632L;

  /**
   * An abstract base implementation for a formatter that can be used by
   * a JTextField. A formatter can display a specific type of object and
   * may provide a way to edit this value.
   */
  public abstract static class AbstractFormatter implements Serializable
  {
    private static final long serialVersionUID = -5193212041738979680L;
    
    private JFormattedTextField textField;
    
    public AbstractFormatter ()
    {
      //Do nothing here.
    }

    protected Object clone ()
      throws CloneNotSupportedException
    {
      throw new InternalError ("not implemented");
    }

    protected Action[] getActions ()
    {
      return textField.getActions();
    }

    protected DocumentFilter getDocumentFilter ()
    {
      throw new InternalError ("not implemented");
    }

    protected JFormattedTextField getFormattedTextField ()
    {
      return textField;
    }

    protected NavigationFilter getNavigationFilter ()
    {
      return textField.getNavigationFilter();
    }

    public void install(JFormattedTextField textField)
    {
      if (this.textField != null)
        uninstall();
      
      this.textField = textField;
      
      if (textField != null)
        {
          try
          {
            textField.setText(valueToString(textField.getValue()));
          }
          catch (ParseException pe)
          {
            // FIXME: Not sure what to do here.
          }
        }
    }

    public void uninstall ()
    {
      this.textField = null;
    }

    protected void invalidEdit ()
    {
      textField.invalidEdit();
    }

    protected void setEditValid (boolean valid)
    {
      textField.editValid = valid;
    }

    public abstract Object stringToValue (String text)
      throws ParseException;

    public abstract String valueToString (Object value)
      throws ParseException;
  }

  /**
   * Delivers instances of an {@link AbstractFormatter} for
   * a specific value type for a JFormattedTextField. 
   */
  public abstract static class AbstractFormatterFactory
  {
    public AbstractFormatterFactory ()
    {
      // Do nothing here.
    }

    public abstract AbstractFormatter getFormatter (JFormattedTextField tf);
  }

  public static final int COMMIT = 0;
  public static final int COMMIT_OR_REVERT = 1;
  public static final int REVERT = 2;
  public static final int PERSIST = 3;

  private Object value;
  private int focusLostBehavior = COMMIT_OR_REVERT;
  private AbstractFormatterFactory formatterFactory;
  private AbstractFormatter formatter;
  // Package-private to avoid an accessor method.
  boolean editValid = true;
  
  public JFormattedTextField ()
  {
    this((AbstractFormatterFactory) null, null);
  }

  public JFormattedTextField (Format format)
  {
    this ();
    setFormatterFactory(getAppropriateFormatterFactory(format));
  }

  public JFormattedTextField (AbstractFormatter formatter)
  {
    this(new DefaultFormatterFactory (formatter), null);
  }

  public JFormattedTextField (AbstractFormatterFactory factory)
  {
    this(factory, null);
  }

  public JFormattedTextField (AbstractFormatterFactory factory, Object value)
  {
    setValue(value);
    setFormatterFactory(factory);    
  }

  public JFormattedTextField (Object value)
  {
    setValue(value);
  }
  
  /**
   * Returns an AbstractFormatterFactory that will give an appropriate
   * AbstractFormatter for the given Format.
   * @param format the Format to match with an AbstractFormatter.
   * @return a DefaultFormatterFactory whose defaultFormatter is appropriate
   * for the given Format.
   */
  private AbstractFormatterFactory getAppropriateFormatterFactory (Format format)
  {
    AbstractFormatter newFormatter;
    if (format instanceof DateFormat)
      newFormatter = new DateFormatter((DateFormat)format);
    else if (format instanceof NumberFormat)
      newFormatter = new NumberFormatter ((NumberFormat)format);
    else
      newFormatter = new InternationalFormatter(format);
    
    return new DefaultFormatterFactory(newFormatter);
  }

  public void commitEdit ()
    throws ParseException
  {
    throw new InternalError ("not implemented");
  }

  public Action[] getActions ()
  {
    // FIXME: Add JFormattedTextField specific actions
    return super.getActions();
  }

  public int getFocusLostBehavior()
  {
    return focusLostBehavior;
  }

  public AbstractFormatter getFormatter ()
  {
    return formatter;
  }

  public AbstractFormatterFactory getFormatterFactory ()
  {
    return formatterFactory;
  }

  public String getUIClassID ()
  {
    return "FormattedTextFieldUI";
  }

  public Object getValue ()
  {
    return value;
  }

  protected void invalidEdit ()
  {
    UIManager.getLookAndFeel().provideErrorFeedback(this);
  }

  public boolean isEditValid ()
  {
    return editValid;
  }

  protected void processFocusEvent (FocusEvent evt)
  {
    super.processFocusEvent(evt);
    // Let the formatterFactory change the formatter for this text field
    // based on whether or not it has focus.
    setFormatter (formatterFactory.getFormatter(this));
  }

  public void setDocument(Document newDocument)
  {
    Document oldDocument = getDocument();

    if (oldDocument == newDocument)
      return;
    
    super.setDocument(newDocument);
  }

  public void setFocusLostBehavior(int behavior)
  {
    if (behavior != COMMIT
	&& behavior != COMMIT_OR_REVERT
	&& behavior != PERSIST
	&& behavior != REVERT)
      throw new IllegalArgumentException("invalid behavior");

    this.focusLostBehavior = behavior;
  }

  protected void setFormatter (AbstractFormatter formatter)
  {
    AbstractFormatter oldFormatter = null;
    
    oldFormatter = this.formatter;

    if (oldFormatter == formatter)
      return;
    
    if (oldFormatter != null)
      oldFormatter.uninstall();
    
    this.formatter = formatter;
    
    if (formatter != null)
      formatter.install(this);

    firePropertyChange("formatter", oldFormatter, formatter);
  }

  public void setFormatterFactory (AbstractFormatterFactory factory)
  {
    if (formatterFactory == factory)
      return;
    
    AbstractFormatterFactory oldFactory = formatterFactory;
    formatterFactory = factory;
    firePropertyChange("formatterFactory", oldFactory, factory);
    setFormatter(formatterFactory.getFormatter(this));
  }

  public void setValue (Object newValue)
  {
    if (value == newValue)
      return;

    Object oldValue = value;
    value = newValue;
    
    // format value
    formatter = createFormatter(newValue);
    try
      {
        setText(formatter.valueToString(newValue));
      }
    catch (ParseException ex)
      {
        // TODO: what should we do with this?
      }
    firePropertyChange("value", oldValue, newValue);
  }

  /**
   * A helper method that attempts to create a formatter that is suitable
   * to format objects of the type like <code>value</code>.
   *
   * If <code>formatterFactory</code> is not null and the returned formatter
   * is also not <code>null</code> then this formatter is used. Otherwise we
   * try to create one based on the type of <code>value</code>.
   *
   * @param value an object which should be formatted by the formatter
   *
   * @return a formatter able to format objects of the class of
   *     <code>value</code>
   */
  AbstractFormatter createFormatter(Object value)
  {
    AbstractFormatter formatter = null;
    if (formatterFactory != null && formatterFactory.getFormatter(this) != null)
      formatter = formatterFactory.getFormatter(this);
    else
      {
        if (value instanceof Date)
          formatter = new DateFormatter();
        else if (value instanceof Number)
          formatter = new NumberFormatter();
        else
          formatter = new DefaultFormatter();
        formatterFactory = new DefaultFormatterFactory (formatter);
      }
    return formatter;
  }
}
