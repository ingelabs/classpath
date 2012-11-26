/* SourceVersion.java -- Source versions of the Java programming language.
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

package javax.lang.model;

/**
 * Source versions of the Java programming language.
 * Note that this will be extended with additional
 * constants to represent new versions.
 *
 * @author Andrew John Hughes (gnu_andrew@member.fsf.org)
 * @since 1.6
 */
public enum SourceVersion
{
  /** The original version of Java. */ RELEASE_0,
  /** Java 1.1 */ RELEASE_1,
  /** Java 1.2 */ RELEASE_2,
  /** Java 1.3 */ RELEASE_3,
  /** Java 1.4 */ RELEASE_4,
  /** Java 5 */ RELEASE_5,
  /** Java 6 */ RELEASE_6;

  /**
   * Returns true if {@code name} is a syntactically valid identifier or
   * keyword in the latest version of the language.  That is, this
   * method returns true if the {@link Character#isJavaIdentifierStart(int)}
   * holds for the first character and {@link Character#isJavaIdentifierPart(int)}
   * for the rest.  This matches all regular identifiers, keywords
   * and the literals {@code true}, {@code false} and {@code null}.
   *
   * @param name the name to check.
   * @return true if {@code name} represents a valid identifier, keyword or literal.
   */
  public static boolean isIdentifier(CharSequence name)
  {
    int size = name.length();
    if (size > 0 && Character.isJavaIdentifierStart(name.charAt(0)))
      {
	for (int a = 1; a < size; ++a)
	  if (!Character.isJavaIdentifierPart(name.charAt(a)))
	    return false;
	return true;
      }
    return false;
  }

  /**
   * Returns the latest source version that can be modeled.
   *
   * @return the latest modelable source version.
   */
  public static SourceVersion latest()
  {
    return RELEASE_6;
  }

  /**
   * Returns the latest source version fully supported
   * by GNU Classpath.  Must be at least {@code RELEASE_5}.
   *
   * @return the latest supported source version.
   */
  public static SourceVersion latestSupported()
  {
    return RELEASE_5;
  }

}
