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

import java.net.URL;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;

import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML.Tag;

/**
 * TODO: This class is not yet completetely implemented.
 *
 * @author Audrius Meskauskas, Lithuania (AudriusA@Bioinformatics.org)
 */
public class HTMLDocument extends DefaultStyledDocument
{
  /** A key for document properies.  The value for the key is
   * a Vector of Strings of comments not found in the body.
   */  
  public static final String AdditionalComments = "AdditionalComments";
  URL baseURL = null;
  boolean preservesUnknownTags = true;
  int tokenThreshold = Integer.MAX_VALUE;
  HTMLEditorKit.Parser parser;
  
  /**
   * Returns the parser used by this HTMLDocument to insert HTML.
   * 
   * @return the parser used by this HTMLDocument to insert HTML.
   */
  public HTMLEditorKit.Parser getParser()
  {
    return parser; 
  }
  
  /**
   * Sets the parser used by this HTMLDocument to insert HTML.
   * 
   * @param p the parser to use
   */
  public void setParser (HTMLEditorKit.Parser p)
  {
    parser = p;
  }
  /**
   * Sets the number of tokens to buffer before trying to display the
   * Document.
   * 
   * @param n the number of tokens to buffer
   */
  public void setTokenThreshold (int n)
  {
    tokenThreshold = n;
  }
  
  /**
   * Returns the number of tokens that are buffered before the document
   * is rendered.
   * 
   * @return the number of tokens buffered
   */
  public int getTokenThreshold ()
  {
    return tokenThreshold;
  }
  
  /**
   * Returns the location against which to resolve relative URLs.
   * This is the document's URL if the document was loaded from a URL.
   * If a <code>base</code> tag is found, it will be used.
   * @return the base URL
   */
  public URL getBase()
  {
    return baseURL;
  }
  
  /**
   * Sets the location against which to resolve relative URLs.
   * @param u the new base URL
   */
  public void setBase(URL u)
  {
    baseURL = u;
    //TODO: also set the base of the StyleSheet
  }
  
  /**
   * Returns whether or not the parser preserves unknown HTML tags.
   * @return true if the parser preserves unknown tags
   */
  public boolean getPreservesUnknownTags()
  {
    return preservesUnknownTags;
  }
  
  /**
   * Sets the behaviour of the parser when it encounters unknown HTML tags.
   * @param preservesTags true if the parser should preserve unknown tags.
   */
  public void setPreservesUnknownTags(boolean preservesTags)
  {
    preservesUnknownTags = preservesTags;
  }
  
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
    // TODO: Implement this properly.
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
  
  public class BlockElement extends AbstractDocument.BranchElement
  {
    public BlockElement (Element parent, AttributeSet a)
    {
      super (parent, a);
    }
    
    /**
     * Gets the resolving parent.  Since HTML attributes are not 
     * inherited at the model level, this returns null.
     */
    public AttributeSet getResolveParent()
    {
      return null;
    }
    
    /**
     * Gets the name of the element.
     * 
     * @return the name of the element if it exists, null otherwise.
     */
    public String getName()
    {
      return (String) getAttribute(NameAttribute); 
    }
  }
  
  /**
   * RunElement represents a section of text that has a set of 
   * HTML character level attributes assigned to it.
   */
  public class RunElement extends AbstractDocument.LeafElement
  {
    
    /**
     * Constructs an element that has no children. It represents content
     * within the document.
     * 
     * @param parent - parent of this
     * @param a - elements attributes
     * @param start - the start offset >= 0
     * @param end - the end offset 
     */
    public RunElement(Element parent, AttributeSet a, int start, int end)
    {
      super(parent, a, start, end);
    }
    
    /**
     * Gets the name of the element.
     * 
     * @return the name of the element if it exists, null otherwise.
     */
    public String getName()
    {
      return (String) getAttribute(NameAttribute);      
    }
    
    /**
     * Gets the resolving parent. HTML attributes do not inherit at the
     * model level, so this method returns null.
     * 
     * @return null
     */
    public AttributeSet getResolveParent()
    {
      return null;
    }
  }
  
