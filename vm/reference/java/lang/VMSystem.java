/*
 * java.lang.System: part of the Java Class Libraries project.
 * Copyright (C) 1998 John Keiser
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
 ** VMSystem is a package-private helper class for System that the
 ** VM must implement.
 **
 ** @author John Keiser
 ** @version 1.1.0, Aug 8 1998
 **/

class VMSystem {
	Properties getSystemProperties() {
		return new Properties();
	}

	/** Copy one array onto another from
	 ** <CODE>src[srcStart] ... src[srcStart+len]</CODE> to
	 ** <CODE>dest[destStart] ... dest[destStart+len]</CODE>
	 ** @param src the array to copy elements from
	 ** @param srcStart the starting position to copy elements
	 **        from in the src array
	 ** @param dest the array to copy elements to
	 ** @param destStart the starting position to copy
	 **        elements from in the src array
	 ** @param len the number of elements to copy
	 ** @exception ArrayStoreException if src or dest is not
	 **            an array, or if one is a primitive type
	 **            and the other is a reference type or a
	 **            different primitive type.  The array will
	 **            not be modified if any of these is the
	 **            case.  If there is an element in src that
	 **            is not assignable to dest's type, this will
	 **            be thrown and all elements up to but not
	 **            including that element will have been
	 **            modified.
	 ** @exception ArrayIndexOutOfBoundsException if len is
	 **            negative, or if the start or end copy
	 **            position in either array is out of bounds.
	 **            The array will not be modified if this
	 **            exception is thrown.
	 **/
	static void arraycopy(Object src, int srcStart, Object dest, int destStart, int len) {
		throw new UnsupportedOperationException();
	}

	/** Get a hash code computed by the VM for the Object.
	 ** This hash code will be the same as Object's hashCode()
	 ** method.  It is usually some convolution of the pointer
	 ** to the Object internal to the VM.  It follows standard
	 ** hash code rules, in that it will remain the same for a
	 ** given Object for the lifetime of that Object.
	 ** @param o the Object to get the hash code for
	 ** @return the VM-dependent hash code for this Object
	 **/
	static int identityHashCode(Object o) {
		throw new UnsupportedOperationException();
	}
}
