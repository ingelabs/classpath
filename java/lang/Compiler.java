/*************************************************************************
 * Compiler.java -- Interface for implementing a method to override 
 * Object.clone()comparaing objects to obtain an ordering
 *
 * Copyright (c) 1998 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

package java.lang;

/**
 * The <code>Compiler</code> class is a place holder for a JIT
 * compiler implementation does nothing unless there is such a 
 * compiler by default.
 * <p>
 * The system property <code>java.compiler</code> may contain the name
 * of a library to load with <code>System.loadLibrary</code> when the
 * virtual machine first starts.  If so and loading the library succeeds,
 * then a function by the name of <code>java_lang_Compiler_start()</code> 
 * in that library is called.
 *
 * @since JDK 1.0
 * @see System#getProperty(java.lang.String)
 * @see System#getProperty(java.lang.String,java.lang.String)
 * @see System#loadLibrary(java.lang.String)
 */
public final class Compiler 
{
    /**
     * Compile the class named by <code>clazz</code>.
     * 
     * @param clazz the class to compile
     * @return <code>false</code> if no compiler is available or 
     * compilation failed and <code>true</code> if compilation succeeded.
     */
    public static boolean compileClass(Class clazz) 
    {
	return false;
    }
    
    /**
     * Compile the classes whose name matches <code>string</code>.
     *
     * @param string the name of classes to compile
     * @return <code>false</code> if no compiler is available or 
     * compilation failed and <code>true</code> if compilation succeeded.
     */
    public static boolean compileClasses(String string) 
    {
	return false;
    }

    /**
     * This method examines the argument and performs an operation 
     * according to the compilers documentation.  No specific operation
     * is required.
     */
    public static Object command(Object any) 
    {
	return null;
    }

    /**
     * Calling <code>Compiler.enable()</code> will cause the compiler
     * to resume operation if it was previously disabled.  
     */
    public static void enable() { }

    /**
     * Calling <code>Compiler.disable()</code> will cause the compiler
     * to be suspended.
     */
    public static void disable() { }
}
