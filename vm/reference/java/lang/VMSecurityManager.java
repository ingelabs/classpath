/*
 * java.lang.SecurityManager: part of the Java Class Libraries project.
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

import java.net.*;
import java.util.*;
import java.io.*;

/**
 ** VMSecurityManager is a helper class for SecurityManager the VM must
 ** implement.
 **
 ** @author  John Keiser
 ** @version 1.1.0, 31 May 1998
 **/
class VMSecurityManager {
	/** Get a list of all the classes currently executing
	 ** methods on the Java stack.  getClassContext()[0] is
	 ** the currently executing method
	 ** <STRONG>Spec Note:</STRONG> does not say whether
	 ** the stack will include the getClassContext() call or
	 ** the one just before it.
	 **
	 ** @return an array containing all the methods on classes
	 **         on the Java execution stack.
	 **/
	protected static Class[] getClassContext() {
		throw new UnsupportedOperationException();
	}
}
