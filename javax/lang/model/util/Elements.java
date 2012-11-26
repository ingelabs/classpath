/* Elements.java -- Utility methods for operating on elements.
   Copyright (C) 2012  Free Software Foundation, Inc.

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

package javax.lang.model.util;

import java.io.Writer;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Utility methods for operating on elements.
 *
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.6
 */
public interface Elements
{

  /**
   * Returns all members of a type element, whether declared
   * directly in that element or inherited.  For a class element,
   * this includes all constructors, but not local or anonymous
   * classes.
   *
   * @param type the type to return the members of.
   * @return all members of the type.
   * @see Element#getEnclosedElements()
   */
  List<? extends Element> getAllMembers(TypeElement type);

  /**
   * Returns the text of a constant expression which represents
   * either a primitive value or a {@link String}.  The returned
   * text is in a form suitable for inclusion in source code.
   *
   * @param value a primitive value or string.
   * @return the text of the constant expression.
   * @throws IllegalArgumentExpression if the argument is not a
   *         primitive value or string.
   */
  String getConstantExpression(Object value);

  /**
   * Returns the text of the documentation comment attached to
   * an element.
   *
   * @param elem the element whose comment should be returned.
   * @return the documentation comment, or {@code null} if there is none.
   */
  String getDocComment(Element elem);

  /**
   * Returns a type element, given its canonical name.
   *
   * @param name the canonical name of the element.
   * @return the named type element or {@code null} if it wasn't found.
   */
  TypeElement getTypeElement(CharSequence name);

  /**
   * Tests whether a type, method or field hides another.
   *
   * @param hider the element that is doing the hiding.
   * @param hidden the element hidden by the hider.
   * @return true if, and only if, the hider hides the hidden element.
   */
  boolean hides(Element hider, Element hidden);

  /**
   * Returns true if the specified element has been deprecated.
   *
   * @param elem the element to check for deprecation.
   * @return true if the element is deprecated.
   */
  boolean isDeprecated(Element elem);

  /**
   * Prints out a representation of the elements in the order specified
   * using the supplied writer.  The main purpose of this method is for
   * debugging purposes and the format of the output is not defined.
   *
   * @param writer the writer to send the output to.
   * @param elements the elements to print.
   */
  void printElements(Writer w, Element... elements);

}
