/* FrameView.java -- Renders HTML frame tags
   Copyright (C) 2006 Free Software Foundation, Inc.

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

import java.awt.Component;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;

/**
 * A view that is responsible for rendering HTML frame tags.
 * This is accomplished by a specialized {@link ComponentView}
 * that embeds a JEditorPane with an own document.
 */
class FrameView
  extends ComponentView
{

  /**
   * Creates a new FrameView for the specified element.
   *
   * @param el the element for the view
   */
  FrameView(Element el)
  {
    super(el);
  }

  /**
   * Creates the element that will be embedded in the view.
   * This will be a JEditorPane with the appropriate content set.
   *
   * @return the element that will be embedded in the view
   */
  protected Component createComponent()
  {
    Element el = getElement();
    AttributeSet atts = el.getAttributes();
    JEditorPane html = new JEditorPane();
    URL base = ((HTMLDocument) el.getDocument()).getBase();
    String srcAtt = (String) atts.getAttribute(HTML.Attribute.SRC);
    if (srcAtt != null && ! srcAtt.equals(""))
      {
        try
          {
            URL page = new URL(base, srcAtt);
            html.setPage(page);
            System.err.println("loading: " + page);
          }
        catch (MalformedURLException ex)
          {
            // Leave page empty.
          }
        catch (IOException ex)
          {
            // Leave page empty.
          }
      }
    return html;
  }
}