  /**
   * A reader to load an HTMLDocument with HTML structure.
   * 
   * @author Anthony Balkissoon abalkiss at redhat dot com
   */
  public class HTMLReader extends HTMLEditorKit.ParserCallback
  {
    /** Holds the current character attribute set **/
    protected MutableAttributeSet charAttr;
    
    protected Vector parseBuffer;
    
    /** A stack for character attribute sets **/
    Stack charAttrStack = new Stack();
    
    /** A mapping between HTML.Tag objects and the actions that handle them **/
    HashMap tagToAction;
    
    /** Tells us whether we've received the '</html>' tag yet **/
    boolean endHTMLEncountered = false;    
    
    public class TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.  By default this does nothing.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // Nothing to do here.
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.  By default does nothing.
       */
      public void end(HTML.Tag t)
      {
        // Nothing to do here.
      }
    }

    public class BlockAction extends TagAction
    {      
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("BlockAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("BlockAction.end not implemented");
      }
    }
    
    public class CharacterAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("CharacterAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("CharacterAction.end not implemented");
      } 
    }
    
    public class FormAction extends SpecialAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("FormAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("FormAction.end not implemented");
      } 
    }
    
    public class HiddenAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("HiddenAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("HiddenAction.end not implemented");
      } 
    }
    
    public class IsindexAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("IsindexAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("IsindexAction.end not implemented");
      } 
    }
    
    public class ParagraphAction extends BlockAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("ParagraphAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("ParagraphAction.end not implemented");
      } 
    }
    
    public class PreAction extends BlockAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("PreAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("PreAction.end not implemented");
      } 
    }
    
    public class SpecialAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("SpecialAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("SpecialAction.end not implemented");
      }                
    }
    
    class AreaAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("AreaAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("AreaAction.end not implemented");
      } 
    }
    
    class BaseAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("BaseAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("BaseAction.end not implemented");
      } 
    }
    
    class HeadAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("HeadAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("HeadAction.end not implemented");
      } 
    }
    
    class LinkAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("LinkAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("LinkAction.end not implemented");
      } 
    }
    
    class MapAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("MapAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("MapAction.end not implemented");
      } 
    }
    
    class MetaAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("MetaAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("MetaAction.end not implemented");
      } 
    }
    
    class StyleAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("StyleAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("StyleAction.end not implemented");
      } 
    }
    
    class TitleAction extends TagAction
    {
      /**
       * This method is called when a start tag is seen for one of the types
       * of tags associated with this Action.
       */
      public void start(HTML.Tag t, MutableAttributeSet a)
      {
        // FIXME: Implement.
        System.out.println ("TitleAction.start not implemented");
      }
      
      /**
       * Called when an end tag is seen for one of the types of tags associated
       * with this Action.
       */
      public void end(HTML.Tag t)
      {
        // FIXME: Implement.
        System.out.println ("TitleAction.end not implemented");
      } 
    }    
    
    public HTMLReader(int offset)
    {
      // FIXME: What to do with offset?
      initTags();
    }
    
    public HTMLReader(int offset, int popDepth, int pushDepth,
                      HTML.Tag insertTag)
    {
      // FIXME: Implement
      initTags();
    }
    
    void initTags()
    {
      tagToAction = new HashMap(72);
      CharacterAction characterAction = new CharacterAction();
      HiddenAction hiddenAction = new HiddenAction();
      AreaAction areaAction = new AreaAction();
      BaseAction baseAction = new BaseAction();
      BlockAction blockAction = new BlockAction();
      SpecialAction specialAction = new SpecialAction();
      ParagraphAction paragraphAction = new ParagraphAction();
      HeadAction headAction = new HeadAction();
      FormAction formAction = new FormAction();
      IsindexAction isindexAction = new IsindexAction();
      LinkAction linkAction = new LinkAction();
      MapAction mapAction = new MapAction();
      PreAction preAction = new PreAction();
      MetaAction metaAction = new MetaAction();
      StyleAction styleAction = new StyleAction();
      TitleAction titleAction = new TitleAction();
      
      
      tagToAction.put(HTML.Tag.A, characterAction);
      tagToAction.put(HTML.Tag.ADDRESS, characterAction);
      tagToAction.put(HTML.Tag.APPLET, hiddenAction);
      tagToAction.put(HTML.Tag.AREA, areaAction);
      tagToAction.put(HTML.Tag.B, characterAction);
      tagToAction.put(HTML.Tag.BASE, baseAction);
      tagToAction.put(HTML.Tag.BASEFONT, characterAction);
      tagToAction.put(HTML.Tag.BIG, characterAction);
      tagToAction.put(HTML.Tag.BLOCKQUOTE, blockAction);
      tagToAction.put(HTML.Tag.BODY, blockAction);
      tagToAction.put(HTML.Tag.BR, specialAction);
      tagToAction.put(HTML.Tag.CAPTION, blockAction);
      tagToAction.put(HTML.Tag.CENTER, blockAction);
      tagToAction.put(HTML.Tag.CITE, characterAction);
      tagToAction.put(HTML.Tag.CODE, characterAction);
      tagToAction.put(HTML.Tag.DD, blockAction);
      tagToAction.put(HTML.Tag.DFN, characterAction);
      tagToAction.put(HTML.Tag.DIR, blockAction);
      tagToAction.put(HTML.Tag.DIV, blockAction);
      tagToAction.put(HTML.Tag.DL, blockAction);
      tagToAction.put(HTML.Tag.DT, paragraphAction);
      tagToAction.put(HTML.Tag.EM, characterAction);
      tagToAction.put(HTML.Tag.FONT, characterAction);
      tagToAction.put(HTML.Tag.FORM, blockAction);
      tagToAction.put(HTML.Tag.FRAME, specialAction);
      tagToAction.put(HTML.Tag.FRAMESET, blockAction);
      tagToAction.put(HTML.Tag.H1, paragraphAction);
      tagToAction.put(HTML.Tag.H2, paragraphAction);
      tagToAction.put(HTML.Tag.H3, paragraphAction);
      tagToAction.put(HTML.Tag.H4, paragraphAction);
      tagToAction.put(HTML.Tag.H5, paragraphAction);
      tagToAction.put(HTML.Tag.H6, paragraphAction);
      tagToAction.put(HTML.Tag.HEAD, headAction);
      tagToAction.put(HTML.Tag.HR, specialAction);
      tagToAction.put(HTML.Tag.HTML, blockAction);
      tagToAction.put(HTML.Tag.I, characterAction);
      tagToAction.put(HTML.Tag.IMG, specialAction);
      tagToAction.put(HTML.Tag.INPUT, formAction);
      tagToAction.put(HTML.Tag.ISINDEX, isindexAction);
      tagToAction.put(HTML.Tag.KBD, characterAction);
      tagToAction.put(HTML.Tag.LI, blockAction);
      tagToAction.put(HTML.Tag.LINK, linkAction);
      tagToAction.put(HTML.Tag.MAP, mapAction);
      tagToAction.put(HTML.Tag.MENU, blockAction);
      tagToAction.put(HTML.Tag.META, metaAction);
      tagToAction.put(HTML.Tag.NOFRAMES, blockAction);
      tagToAction.put(HTML.Tag.OBJECT, specialAction);
      tagToAction.put(HTML.Tag.OL, blockAction);
      tagToAction.put(HTML.Tag.OPTION, formAction);
      tagToAction.put(HTML.Tag.P, paragraphAction);
      tagToAction.put(HTML.Tag.PARAM, hiddenAction);
      tagToAction.put(HTML.Tag.PRE, preAction);
      tagToAction.put(HTML.Tag.SAMP, characterAction);
      tagToAction.put(HTML.Tag.SCRIPT, hiddenAction);
      tagToAction.put(HTML.Tag.SELECT, formAction);
      tagToAction.put(HTML.Tag.SMALL, characterAction);
      tagToAction.put(HTML.Tag.STRIKE, characterAction);
      tagToAction.put(HTML.Tag.S, characterAction);      
      tagToAction.put(HTML.Tag.STRONG, characterAction);
      tagToAction.put(HTML.Tag.STYLE, styleAction);
      tagToAction.put(HTML.Tag.SUB, characterAction);
      tagToAction.put(HTML.Tag.SUP, characterAction);
      tagToAction.put(HTML.Tag.TABLE, blockAction);
      tagToAction.put(HTML.Tag.TD, blockAction);
      tagToAction.put(HTML.Tag.TEXTAREA, formAction);
      tagToAction.put(HTML.Tag.TH, blockAction);
      tagToAction.put(HTML.Tag.TITLE, titleAction);
      tagToAction.put(HTML.Tag.TR, blockAction);
      tagToAction.put(HTML.Tag.TT, characterAction);
      tagToAction.put(HTML.Tag.U, characterAction);
      tagToAction.put(HTML.Tag.UL, blockAction);
      tagToAction.put(HTML.Tag.VAR, characterAction);
    }
    
    /**
     * Pushes the current character style onto the stack.
     *
     */
    protected void pushCharacterStyle()
    {
      charAttrStack.push(charAttr);
    }
    
    /**
     * Pops a character style off of the stack and uses it as the 
     * current character style.
     *
     */
    protected void popCharacterStyle()
    {
      if (!charAttrStack.isEmpty())
        charAttr = (MutableAttributeSet) charAttrStack.pop();
    }
    
    /**
     * Registers a given tag with a given Action.  All of the well-known tags
     * are registered by default, but this method can change their behaviour
     * or add support for custom or currently unsupported tags.
     * 
     * @param t the Tag to register
     * @param a the Action for the Tag
     */
    protected void registerTag(HTML.Tag t, HTMLDocument.HTMLReader.TagAction a)
    {
      tagToAction.put (t, a);
    }
    
    /**
     * This is the last method called on the HTMLReader, allowing any pending
     * changes to be flushed to the HTMLDocument.
     */
    public void flush() throws BadLocationException
    {
      // FIXME: Implement.
      System.out.println ("HTMLReader.flush not implemented yet");
    }
    
    /**
     * This method is called by the parser to indicate a block of 
     * text was encountered.  Should insert the text appropriately.
     * 
     * @param data the text that was inserted
     * @param pos the position at which the text was inserted
     */
    public void handleText(char[] data, int pos)
    {
      // FIXME: Implement.
      System.out.println ("HTMLReader.handleText not implemented yet");
    }
    
    /**
     * This method is called by the parser and should route the call to 
     * the proper handler for the tag.
     * 
     * @param t the HTML.Tag
     * @param a the attribute set
     * @param pos the position at which the tag was encountered
     */
    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
    {
      // Don't call the Action if we've already seen </html>.
      if (endHTMLEncountered)
        return;
        
      TagAction action = (TagAction) tagToAction.get(t);
      if (action != null)
        action.start(t, a);      
    }
    
    /**
     * This method called by parser to handle a comment block.
     * 
     * @param data the comment
     * @param pos the position at which the comment was encountered
     */
    public void handleComment(char[] data, int pos)
    {
      // Don't call the Action if we've already seen </html>.
      if (endHTMLEncountered)
        return;
      
      TagAction action = (TagAction) tagToAction.get(HTML.Tag.COMMENT);
      if (action != null)
        {
          action.start(HTML.Tag.COMMENT, new SimpleAttributeSet());
          action.end (HTML.Tag.COMMENT);
        }
    }
    
    /**
     * This method is called by the parser and should route the call to 
     * the proper handler for the tag.
     * 
     * @param t the HTML.Tag
     * @param pos the position at which the tag was encountered
     */
    public void handleEndTag(HTML.Tag t, int pos)
    {
      // Don't call the Action if we've already seen </html>.
      if (endHTMLEncountered)
        return;
      
      // If this is the </html> tag we need to stop calling the Actions
      if (t == HTML.Tag.HTML)
        endHTMLEncountered = true;
      
      TagAction action = (TagAction) tagToAction.get(t);
      if (action != null)
        action.end(t);
    }
    
    /**
     * This is a callback from the parser that should be routed to the 
     * appropriate handler for the tag.
     * 
     * @param t the HTML.Tag that was encountered
     * @param a the attribute set
     * @param pos the position at which the tag was encountered
     */
    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos)
    {
      // Don't call the Action if we've already seen </html>.
      if (endHTMLEncountered)
        return;
      
      TagAction action = (TagAction) tagToAction.get (t);
      if (action != null)
        {
          action.start(t, a);
          action.end(t);
        }
    }
    
    /**
     * This is invoked after the stream has been parsed but before it has been
     * flushed.
     * 
     * @param eol one of \n, \r, or \r\n, whichever was encountered the most in 
     * parsing the stream
     * @since 1.3
     */
    public void handleEndOfLineString(String eol)
    {
      // FIXME: Implement
      System.out.println ("HTMLReader.handleEndOfLineString not implemented yet");
    }
    
    /**
     * Adds the given text to the textarea document.  Called only when we are
     * within a textarea.  
     * 
     * @param data the text to add to the textarea
     */
    protected void textAreaContent(char[] data)
    {
      // FIXME: Implement.
      System.out.println ("HTMLReader.textAreaContent not implemented yet");
    }
    
    /**
     * Adds the given text that was encountered in a <PRE> element.
     * 
     * @param data the text
     */
    protected void preContent(char[] data)
    {
      // FIXME: Implement
      System.out.println ("HTMLReader.preContent not implemented yet");
    }
    
    /**
     * Instructs the parse buffer to create a block element with the given 
     * attributes.
     * 
     * @param t the tag that requires opening a new block
     * @param attr the attribute set for the new block
     */
    protected void blockOpen(HTML.Tag t, MutableAttributeSet attr)
    {
      // FIXME: Implement
      System.out.println ("HTMLReader.blockOpen not implemented yet");
    }
    
    /**
     * Instructs the parse buffer to close the block element associated with 
     * the given HTML.Tag
     * 
     * @param t the HTML.Tag that is closing its block
     */
    protected void blockClose(HTML.Tag t)
    {
      // FIXME: Implement
      System.out.println ("HTMLReader.blockClose not implement yet");
    }
    
    /**
     * Adds text to the appropriate context using the current character
     * attribute set.
     * 
     * @param data the text to add
     * @param offs the offset at which to add it
     * @param length the length of the text to add
     */
    protected void addContent(char[] data, int offs, int length)
    {
      addContent(data, offs, length, true);
    }
    
    /**
     * Adds text to the appropriate context using the current character
     * attribute set, and possibly generating an IMPLIED Tag if necessary.
     * 
     * @param data the text to add
     * @param offs the offset at which to add it
     * @param length the length of the text to add
     * @param generateImpliedPIfNecessary whether or not we should generate
     * an HTML.Tag.IMPLIED tag if necessary
     */
    protected void addContent(char[] data, int offs, int length,
                              boolean generateImpliedPIfNecessary)
    {
      // FIXME: Implement
      System.out.println ("HTMLReader.addContent not implemented yet");
    }
    
    /**
     * Adds content that is specified in the attribute set.
     * 
     * @param t the HTML.Tag
     * @param a the attribute set specifying the special content
     */
    protected void addSpecialElement(HTML.Tag t, MutableAttributeSet a)
    {
      // FIXME: Implement
      System.out.println ("HTMLReader.addSpecialElement not implemented yet");
    }
  }
  
  /**
   * Gets the reader for the parser to use when loading the document with HTML. 
   * 
   * @param pos - the starting position
   * @return - the reader
   */
  public HTMLEditorKit.ParserCallback getReader(int pos)
  {
    // FIXME: Not implemented.
    return null;
  }  
  
  /**
   * Gets the reader for the parser to use when loading the document with HTML. 
   * 
   * @param pos - the starting position
   * @param popDepth - the number of EndTagTypes to generate before inserting
   * @param pushDepth - the number of StartTagTypes with a direction 
   * of JoinNextDirection that should be generated before inserting, 
   * but after the end tags have been generated.
   * @param insertTag - the first tag to start inserting into document
   * @return - the reader
   */
  public HTMLEditorKit.ParserCallback getReader(int pos,
                                                int popDepth,
                                                int pushDepth,
                                                HTML.Tag insertTag)
  {
    // FIXME: Not Implemented.
    return null;
  }  
}
