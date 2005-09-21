/* HTMLDocument.java --
   Copyright (C) 2005  Free Software Foundation, Inc.

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


package javax.swing.text.html;

import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.ElementIterator;
import javax.swing.text.html.HTML.Tag;

/**
 * TODO: This class is not yet completetely implemented.
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class HTMLDocument extends DefaultStyledDocument
{

  /**
   * An iterator to iterate through LeafElements in the document.
   */
  class LeafIterator extends Iterator
  {
    HTML.Tag tag;
    HTMLDocument doc;
    ElementIterator it;

    public LeafIterator (HTML.Tag t, HTMLDocument d)
    {
      doc = d;
      tag = t;
      it = new ElementIterator(doc);
    }
    
    /**
     * Return the attributes for the tag associated with this iteartor
     * @return the AttributeSet
     */
    public AttributeSet getAttributes()
    {
      if (it.current() != null)
        return it.current().getAttributes();
      return null;
    }

    /**
     * Get the end of the range for the current occurrence of the tag
     * being defined and having the same attributes.
     * @return the end of the range
     */
    public int getEndOffset()
    {
      if (it.current() != null)
        return it.current().getEndOffset();
      return -1;
    }

    /**
     * Get the start of the range for the current occurrence of the tag
     * being defined and having the same attributes.
     * @return the start of the range (-1 if it can't be found).
     */

    public int getStartOffset()
    {
      if (it.current() != null)
        return it.current().getStartOffset();
      return -1;
    }

    /**
     * Advance the iterator to the next LeafElement .
     */
    public void next()
    {
      it.next();
      while (it.current()!= null && !it.current().isLeaf())
        it.next();
    }

    /**
     * Indicates whether or not the iterator currently represents an occurrence
     * of the tag.
     * @return true if the iterator currently represents an occurrence of the
     * tag.
     */
    public boolean isValid()
    {
      return it.current() != null;
    }

    /**
     * Type of tag for this iterator.
     */
    public Tag getTag()
    {
      return tag;
    }

  }

  public void processHTMLFrameHyperlinkEvent(HTMLFrameHyperlinkEvent event)
  {
  }
  
  /**
   * Gets an iterator for the given HTML.Tag.
   * @param t the requested HTML.Tag
   * @return the Iterator
   */
  public HTMLDocument.Iterator getIterator (HTML.Tag t)
  {
    return new HTMLDocument.LeafIterator(t, this);
  }
  
  /**
   * An iterator over a particular type of tag.
   */
  public abstract static class Iterator
  {
    /**
     * Return the attribute set for this tag.
     * @return the <code>AttributeSet</code> (null if none found).
     */
    public abstract AttributeSet getAttributes();
    
    /**
     * Get the end of the range for the current occurrence of the tag
     * being defined and having the same attributes.
     * @return the end of the range
     */
    public abstract int getEndOffset();
    
    /**
     * Get the start of the range for the current occurrence of the tag
     * being defined and having the same attributes.
     * @return the start of the range (-1 if it can't be found).
     */
    public abstract int getStartOffset();
    
    /**
     * Move the iterator forward.
     */
    public abstract void next();
    
    /**
     * Indicates whether or not the iterator currently represents an occurrence
     * of the tag.
     * @return true if the iterator currently represents an occurrence of the
     * tag.
     */
    public abstract boolean isValid();
    
    /**
     * Type of tag this iterator represents.
     * @return the tag.
     */
    public abstract HTML.Tag getTag();
  }
}
