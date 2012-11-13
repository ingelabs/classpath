/* TypeElement.java -- Represents a class or interface element.
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

package javax.lang.model.element;

/**
 * <p>Represents a class or interface program element.
 * Note that enumerations are a kind of class and annotations
 * are a kind of interface.  The element provides access
 * to information about the type and its members.</p>
 * <p>A distinction is made between elements and types,
 * with the latter being an invocation of the former.
 * This distinction is most clear when looking at
 * parameterized types.  A {@code TypeElement} represents the
 * general type, such as {@code java.util.Set}, while
 * a {@link DeclaredType} instance represents different
 * instantiations such as {@code java.util.Set<String>},
 * {@code java.util.Set<Integer>} and the raw type
 * {@code java.util.Set}.</p>
 * <p>The methods of this interface return elements in the
 * natural order of the underlying information.  So, for
 * example, if the element is derived from a Java source
 * file, elements are returned in the order they appear
 * in the source code.</p>
 *
 * @since 1.6
 */
public interface TypeElement
  extends Element
{
}
