/////////////////////////////////////////////////////////////////////////////
// Dictionary.java -- an abstract (and essentially worthless) 
//                    class which is Hashtable's superclass
//
// This is a JDK 1.2 compliant version of Dictionary.java
//
// Copyright (c) 1998 by Jon A. Zeppieri (jon@eease.com)
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU Library General Public License as published
// by the Free Software Foundation, version 2. (see COPYING.LIB)
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; if not, write to the Free Software Foundation
// Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/////////////////////////////////////////////////////////////////////////////

package java.util;

/**
 * A Dictionary maps keys to values; <i>how</i> it does that is
 * implementation-specific.
 * 
 * This is an abstract class which has really gone by the wayside.
 * People at Javasoft are probably embarrassed by it.  At this point,
 * it might as well be an interface rather than a class, but it remains
 * this poor, laugable skeleton for the sake of backwards compatibility.
 * At any rate, this was what came before the <pre>Map</pre> interface 
 * in the Collections framework.
 *
 * @author      Jon Zeppieri
 * @version     $Revision: 1.1 $
 * @modified    $Id: Dictionary.java,v 1.1 1998-10-13 00:38:35 jaz Exp $
 */
public abstract class Dictionary extends Object
{
    /** returns an Enumeration of the values in this Dictionary */
    public abstract Enumeration elements();

    /** 
     * returns the value associated with the supplied key, or null
     * if no such value exists
     *
     * @param    key      the key to use to fetch the value
     */
    public abstract Object get(Object key);

    /** returns true IFF there are no elements in this Dictionary (size() == 0) */
    public abstract boolean isEmpty();

    /** returns an Enumeration of the keys in this Dictionary */
    public abstract Enumeration keys();

    /**
     * inserts a new value into this Dictionary, located by the
     * supllied key; note: Dictionary's subclasses (all 1 of them)
     * do not support null keys or values (I can only assume this
     * would have been more general) 
     *
     * @param      key      the key which locates the value
     * @param      value    the value to put into the Dictionary
     */
    public abstract Object put(Object key, Object value);

    /**
     * removes fro the Dictionary the value located by the given key
     *
     * @param       key      the key used to locate the value to be removed
     */
    public abstract Object remove(Object key);

    /** returns the number of values currently in this Dictionary */
    public abstract int size();
}
