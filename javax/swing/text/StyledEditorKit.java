/* StyledEditorKit.java --
   Copyright (C) 2002, 2004 Free Software Foundation, Inc.

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


package javax.swing.text;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

/**
 * StyledEditorKit
 *
 * @author Andrew Selkirk
 */
public class StyledEditorKit extends DefaultEditorKit
{
  private static final long serialVersionUID = 7002391892985555948L;

  /**
   * UnderlineAction
   */
  public static class UnderlineAction extends StyledEditorKit.StyledTextAction
  {
    /**
     * Constructor UnderlineAction
     */
    public UnderlineAction()
    {
      super("TODO"); // TODO: Figure out name for this action.
    }

    /**
     * actionPerformed
     * @param event TODO
     */
    public void actionPerformed(ActionEvent event)
    {
      JEditorPane editor = getEditor(event);
      StyledDocument doc = getStyledDocument(editor);
      Element el = doc.getCharacterElement(editor.getSelectionStart());
      boolean isUnderline = StyleConstants.isUnderline(el.getAttributes());
      SimpleAttributeSet atts = new SimpleAttributeSet();
      StyleConstants.setUnderline(atts, ! isUnderline);
      setCharacterAttributes(editor, atts, false);
    }
  }

  /**
   * ItalicAction
   */
  public static class ItalicAction extends StyledEditorKit.StyledTextAction
  {
    /**
     * Constructor ItalicAction
     */
    public ItalicAction()
    {
      super("TODO"); // TODO: Figure out correct name of this Action.
    }

    /**
     * actionPerformed
     * @param event TODO
     */
    public void actionPerformed(ActionEvent event)
    {
      JEditorPane editor = getEditor(event);
      StyledDocument doc = getStyledDocument(editor);
      Element el = doc.getCharacterElement(editor.getSelectionStart());
      boolean isItalic = StyleConstants.isItalic(el.getAttributes());
      SimpleAttributeSet atts = new SimpleAttributeSet();
      StyleConstants.setItalic(atts, ! isItalic);
      setCharacterAttributes(editor, atts, false);
    }
  }

  /**
   * BoldAction
   */
  public static class BoldAction extends StyledEditorKit.StyledTextAction
  {
    /**
     * Constructor BoldAction
     */
    public BoldAction()
    {
      super("TODO"); // TODO: Figure out correct name of this Action.
    }

    /**
     * actionPerformed
     * @param event TODO
     */
    public void actionPerformed(ActionEvent event)
    {
      JEditorPane editor = getEditor(event);
      StyledDocument doc = getStyledDocument(editor);
      Element el = doc.getCharacterElement(editor.getSelectionStart());
      boolean isBold = StyleConstants.isBold(el.getAttributes());
      SimpleAttributeSet atts = new SimpleAttributeSet();
      StyleConstants.setItalic(atts, ! isBold);
      setCharacterAttributes(editor, atts, false);
    }
  }

  /**
   * AlignmentAction
   */
  public static class AlignmentAction extends StyledEditorKit.StyledTextAction
  {
    /**
     * a
     */
    private int a;

    /**
     * Constructor AlignmentAction
     * @param nm TODO
     * @param a TODO
     */
    public AlignmentAction(String nm, int a)
    {
      super(nm);
      this.a = a;
    }

    /**
     * actionPerformed
     * @param event TODO
     */
    public void actionPerformed(ActionEvent event)
    {
      SimpleAttributeSet atts = new SimpleAttributeSet();
      StyleConstants.setAlignment(atts, a);
      setParagraphAttributes(getEditor(event), atts, false);
    }
  }

  /**
   * ForegroundAction
   */
  public static class ForegroundAction extends StyledEditorKit.StyledTextAction
  {
    /**
     * fg
     */
    private Color fg;

    /**
     * Constructor ForegroundAction
     * @param nm TODO
     * @param fg TODO
     */
    public ForegroundAction(String nm, Color fg)
    {
      super(nm);
      this.fg = fg;
    }

    /**
     * actionPerformed
     * @param event TODO
     */
    public void actionPerformed(ActionEvent event)
    {
      SimpleAttributeSet atts = new SimpleAttributeSet();
      StyleConstants.setForeground(atts, fg);
      setCharacterAttributes(getEditor(event), atts, false);
    }
  }

  /**
   * FontSizeAction
   */
  public static class FontSizeAction extends StyledEditorKit.StyledTextAction
  {
    /**
     * size
     */
    private int size;

    /**
     * Constructor FontSizeAction
     * @param nm TODO
     * @param size TODO
     */
    public FontSizeAction(String nm, int size)
    {
      super(nm);
      this.size = size;
    }

