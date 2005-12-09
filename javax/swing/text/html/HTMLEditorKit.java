/* HTMLEditorKit.java --
   Copyright (C) 2005 Free Software Foundation, Inc.

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

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;

import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * This class is NOT implemented. This file currently holds only
 * declarations of the two enclosing classes, necessary for testing
 * the implemented javax.swing.text.html.parser package.
 *
 * @author No authorship is taken, implement the class and be!
 * TODO: replace this header after implementing the class.
 */
public class HTMLEditorKit
  extends StyledEditorKit
  implements Serializable, Cloneable
{
  /**
   * The abstract HTML parser declaration.
   */
  public abstract static class Parser
  {
    /**
     * Parse the HTML text, calling various methods of the provided callback
     * in response to the occurence of the corresponding HTML constructions.
     * @param reader The reader to read the source HTML from.
     * @param callback The callback to receive information about the parsed
     * HTML structures
     * @param ignoreCharSet If true, the parser ignores all charset information
     * that may be present in HTML documents.
     * @throws IOException, normally if the reader throws one.
     */
    public abstract void parse(Reader reader, ParserCallback callback,
                               boolean ignoreCharSet
                              )
                        throws IOException;
  }

  /**
   * The "hook" that receives all information about the HTML document
   * structure while parsing it. The methods are invoked by parser
   * and should be normally overridden.
   */
  public static class ParserCallback
  {
    /**
     * If the tag does not occurs in the html stream directly, but
     * is supposed by parser, the tag attribute set contains this additional
     * attribute, having value Boolean.True.
     */
    public static final Object IMPLIED = "_implied_";

    /**
     * The parser calls this method after it finishes parsing the document.
     */
    public void flush() throws BadLocationException
    {
      // TODO: What to do here, if anything?
    }

    /**
     * Handle HTML comment, present in the given position.
     * @param comment the comment
     * @position the position of the comment in the text being parsed.
     */
    public void handleComment(char[] comment, int position)
    {
      // TODO: What to do here, if anything?
    }

    /**
     * Notifies about the character sequences, used to separate lines in
     * this document. The parser calls this method after it finishes
     * parsing the document, but before flush().
     * @param end_of_line The "end of line sequence", one of: \r or \n or \r\n.
     */
    public void handleEndOfLineString(String end_of_line)
    {
      // TODO: What to do here, if anything?
    }

    /**
     * The method is called when the HTML closing tag ((like &lt;/table&gt;)
     * is found or if the parser concludes that the one should be present
     * in the current position.
     * @param tag The tag being handled
     * @param position the tag position in the text being parsed.
     */
    public void handleEndTag(HTML.Tag tag, int position)
    {
      // TODO: What to do here, if anything?
    }

    /**
     * Handle the error.
     * @param message The message, explaining the error.
     * @param position The starting position of the fragment that has caused
     * the error in the html document being parsed.
     */
    public void handleError(String message, int position)
    {
      // TODO: What to do here, if anything?
    }

    /**
     * Handle the tag with no content, like &lt;br&gt;. The method is
     * called for the elements that, in accordance with the current DTD,
     * has an empty content.
     * @param tag The tag being handled.
     * @param position The tag position in the text being parsed.
     */
    public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet attributes,
                                int position)
    {
      // TODO: What to do here, if anything?
    }

    /**
     * The method is called when the HTML opening tag ((like &lt;table&gt;)
     * is found or if the parser concludes that the one should be present
     * in the current position.
     * @param tag The tag being handled
     * @param position The tag position in the text being parsed
     */
    public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributes,
                               int position
                              )
    {
      // TODO: What to do here, if anything?
    }

    /**
     * Handle the text section.
     * @param text A section text.
     * @param position The text position in the HTML document text being parsed.
     */
    public void handleText(char[] text, int position)
    {
      // TODO: What to do here, if anything?
    }
  }

  /**
   * Use serialVersionUID (v1.4) for interoperability.
   */
  private static final long serialVersionUID = 8751997116710384592L;

  /**
   * Default cascading stylesheed file ("default.css").
   */
  public static final String DEFAULT_CSS = "default.css";

  /**
   * The <b>bold</b> action identifier.
   */
  public static final String BOLD_ACTION = "html-bold-action";

  /**
   * The <i>italic</i> action identifier.
   */
  public static final String ITALIC_ACTION = "html-italic-action";

  /**
   * The <font color="#FF0000">color</font> action indentifier
   * (passing the color as an argument).
   */
  public static final String COLOR_ACTION = "html-color-action";

  /**
   * The <font size="+1">increase</font> font action identifier.
   */
  public static final String FONT_CHANGE_BIGGER = "html-font-bigger";

  /**
   * The <font size="-1">decrease</font> font action identifier.
   */
  public static final String FONT_CHANGE_SMALLER = "html-font-smaller";

  /**
   * Align images at the bottom.
   */
  public static final String IMG_ALIGN_BOTTOM = "html-image-align-bottom";

  /**
   * Align images at the middle.
   */
  public static final String IMG_ALIGN_MIDDLE = "html-image-align-middle";

  /**
   * Align images at the top.
   */
  public static final String IMG_ALIGN_TOP = "html-image-align-top";

  /**
   * Align images at the border.
   */
  public static final String IMG_BORDER = "html-image-border";

  /**
   * The "logical style" action identifier, passing that style as parameter.
   */
  public static final String LOGICAL_STYLE_ACTION = "html-logical-style-action";

  /**
   * The "ident paragraph left" action.
   */
  public static final String PARA_INDENT_LEFT = "html-para-indent-left";

  /**
   * The "ident paragraph right" action.
   */
  public static final String PARA_INDENT_RIGHT = "html-para-indent-right";

  /**
   * The ViewFactory for HTMLFactory.
   */
  HTMLFactory viewFactory;
  
  /**
   * Create a text storage model for this type of editor.
   *
   * @return the model
   */
  public Document createDefaultDocument()
  {
    HTMLDocument document = new HTMLDocument();
    return document;
  }

  /**
   * Get the parser that this editor kit uses for reading HTML streams. This
   * method can be overridden to use the alternative parser.
   *
   * @return the HTML parser (by default, {@link ParserDelegator}).
   */
  protected Parser getParser()
  {
    return new ParserDelegator();
  }
  
  /**
   * Inserts HTML into an existing document.
   * 
   * @param doc - the Document to insert the HTML into.
   * @param offset - where to begin inserting the HTML.
   * @param html - the String to insert
   * @param popDepth - the number of ElementSpec.EndTagTypes 
   * to generate before inserting
   * @param pushDepth - the number of ElementSpec.StartTagTypes 
   * with a direction of ElementSpec.JoinNextDirection that 
   * should be generated before
   * @param insertTag - the first tag to start inserting into document
   * @throws IOException - on any I/O error
   * @throws BadLocationException - if pos represents an invalid location
   * within the document
   */
  public void insertHTML(HTMLDocument doc, int offset, String html,
                         int popDepth, int pushDepth, HTML.Tag insertTag)
      throws BadLocationException, IOException
  {
    // FIXME: Not implemented.
  }
  
  /**
   * Inserts content from the given stream. Inserting HTML into a non-empty 
   * document must be inside the body Element, if you do not insert into 
   * the body an exception will be thrown. When inserting into a non-empty 
   * document all tags outside of the body (head, title) will be dropped.
   * 
   * @param in - the stream to read from
   * @param doc - the destination for the insertion
   * @param pos - the location in the document to place the content
   * @throws IOException - on any I/O error
   * @throws BadLocationException - if pos represents an invalid location
   * within the document
   */
  public void read(Reader in, Document doc, int pos) throws IOException,
      BadLocationException
  {
    // FIXME: Not implemented.
  }
  
  /**
   * Writes content from a document to the given stream in 
   * an appropriate format.
   * 
   * @param out - the stream to write to
   * @param doc - the source for the write
   * @param pos - the location in the document to get the content.
   * @param len - the amount to write out
   * @throws IOException - on any I/O error
   * @throws BadLocationException - if pos represents an invalid location
   * within the document
   */
  public void write(Writer out, Document doc, int pos, int len)
      throws IOException, BadLocationException
  {
    // FIXME: Not implemented.
  }
  
  /**
   * Gets the content type that the kit supports.
   * This kit supports the type text/html.
   * 
   * @returns the content type supported.
   */
  public String getContentType()
  {
    return "text/html";
  }
  
  /**
   * Gets a factory suitable for producing views of any 
   * models that are produced by this kit.
   * 
   * @return the view factory suitable for producing views.
   */
  public ViewFactory getViewFactory()
  {
    if (viewFactory == null)
      viewFactory = new HTMLFactory();
    return viewFactory;
  }
  
  /**
   * A {@link ViewFactory} that is able to create {@link View}s for
   * the <code>Element</code>s that are supported.
   */
  public static class HTMLFactory
    implements ViewFactory
  {
    /**
     * Creates a {@link View} for the specified <code>Element</code>.
     *
     * @param element the <code>Element</code> to create a <code>View</code>
     *        for
     * @return the <code>View</code> for the specified <code>Element</code>
     *         or <code>null</code> if the type of <code>element</code> is
     *         not supported
     */
    public View create(Element element)
    {
      View view = null;
      Object attr = element.getAttributes().getAttribute(
                                StyleConstants.NameAttribute);
      if (attr instanceof HTML.Tag)
        {
          HTML.Tag tag = (HTML.Tag) attr;

          if (tag.equals(HTML.Tag.IMPLIED) || tag.equals(HTML.Tag.P)
              || tag.equals(HTML.Tag.H1) || tag.equals(HTML.Tag.H2)
              || tag.equals(HTML.Tag.H3) || tag.equals(HTML.Tag.H4)
              || tag.equals(HTML.Tag.H5) || tag.equals(HTML.Tag.H6)
              || tag.equals(HTML.Tag.DT))
            view = new ParagraphView(element);
          else if (tag.equals(HTML.Tag.LI) || tag.equals(HTML.Tag.DL)
                   || tag.equals(HTML.Tag.DD) || tag.equals(HTML.Tag.BODY)
                   || tag.equals(HTML.Tag.HTML) || tag.equals(HTML.Tag.CENTER)
                   || tag.equals(HTML.Tag.DIV)
                   || tag.equals(HTML.Tag.BLOCKQUOTE)
                   || tag.equals(HTML.Tag.PRE))
            view = new BlockView(element, View.Y_AXIS);
          
          // FIXME: Uncomment when the views have been implemented
         /* else if (tag.equals(HTML.Tag.CONTENT))
            view = new InlineView(element); 
          else if (tag.equals(HTML.Tag.MENU) || tag.equals(HTML.Tag.DIR)
                   || tag.equals(HTML.Tag.UL) || tag.equals(HTML.Tag.OL))
            view = new ListView(element);
          else if (tag.equals(HTML.Tag.IMG))
            view = new ImageView(element);
          else if (tag.equals(HTML.Tag.HR))
            view = new HRuleView(element);
          else if (tag.equals(HTML.Tag.BR))
            view = new BRView(element);
          else if (tag.equals(HTML.Tag.TABLE))
            view = new TableView(element);
          else if (tag.equals(HTML.Tag.INPUT) || tag.equals(HTML.Tag.SELECT)
                   || tag.equals(HTML.Tag.TEXTAREA))
            view = new FormView(element);
          else if (tag.equals(HTML.Tag.OBJECT))
            view = new ObjectView(element);
          else if (tag.equals(HTML.Tag.FRAMESET))
            view = new FrameSetView(element);
          else if (tag.equals(HTML.Tag.FRAME))
            view = new FrameView(element); */
        }      
      
      if (view == null)
        {
          String name = element.getName();
          if (name.equals(AbstractDocument.ContentElementName))
            view = new LabelView(element);
          else if (name.equals(AbstractDocument.ParagraphElementName))
            view = new ParagraphView(element);
          else if (name.equals(AbstractDocument.SectionElementName))
            view = new BoxView(element, View.Y_AXIS);
          else if (name.equals(StyleConstants.ComponentElementName))
            view = new ComponentView(element);
          else if (name.equals(StyleConstants.IconElementName))
            view = new IconView(element);
        }
      return view;
    }
  }
}