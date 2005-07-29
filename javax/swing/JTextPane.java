/* JTextPane.java --
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


package javax.swing;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;

/**
 * JTextPane
 *
 * @author Roman Kennke (roman@kennke.org)
 * @author Andrew Selkirk
 */
public class JTextPane
  extends JEditorPane
{
  /**
   * Constructor JTextPane
   */
  public JTextPane()
  {
    setEditorKit(createDefaultEditorKit());
    setDocument(null);
  }

  /**
   * Constructor JTextPane
   *
   * @param document TODO
   */
  public JTextPane(StyledDocument document)
  {
    this();
    setStyledDocument(document);
  }

  /**
   * getUIClassID
   *
   * @returns String
   */
  public String getUIClassID()
  {
    return "TextPaneUI";
  }

  /**
   * setDocument
   *
   * @param document TODO
   */
  public void setDocument(Document document)
  {
    if (document != null && !(document instanceof StyledDocument))
      throw new IllegalArgumentException("JTextPane can only handle StyledDocuments"); // TODO: Figure out exception message

    setStyledDocument((StyledDocument) document);
  }

  /**
   * getStyledDocument
   *
   * @returns StyledDocument
   */
  public StyledDocument getStyledDocument()
  {
    return (StyledDocument) super.getDocument();
  }

  /**
   * setStyledDocument
   *
   * @param document TODO
   */
  public void setStyledDocument(StyledDocument document)
  {
    super.setDocument(document);
  }

  /**
   * replaceSelection
   *
   * @param content TODO
   */
  public void replaceSelection(String content)
  {
    Caret caret = getCaret();
    StyledDocument doc = getStyledDocument();

    int dot = caret.getDot();
    int mark = caret.getMark();

    // If content is empty delete selection.
    if (content == null)
      {
	caret.setDot(dot);
	return;
      }

    try
      {
	int start = getSelectionStart();
	int end = getSelectionEnd();
	int contentLength = content.length();

	// Remove selected text.
	if (dot != mark)
	  doc.remove(start, end - start);

	// Insert new text.
	doc.insertString(start, content, null);
	// Set attributes for inserted text
	doc.setCharacterAttributes(start, contentLength, getInputAttributes(),
				   true);

	// Set dot to new position.
	setCaretPosition(start + contentLength);
      }
    catch (BadLocationException e)
      {
	throw new AssertionError
	  ("No BadLocationException should be thrown here");
      }
  }

  /**
   * insertComponent
   *
   * @param component TODO
   */
  public void insertComponent(Component component)
  {
    // TODO: One space must be inserted here with attributes set to indicate
    // that the component must be displayed here. Have to figure out the
    // attributes.
  }

  /**
   * insertIcon
   *
   * @param icon TODO
   */
  public void insertIcon(Icon icon)
  {
    // TODO: One space must be inserted here with attributes set to indicate
    // that the icon must be displayed here. Have to figure out the
    // attributes.
  }

  /**
   * addStyle
   *
   * @param nm TODO
   * @param parent TODO
   *
   * @returns Style
   */
  public Style addStyle(String nm, Style parent)
  {
    return getStyledDocument().addStyle(nm, parent);
  }

  /**
   * removeStyle
   *
   * @param nm TODO
   */
  public void removeStyle(String nm)
  {
    getStyledDocument().removeStyle(nm);
  }

  /**
   * getStyle
   *
   * @param nm TODO
   *
   * @returns Style
   */
  public Style getStyle(String nm)
  {
    return getStyledDocument().getStyle(nm);
  }

  /**
   * getLogicalStyle
   *
   * @returns Style
   */
  public Style getLogicalStyle()
  {
    return getStyledDocument().getLogicalStyle(getCaretPosition());
  }

  /**
   * setLogicalStyle
   *
   * @param style TODO
   */
  public void setLogicalStyle(Style style)
  {
    getStyledDocument().setLogicalStyle(getCaretPosition(), style);
  }

  /**
   * getCharacterAttributes
   *
   * @returns AttributeSet
   */
  public AttributeSet getCharacterAttributes()
  {
    StyledDocument doc = getStyledDocument();
    Element el = doc.getCharacterElement(getCaretPosition());
    return el.getAttributes();
  }

  /**
   * setCharacterAttributes
   *
   * @param attribute TODO
   * @param replace TODO
   */
  public void setCharacterAttributes(AttributeSet attribute,
                                     boolean replace)
  {
    int dot = getCaret().getDot();
    int start = getSelectionStart();
    int end = getSelectionEnd();
    if (start == dot && end == dot)
      // There is no selection, update insertAttributes instead
      {
	MutableAttributeSet inputAttributes =
	  getStyledEditorKit().getInputAttributes();
	inputAttributes.addAttributes(attribute);
      }
    else
      getStyledDocument().setCharacterAttributes(start, end - start, attribute,
						 replace);
  }

  /**
   * getParagraphAttributes
   *
   * @returns AttributeSet
   */
  public AttributeSet getParagraphAttributes()
  {
    StyledDocument doc = getStyledDocument();
    Element el = doc.getParagraphElement(getCaretPosition());
    return el.getAttributes();
  }

  /**
   * setParagraphAttributes
   *
   * @param attribute TODO
   * @param replace TODO
   */
  public void setParagraphAttributes(AttributeSet attribute,
                                     boolean replace)
  {
    // TODO
  }

  /**
   * getInputAttributes
   *
   * @returns MutableAttributeSet
   */
  public MutableAttributeSet getInputAttributes()
  {
    return getStyledEditorKit().getInputAttributes();
  }

  /**
   * getStyledEditorKit
   *
   * @returns StyledEditorKit
   */
  protected final StyledEditorKit getStyledEditorKit()
  {
    return (StyledEditorKit) getEditorKit();
  }

  /**
   * createDefaultEditorKit
   *
   * @returns EditorKit
   */
  protected EditorKit createDefaultEditorKit()
  {
    return new StyledEditorKit();
  }

  /**
   * setEditorKit
   *
   * @param editor TODO
   */
  public final void setEditorKit(EditorKit editor)
  {
    if (!(editor instanceof StyledEditorKit))
      throw new IllegalArgumentException("JTextPanes can only handle StyledEditorKits"); // TODO: Figure out exception message
    super.setEditorKit(editor);
  }

  /**
   * paramString
   *
   * @returns String
   */
  protected String paramString()
  {
    return super.paramString(); // TODO
  }
}
