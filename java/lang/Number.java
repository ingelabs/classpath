/*
 * java.lang.Number: part of the Java Class Libraries project.
 * Copyright (C) 1998 Free Software Foundation
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.lang;

import java.io.Serializable;

/**
 ** Number is a generic superclass of all the numeric classes, namely
 ** <code>Byte</code>, <code>Short</code>, <code>Integer</code>,
 ** <code>Long</code>, <code>Float</code>, and <code>Double</code>.
 **
 ** It provides ways to convert from any one value to any other.
 **
 ** @author Paul Fisher
 ** @author John Keiser
 ** @since JDK1.0
 **/
public abstract class Number implements Serializable
{
  /** Return the value of this <code>Number</code> as a <code>byte</code>.
   ** @return the value of this <code>Number</code> as a <code>byte</code>.
   **/
  public byte byteValue() {
    return (byte) intValue();
  }

  /** Return the value of this <code>Number</code> as a <code>short</code>.
   ** @return the value of this <code>Number</code> as a <code>short</code>.
   **/
  public short shortValue() {
    return (short) intValue();
  }

  /** Return the value of this <code>Number</code> as an <code>int</code>.
   ** @return the value of this <code>Number</code> as an <code>int</code>.
   **/
  public abstract int intValue();

  /** Return the value of this <code>Number</code> as a <code>long</code>.
   ** @return the value of this <code>Number</code> as a <code>long</code>.
   **/
  public abstract long longValue();

  /** Return the value of this <code>Number</code> as a <code>float</code>.
   ** @return the value of this <code>Number</code> as a <code>float</code>.
   **/
  public abstract float floatValue();

  /** Return the value of this <code>Number</code> as a <code>float</code>.
   ** @return the value of this <code>Number</code> as a <code>float</code>.
   **/
  public abstract double doubleValue();
}