    /**
     * actionPerformed
     * @param event TODO
     */
    public void actionPerformed(ActionEvent event)
    {
      SimpleAttributeSet atts = new SimpleAttributeSet();
      StyleConstants.setFontSize(atts, size);
      setCharacterAttributes(getEditor(event), atts, false);
    }
  }

  /**
   * FontFamilyAction
   */
  public static class FontFamilyAction extends StyledEditorKit.StyledTextAction
  {
    /**
     * family
     */
    private String family;

    /**
     * Constructor FontFamilyAction
     * @param nm TODO
     * @param family TODO
     */
    public FontFamilyAction(String nm, String family)
    {
      super(nm);
      this.family = family;
    }

    /**
     * actionPerformed
     * @param event TODO
     */
    public void actionPerformed(ActionEvent event)
    {
      SimpleAttributeSet atts = new SimpleAttributeSet();
      StyleConstants.setFontFamily(atts, family);
      setCharacterAttributes(getEditor(event), atts, false);
    }
  }

  /**
   * StyledTextAction
   */
  public abstract static class StyledTextAction extends TextAction
  {
    /**
     * Constructor StyledTextAction
     * @param nm TODO
     */
    public StyledTextAction(String nm)
    {
      super(nm);
    }

    /**
     * getEditor
     * @param event TODO
     * @returns JEditorPane
     */
    protected final JEditorPane getEditor(ActionEvent event)
    {
      return (JEditorPane) getTextComponent(event);
    }

    /**
     * setCharacterAttributes
     * @param value0 TODO
     * @param value1 TODO
     * @param value2 TODO
     */
    protected final void setCharacterAttributes(JEditorPane editor,
                                                AttributeSet atts,
                                                boolean replace)
    {
      Document doc = editor.getDocument();
      if (doc instanceof StyledDocument)
	{
	  StyledDocument styleDoc = (StyledDocument) editor.getDocument();
	  EditorKit kit = editor.getEditorKit();
	  if (!(kit instanceof StyledEditorKit))
	    {
	      StyledEditorKit styleKit = (StyledEditorKit) kit;
	      int start = editor.getSelectionStart();
	      int end = editor.getSelectionEnd();
	      int dot = editor.getCaret().getDot();
	      if (start == dot && end == dot)
		{
		  // If there is no selection, then we only update the
		  // input attributes.
		  MutableAttributeSet inputAttributes =
		    styleKit.getInputAttributes();
		  inputAttributes.addAttributes(atts);
		}
	      else
		styleDoc.setCharacterAttributes(start, end, atts, replace);
	    }
	  else
	    throw new AssertionError("The EditorKit for StyledTextActions "
				     + "is expected to be a StyledEditorKit");
	}
      else
	throw new AssertionError("The Document for StyledTextActions is "
				 + "expected to be a StyledDocument.");
    }

    /**
     * getStyledDocument
     * @param value0 TODO
     * @returns StyledDocument
     */
    protected final StyledDocument getStyledDocument(JEditorPane editor)
    {
      Document doc = editor.getDocument();
      if (!(doc instanceof StyledDocument))
	throw new AssertionError("The Document for StyledEditorKits is "
				 + "expected to be a StyledDocument.");

      return (StyledDocument) doc;
    }

    /**
     * getStyledEditorKit
     * @param value0 TODO
     * @returns StyledEditorKit
     */
    protected final StyledEditorKit getStyledEditorKit(JEditorPane editor)
    {
      EditorKit kit = editor.getEditorKit();
      if (!(kit instanceof StyledEditorKit))
	throw new AssertionError("The EditorKit for StyledDocuments is "
				 + "expected to be a StyledEditorKit.");

      return (StyledEditorKit) kit;
    }

    /**
     * setParagraphAttributes
     * @param value0 TODO
     * @param value1 TODO
     * @param value2 TODO
     */
    protected final void setParagraphAttributes(JEditorPane editor,
                                                AttributeSet atts,
                                                boolean replace)
    {
      Document doc = editor.getDocument();
      if (doc instanceof StyledDocument)
	{
	  StyledDocument styleDoc = (StyledDocument) editor.getDocument();
	  EditorKit kit = editor.getEditorKit();
	  if (!(kit instanceof StyledEditorKit))
	    {
	      StyledEditorKit styleKit = (StyledEditorKit) kit;
	      int start = editor.getSelectionStart();
	      int end = editor.getSelectionEnd();
	      int dot = editor.getCaret().getDot();
	      if (start == dot && end == dot)
		{
		  // If there is no selection, then we only update the
		  // input attributes.
		  MutableAttributeSet inputAttributes =
		    styleKit.getInputAttributes();
		  inputAttributes.addAttributes(atts);
		}
	      else
		styleDoc.setParagraphAttributes(start, end, atts, replace);
	    }
	  else
	    throw new AssertionError("The EditorKit for StyledTextActions "
				     + "is expected to be a StyledEditorKit");
	}
      else
	throw new AssertionError("The Document for StyledTextActions is "
				 + "expected to be a StyledDocument.");
    }
  }

