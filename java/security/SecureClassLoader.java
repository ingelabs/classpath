/* SecureClassLoader.java --- A Secure Class Loader
   
  Copyright (c) 1999 by Free Software Foundation, Inc.
  Written by Mark Benvenuto <ivymccough@worldnet.att.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU Library General Public License as published 
  by the Free Software Foundation, version 2. (see COPYING.LIB)

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation
  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307 USA. */

package java.security;

/**
	A Secure Class Loader for loading classes with additional 
	support for specifying code source and permissions when
	they are retrieved by the system policy handler.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public class SecureClassLoader extends ClassLoader
{

private ClassLoader parent;

/**
	Creates a new SecureClassLoader specifying the parent to make
	calls back to.

	If there is a security manager it first calls,
	checkCreateClassLoader to ensure creation of the class loader 
	is allowed.

	@param ClassLoader parent class loader

	@throws SecurityException if security manager exists and denies
		access.
*/
protected SecureClassLoader(ClassLoader parent)
{
	SecurityManager sm = System.getSecurityManager();
	if(sm != null)
		sm.checkCreateClassLoader();
	this.parent = parent;
}

/**
	Creates a new SecureClassLoader using the default parent to make
	calls back to.

	If there is a security manager it first calls,
	checkCreateClassLoader to ensure creation of the class loader 
	is allowed.

	@throws SecurityException if security manager exists and denies
		access.
*/
protected SecureClassLoader()
{
	SecurityManager sm = System.getSecurityManager();
	if(sm != null)
		sm.checkCreateClassLoader();
	this.parent = ClassLoader.getSystemClassLoader();
}

/** 
	Creates a class using an array of bytes and a 
	CodeSource.

	@param name the name to give the class.  null if unknown.
	@param b the data representing the classfile, in classfile format.
	@param off the offset into the data where the classfile starts.
	@param len the length of the classfile data in the array.
	@param cs the CodeSource for the class

	@return the class that was defined and optional CodeSource.

	@exception ClassFormatError if the byte array is not in proper classfile format.
*/
protected final Class defineClass(String name, byte[] b, int off, int len, CodeSource cs)
{
	ProtectionDomain protectionDomain = new ProtectionDomain( codesource, getPermissions( codesource ) );
	try {

		Class c = parent.defineClass(name, b, off, len, protectionDomain);
		return c;
	} catch( ClassFormatError cfe )	{
		return null;
	}
}

/**
	Returns a PermissionCollection for the specified CodeSource.
	The default implmentation invokes 
	java.security.Policy.getPermissions.

	This method is called by defineClass that takes a CodeSource
	arguement to build a proper ProtectionDomain for the class
	being defined.
	
*/
protected PermissionCollection getPermissions(CodeSource codesource)
{
	Policy policy = Policy.getPolicy();
	return policy.getPermissions( codesource );
}

}
