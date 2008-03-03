/* java.lang.reflect.VMConstructor - VM interface for reflection of Java constructors
   Copyright (C) 1998, 2001, 2004, 2005 Free Software Foundation, Inc.

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


package java.lang.reflect;

import java.lang.annotation.Annotation;

final class VMConstructor
{
  /**
   * Return the raw modifiers for this constructor.  In particular
   * this will include the synthetic and varargs bits.
   * @param c the constructor concerned.
   * @return the constructor's modifiers
   */
  static native int getModifiersInternal(Constructor c);

  /**
   * Get the parameter list for this constructor, in declaration order. If the
   * constructor takes no parameters, returns a 0-length array (not null).
   *
   * @param c the constructor concerned.
   * @return a list of the types of the constructor's parameters
   */
  static native Class[] getParameterTypes(Constructor c);

  /**
   * Get the exception types this constructor says it throws, in no particular
   * order. If the constructor has no throws clause, returns a 0-length array
   * (not null).
   *
   * @param c the constructor concerned.
   * @return a list of the types in the constructor's throws clause
   */
  static native Class[] getExceptionTypes(Constructor c);

  static native Object constructNative(Object[] args, Class declaringClass,
				       int slot)
    throws InstantiationException, IllegalAccessException,
    InvocationTargetException;

  /**
   * Return the String in the Signature attribute for this constructor. If there
   * is no Signature attribute, return null.
   * @param c the constructor concerned.
   */
  static native String getSignature(Constructor c);
  
  /**
   * <p>
   * Return an array of arrays representing the annotations on each
   * of the constructor's parameters.  The outer array is aligned against
   * the parameters of the constructors and is thus equal in length to
   * the number of parameters (thus having a length zero if there are none).
   * Each array element in the outer array contains an inner array which
   * holds the annotations.  This array has a length of zero if the parameter
   * has no annotations.
   * </p>
   * <p>
   * The returned annotations are serialized.  Changing the annotations has
   * no affect on the return value of future calls to this method.
   * </p>
   * 
   * @param c the constructor concerned.
   * @return an array of arrays which represents the annotations used on the
   *         parameters of this constructor.  The order of the array elements
   *         matches the declaration order of the parameters.
   * @since 1.5
   */
  static native Annotation[][] getParameterAnnotations(Constructor c);

}
