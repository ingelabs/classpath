/* VMSecurityManager.java
   Copyright (C) 1998 Free Software Foundation

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
Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA
02111-1307 USA.

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
	static native Class[] getClassContext();

	/** Get the current ClassLoader--the one nearest to the
	 ** top of the stack.
	 ** @return the current ClassLoader.
	 **/
	static native ClassLoader currentClassLoader();
}
