/*
 * java.lang.Runnable: part of the Java Class Libraries project.
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

/**
 ** Runnable is an interface you implement to indicate that your class can be
 ** executed as the main part of a Thread, among other places.  When you want
 ** an entry point to run a piece of code, implement this interface and
 ** override run.
 **
 ** @author Paul Fisher
 ** @version 1.1.0, 5 Feb 1998
 ** @since JDK1.0
 **/

public abstract interface Runnable {
  /** This method will be called by whoever wishes to run your class
   ** implementing Runnable.
   ** @since JDK1.0
   **/
  public abstract void run();
}
