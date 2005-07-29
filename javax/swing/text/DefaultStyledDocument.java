/* DefaultStyledDocument.java --
   Copyright (C) 2004 Free Software Foundation, Inc.

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
import java.awt.Font;
import java.io.Serializable;

import javax.swing.event.DocumentEvent;

/**
 * @author Michael Koch (konqueror@gmx.de)
 */
public class DefaultStyledDocument extends AbstractDocument
  implements StyledDocument
{
  public class ElementBuffer
    implements Serializable
  {
    private Element root;

    /** Holds the offset for structural changes. */
    private int offset;

    /** Holds the length of structural changes. */
    private int length;

    public ElementBuffer(Element root)
    {
      this.root = root;
    }

    public Element getRootElement()
    {
      return root;
    }

    /**
     * Modifies the element structure so that the specified interval starts
     * and ends at an element boundary. Content and paragraph elements
     * are split and created as necessary.
     *
     * This also updates the <code>DefaultDocumentEvent</code> to reflect the
     * structural changes.
     *
     * The bulk work is delegated to {@link #changeUpdate()}.
     *
     * @param offset the start index of the interval to be changed
     * @param length the length of the interval to be changed
     * @param ev the <code>DefaultDocumentEvent</code> describing the change
     */
    public void change(int offset, int length, DefaultDocumentEvent ev)
    {
      this.offset = offset;
      this.length = length;
      changeUpdate();
    }

    /**
     * Performs the actual work for {@link #change}.
     * The elements at the interval boundaries are split up (if necessary)
     * so that the interval boundaries are located at element boundaries.
     */
    protected void changeUpdate()
    {
      // Split up the element at the start offset if necessary.
      Element el = getCharacterElement(offset);
      split(el, offset);

      int endOffset = offset + length;
      el = getCharacterElement(endOffset);
      split(el, endOffset);
    }

    /**
     * Splits an element if <code>offset</code> is not alread at its boundary.
     *
     * @param el the Element to possibly split
     * @param offset the offset at which to possibly split
     */
    void split(Element el, int offset)
    {
      if (el instanceof AbstractElement)
	{
	  AbstractElement ael = (AbstractElement) el;
	  int startOffset = ael.getStartOffset();
	  int endOffset = ael.getEndOffset();
	  int len = endOffset - startOffset;
	  if (startOffset != offset && endOffset != offset)
	    {
	      Element paragraph = ael.getParentElement();
	      if (paragraph instanceof BranchElement)
		{
		  BranchElement par = (BranchElement) paragraph;
		  Element child1 = createLeafElement(par, ael, startOffset,
						     offset);
		  Element child2 = createLeafElement(par, ael, offset,
						     endOffset);
		  int index = par.getElementIndex(startOffset);
		  par.replace(index, 1, new Element[]{ child1, child2 });
		}
	    }
	  else
	    throw new AssertionError("paragraph elements are expected to be "
				     + "instances of "
			  + "javax.swing.text.AbstractDocument.BranchElement");
	}
      else
	throw new AssertionError("content elements are expected to be "
				 + "instances of "
			+ "javax.swing.text.AbstractDocument.AbstractElement");
    }
  }
  
  public static final int BUFFER_SIZE_DEFAULT = 4096;

  protected DefaultStyledDocument.ElementBuffer buffer;
  
  public DefaultStyledDocument()
  {
    this(new GapContent(BUFFER_SIZE_DEFAULT), new StyleContext());
  }

  public DefaultStyledDocument(StyleContext context)
  {
    this(new GapContent(BUFFER_SIZE_DEFAULT), context);
  }

  public DefaultStyledDocument(AbstractDocument.Content content,
			       StyleContext context)
  {
    super(content, context);
    buffer = new ElementBuffer(createDefaultRoot());
    setLogicalStyle(0, context.getStyle(StyleContext.DEFAULT_STYLE));
  }

  public Style addStyle(String nm, Style parent)
  {
    StyleContext context = (StyleContext) getAttributeContext();
    return context.addStyle(nm, parent);
  }
  
  protected AbstractDocument.AbstractElement createDefaultRoot()
  {
    Element[] tmp;
    BranchElement section = new BranchElement(null, null);
    
    BranchElement paragraph = new BranchElement(section, null);
    tmp = new Element[1];
    tmp[0] = paragraph;
    section.replace(0, 0, tmp);

    LeafElement leaf = new LeafElement(paragraph, null, 0, 1);
    tmp = new Element[1];
    tmp[0] = leaf;
    paragraph.replace(0, 0, tmp);
    
    return section;
  }
  
  public Element getCharacterElement(int position)
  {
    Element element = getDefaultRootElement();

    while (! element.isLeaf())
      {
	int index = element.getElementIndex(position);
	element = element.getElement(index);
      }
    
    return element;
  }
  
  public Color getBackground(AttributeSet attributes)
  {
    StyleContext context = (StyleContext) getAttributeContext();
    return context.getBackground(attributes);
  }
  
  public Element getDefaultRootElement()
  {
    return buffer.getRootElement();
  }
  
  public Font getFont(AttributeSet attributes)
  {
    StyleContext context = (StyleContext) getAttributeContext();
    return context.getFont(attributes);
  }
  
  public Color getForeground(AttributeSet attributes)
  {
    StyleContext context = (StyleContext) getAttributeContext();
    return context.getForeground(attributes);
  }
  
  public Style getLogicalStyle(int position)
  {
    Element paragraph = getParagraphElement(position);
    AttributeSet attributes = paragraph.getAttributes();
    return (Style) attributes.getResolveParent();
  }
  
  public Element getParagraphElement(int position)
  {
    Element element = getCharacterElement(position);
    return element.getParentElement();
  }

  public Style getStyle(String nm)
  {
    StyleContext context = (StyleContext) getAttributeContext();
    return context.getStyle(nm);
  }

  public void removeStyle(String nm)
  {
    StyleContext context = (StyleContext) getAttributeContext();
    context.removeStyle(nm);
  }

  public void setCharacterAttributes(int offset, int length,
				     AttributeSet attributes,
				     boolean replace)
  {
    DefaultDocumentEvent ev =
      new DefaultDocumentEvent(offset, length,
			       DocumentEvent.EventType.CHANGE);

    // Modify the element structure so that the interval begins at an element
    // start and ends at an element end.
    buffer.change(offset, length, ev);

    Element root = getDefaultRootElement();
    // Visit all paragraph elements within the specified interval
    int paragraphCount =  root.getElementCount();
    for (int pindex = 0; pindex < paragraphCount; pindex++)
      {
	Element paragraph = root.getElement(pindex);
	// Skip paragraphs that lie outside the interval.
	if ((paragraph.getStartOffset() > offset + length)
	    || (paragraph.getEndOffset() < offset))
	  continue;

	// Visit content elements within this paragraph
	int contentCount = paragraph.getElementCount();
	for (int cindex = 0; cindex < contentCount; cindex++)
	  {
	    Element content = paragraph.getElement(cindex);
	    // Skip content that lies outside the interval.
	    if ((content.getStartOffset() > offset + length)
		|| (content.getEndOffset() < offset))
	      continue;

	    if (content instanceof AbstractElement)
	      {
		AbstractElement el = (AbstractElement) content;
		if (replace)
		  el.removeAttributes(el);
		el.addAttributes(attributes);
	      }
	    else
	      throw new AssertionError("content elements are expected to be"
				       + "instances of "
		       + "javax.swing.text.AbstractDocument.AbstractElement");
	  }
      }
  }
  
  public void setLogicalStyle(int position, Style style)
  {
    Element el = getParagraphElement(position);
    if (el instanceof AbstractElement)
      {
        AbstractElement ael = (AbstractElement) el;
        ael.setResolveParent(style);
      }
    else
      throw new AssertionError("paragraph elements are expected to be"
         + "instances of javax.swing.text.AbstractDocument.AbstractElement");
  }

  public void setParagraphAttributes(int offset, int length,
				     AttributeSet attributes,
				     boolean replace)
  {
    // FIXME: Implement me.
    throw new Error("not implemented");
  }
}