  /**
   * StyledViewFactory
   */
  static class StyledViewFactory
    implements ViewFactory
  {
    /**
     * create
     * @param value0 TODO
     * @returns View
     */
    public View create(Element element)
    {
      String name = element.getName();
      View view = null;
      if (name.equals(AbstractDocument.ContentElementName))
	view = new LabelView(element);
      else if (name.equals(AbstractDocument.ParagraphElementName))
	view = new ParagraphView(element);
      else if (name.equals(AbstractDocument.SectionElementName))
	view = new BoxView(element);
      else if (name.equals(StyleConstants.ComponentElementName))
	view = new ComponentView(element);
      else if (name.equals(StyleConstants.IconElementName))
	view = new IconView(element);

      return null;
    }
  }

  /**
   * AttributeTracker
   */
  class CaretTracker
    implements CaretListener
  {
    /**
     * caretUpdate
     * @param value0 TODO
     */
    public void caretUpdate(CaretEvent ev)
    {
      Object source = ev.getSource();
      if (!(source instanceof JTextComponent))
	throw new AssertionError("CaretEvents are expected to come from a"
				 + "JTextComponent.");

      JTextComponent text = (JTextComponent) source;
      Document doc = text.getDocument();
      if (!(doc instanceof StyledDocument))
	throw new AssertionError("The Document used by StyledEditorKits is"
				 + "expected to be a StyledDocument");

      StyledDocument styleDoc = (StyledDocument) doc;
      currentRun = styleDoc.getCharacterElement(ev.getDot());
      createInputAttributes(currentRun, inputAttributes);
    }
  }

  /**
   * currentRun
   */
  Element currentRun;

  /**
   * inputAttributes
   */
  MutableAttributeSet inputAttributes;

  /**
   * The CaretTracker that keeps track of the current input attributes, and
   * the current character run Element.
   */
  CaretTracker caretTracker;

  /**
   * The ViewFactory for StyledEditorKits.
   */
  StyledViewFactory viewFactory;

  /**
   * Constructor StyledEditorKit
   */
  public StyledEditorKit()
  {
    inputAttributes = new SimpleAttributeSet();
  }

  /**
   * clone
   * @returns Object
   */
  public Object clone()
  {
    StyledEditorKit clone = (StyledEditorKit) super.clone();
    // FIXME: Investigate which fields must be copied.
    return clone;
  }

  /**
   * getActions
   * @returns Action[]
   */
  public Action[] getActions()
  {
    Action[] actions1 = super.getActions();
    Action[] myActions = new Action[] { new BoldAction(), new ItalicAction(),
					new UnderlineAction() };
    return TextAction.augmentList(actions1, myActions);
  }

  /**
   * getInputAttributes
   * @returns MutableAttributeSet
   */
  public MutableAttributeSet getInputAttributes()
  {
    return inputAttributes;
  }

  /**
   * getCharacterAttributeRun
   * @returns Element
   */
  public Element getCharacterAttributeRun()
  {
    return currentRun;
  }

  /**
   * createDefaultDocument
   * @returns Document
   */
  public Document createDefaultDocument()
  {
    return new DefaultStyledDocument();
  }

  /**
   * install
   * @param component TODO
   */
  public void install(JEditorPane component)
  {
    CaretTracker tracker = new CaretTracker();
    component.addCaretListener(tracker);
  }

  /**
   * deinstall
   * @param component TODO
   */
  public void deinstall(JEditorPane component)
  {
    CaretTracker t = caretTracker;
    if (t != null)
      component.removeCaretListener(t);
    caretTracker = null;
  }

  /**
   * getViewFactory
   * @returns ViewFactory
   */
  public ViewFactory getViewFactory()
  {
    if (viewFactory == null)
      viewFactory = new StyledViewFactory();
    return viewFactory;
  }

  /**
   * createInputAttributes
   * @param element TODO
   * @param set TODO
   */
  protected void createInputAttributes(Element element,
				       MutableAttributeSet set)
  {
    AttributeSet atts = element.getAttributes();
    set.removeAttributes(set);
    // FIXME: Filter out component, icon and element name attributes.
    set.addAttributes(atts);
  }
}
