/*
 * gnu.java.lang.ArrayHelper: part of the Java Class Libraries project.
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

package gnu.java.lang;

/**
 ** ArrayHelper helps you do things with arrays.
 **
 ** @author John Keiser
 ** @version 1.1.0, 29 Jul 1998
 **/

public class ArrayHelper {
	public static boolean contains(Object[] array, Object searchFor) {
		return indexOf(array,searchFor) != -1;
	}

	public static int indexOf(Object[] array, Object searchFor) {
		for(int i=0;i<array.length;i++) {
			if(array[i].equals(searchFor)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean equalsArray(Object[] a, Object[] b) {
		if(a.length == b.length) {
			for(int i=0;i<a.length;i++) {
				if(!a[i].equals(b[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
}
