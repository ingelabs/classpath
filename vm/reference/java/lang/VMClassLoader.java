/* VMClassLoader.java
   Copyright (C) 1998, 2001 Free Software Foundation

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

/**
 * java.lang.VMClassLoader is a package-private helper for VMs to implement
 * on behalf of java.lang.ClassLoader.
 *
 * @author John Keiser
 * @author Mark Wielaard (mark@klomp.org)
 * @since CP1.1
 */

class VMClassLoader {

    /** 
     * Helper to define a class using a string of bytes.
     * 
     * @param name the name to give the class.  null if unknown.
     * @param data the data representing the classfile, in classfile format.
     * @param offset the offset into the data where the classfile starts.
     * @param len the length of the classfile data in the array.
     * @return the class that was defined.
     * @exception ClassFormatError if the byte array is not in proper
     *            classfile format.
     */
    final static native Class defineClass(ClassLoader cl, String name, 
	     byte[] data, int offset, int len) throws ClassFormatError;
    
    /** 
     * Helper to resolve all references to other classes from this class.
     * @param c the class to resolve.
     */
    final static native void resolveClass(Class c);

    /** 
     * Helper for java.lang.Integer, Byte, etc to get the TYPE class
     * at initialization time. The type code is one of the chars that
     * represents the primitive type as in JNI.
     *
     * <ul>
     * <li>'Z' - boolean</li>
     * <li>'B' - byte</li>
     * <li>'C' - char</li>
     * <li>'D' - double</li>
     * <li>'F' - float</li>
     * <li>'I' - int</li>
     * <li>'J' - long</li>
     * <li>'S' - short</li>
     * <li>'V' - void</li>
     * </ul>
     *
     * Note that this is currently a java version that converts the type code
     * to a string and calls the native <code>getPrimitiveClass(String)</code>
     * method for backwards compatibility with VMs that used old versions of
     * GNU Classpath. Please replace this method with a native method
     * <code>final static native Class getPrimitiveClass(char type);</code>
     * if your VM supports it. <strong>The java version of this method and
     * the String version of this method will disappear in a future version
     * of GNU Classpath</strong>.
     *
     * @param type the primitive type
     * @return a "bogus" class representing the primitive type.
     */
    final static Class getPrimitiveClass(char type)
    {
      String t;
      switch (type) {
	case 'Z':
	  t = "boolean";
	  break;
	case 'B':
	  t = "byte";
	  break;
	case 'C':
	  t = "char";
	  break;
	case 'D':
	  t = "double";
	  break;
	case 'F':
	  t = "float";
	  break;
	case 'I':
	  t = "int";
	  break;
	case 'J':
	  t = "long";
	  break;
	case 'S':
	  t = "short";
	  break;
	case 'V':
	  t = "void";
	  break;
	default:
	  throw new NoClassDefFoundError("Invalid type specifier: " + type);
      }
      return getPrimitiveClass(t);
    }

    /**
     * Old version of the interface, added here for backwards compatibility.
     * Called by the java version of getPrimitiveClass(char) when no native
     * version of that method is available.
     * <strong>This method will be removed in a future version of GNU
     * Classpath</strong>.
     */
    final static native Class getPrimitiveClass(String type);
}
