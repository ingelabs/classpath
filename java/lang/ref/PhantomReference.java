/*
 * java.lang.ref.PhantomReference: part of the Java Class Libraries project.
 * Copyright (C) 1999 Free Software Foundation
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
package java.lang.ref;

/**
 * A phantom reference is useful, to get notified, when an object got
 * finalized.  You can't access that object though, since it is
 * finalized.  This is the reason, why <code>get()</code> always
 * returns null.
 *
 * @author Jochen Hoenicke 
 */
public class PhantomReference 
  extends Reference
{
  /**
   * Creates a new phantom reference.
   * @param referent the object that should be watched.
   * @param q the queue that should be notified, if the referent was
   * finalized.  This mustn't be <code>null</code>.
   * @exception NullPointerException if q is null.
   */
  public PhantomReference(Object referent, ReferenceQueue q)
  {
    super(referent, q);
  }
  
  /**
   * Returns the object, this reference refers to.
   * @return <code>null</code>, since the refered object may be
   * finalized and thus not accessible.  
   */
  public Object get()
  {
    return null;
  }
}
