/*
 * java.lang.reflect.Constructor: part of the Java Class Libraries project.
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

package java.lang.reflect;

/**
 ** Constructor represents a constructor of a class.
 ** It will allow you to create a new instance of the class or get generic information about the constructor.<P>
 **
 ** <B>Note:</B> This class returns and accepts types as Classes, even primitive types; there are Class
 ** types defined that represent each different primitive type.  They are <code>java.lang.Boolean.TYPE,
 ** java.lang.Byte.TYPE, </code>etc.  These are not to be confused with the classes
 ** <code>java.lang.Boolean, java.lang.Byte</code>, etc., which are real classes.<P>
 **
 ** <STRONG>Implementation note:</STRONG> I am using non-native methods for almost everything here.  It's a tradeoff.
 ** If all the functions are going to be called at least once, then it's better to have the constructor pass in all the
 ** information about the class and store it in the class because JNI overhead will be saved when the information
 ** is accessed.  If many of them will not be called, then the overhead of grabbing that information once was not
 ** worth it.  Especially true of getExceptionTypes() and getArgumentTypes().  I am considering making those two
 ** lazily-accessed--grabbed natively the first time they are accessed and then cached thereafter.  getExceptionTypes()
 ** is not extremely likely to be called at all.<P>
 **
 ** <STRONG>Serialization:</STROMG>Note that this is not a serializable class.  It is entirely feasible to make it
 ** serializable, but this is on Sun, not me.<P>
 **
 ** <STRONG>Access and Security:</STRONG> Once this Constructor is created by java.lang.Class (which does its own
 ** security check), any object may query it for information like parameter types, exception types, etc.
 ** However, the Constructor may only be invoked using standard Java language access controls.  The JLS says that
 ** reflective access to all private, public and protected reflective members is granted to any class which
 ** can be linked against the reflected member.  Link-level enforcement is the enforcement of public, private,
 ** protected and default access rules, based on the caller's relationship to the class (same package,
 ** subclass, or unrelated).  Thus, if you couldn't normally invoke this constructor from the calling class, you
 ** can't do it using a Constructor object either.<P>
 **
 ** The relevant section of the VM spec on link-security is <A
 ** HREF='http://java.sun.com/docs/books/vmspec/html/Concepts.doc.html#22574'>2.16.3</A>, under Resolution.
 ** A summary of the appropriate rules follows.<P>
 **
 ** <STRONG>Summary of access rules</STRONG><BR>
 ** Two checks are done, and they are the same checks--on both the member's class and the member itself:
 ** <UL>
 ** <LI>If the caller is the same class as the member's class, then it can access the member no matter
 ** what.</LI>
 ** <LI>If the caller is in the same package as the member's class, then the member's class and the member
 ** itself must both be public, protected or default access.</LI>
 ** <LI>If the caller is a subclass of the member's class, then the member's class and the member itself must
 ** both be public or protected access.</LI>
 ** <LI>If the caller is in the same package as the member's class, then the member's class and the member must
 ** be public access.</LI>
 ** </UL>
 ** <P>
 **
 ** As far as I can tell from the fairly confusing <A
 ** HREF='http://java.sun.com/products/jdk/1.1/docs/guide/innerclasses/spec/innerclasses.doc.html'>Inner
 ** Classes Specification</A>, there should be no change to these rules from the addition of inner
 ** classes in 1.1.<P>
 **
 ** <STRONG>Version note:</STRONG> In 1.2, the security checks can be disabled in the AccessibleObject
 ** interface.  But this ain't 1.2. :)<P>
 **
 ** <STRONG>BUGS:</STRONG> The maximum size of a signature right now is 4096 characters because I'm using
 ** a static buffer when I calculate them.  While fine for most purposes, it is for pathological cases that
 ** specs are built.  I'm not sure how I'll handle this yet.
 **
 ** @author John Keiser
 ** @version 1.1.0, 31 May 1998
 ** @see Member
 ** @see java.lang.Class#getConstructor(Object[])
 ** @see java.lang.Class#getDeclaredConstructor(Object[])
 ** @see java.lang.Class#getConstructors()
 ** @see java.lang.Class#getDeclaredConstructors()
 **/

public final class Constructor implements Member {
	private Class declaringClass;
	private int modifiers;
	private Class[] parameterTypes;
	private Class[] exceptionTypes;

	/* Native State Stuff */
	private final int native_state = System.identityHashCode(this);
	static {
		initNativeState();
	}

	/* This is for JCL to call only. */
	private Constructor(Class declaringClass, int modifiers,
		Class[] parameterTypes, Class[] exceptionTypes) {
		this.declaringClass = declaringClass;
		this.modifiers = modifiers;
		this.parameterTypes = parameterTypes;
		this.exceptionTypes = exceptionTypes;
	}

	/** Gets the class that declared this constructor.
	 ** <B>It is unclear whether this returns the class that actually syntactically declared
	 ** the member, or the class where the Constructor object was gotten from.</B>
	 ** @return the class that declared this member.
	 **/
	public Class getDeclaringClass() {
		return declaringClass;
	}

	/** Gets the modifiers this constructor uses.  Use the <code>Modifier</code>
	 ** class to interpret the values.
	 ** A Constructor may only have the modifiers public, private and protected.
	 ** @see Modifier
	 ** @return an integer representing the modifiers to this Member.
	 **/
	public int getModifiers() {
		return modifiers;
	}

	/** Gets the name of this constructor (the non-qualified name of the class it was declared in).
	 ** @return the name of this constructor.
	 **/
	public String getName() {
		return getDeclaringClass().getName();
	}

	/** Get the parameter list for this constructor.
	 ** @return a list of classes representing the names of the constructor's parameters.
	 **/
	public Class[] getParameterTypes() {
		return parameterTypes;
	}

	/** Get the exception types this constructor says it throws.
	 ** @return a list of classes representing the exception types.
	 **/
	public Class[] getExceptionTypes() {
		return parameterTypes;
	}

	/** Compare two objects to see if they are semantically equivalent.
	 ** Two Constructors are semantically equivalent if they have the same declaring class and the
	 ** same parameter list.  <B>Though I really don't see how two different Constructor objects with
	 ** identical parameters could be created.</B>
	 ** @param o the object to compare to.
	 ** @return <code>true<code> if they are equal; <code>false</code> if not.
	 **/
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o instanceof Constructor) {
			Constructor c = (Constructor)o;
			if(!getDeclaringClass().equals(c.getDeclaringClass())) {
				return false;
			}
			Class[] p1 = getParameterTypes();
			Class[] p2 = c.getParameterTypes();
			if(p1.length != p2.length) {
				return false;
			}
			for(int i=0;i<p1.length;i++) {
				if(!p1.equals(p2)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/** Get the hash code for the Constructor.
	 ** Constructor hash code is the hash code of the declaring class's name.
	 ** @return the hash code for the object.
	 **/
	public int hashCode() {
		return getDeclaringClass().getName().hashCode();
	}

	/** Get a String representation of the Constructor.
	 ** A Constructor's String representation is &lt;modifiers&gt; &lt;classname&gt;(&lt;paramtypes&gt;).
	 ** Example: <code>public java.lang.Thread(java.lang.Runnable,int)</code>
	 ** @return the String representation of the Constructor.
	 **/
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Modifier.toString(getModifiers()));
		sb.append(' ');
		sb.append(getDeclaringClass().getName());
		sb.append('(');
		Class[] c = getParameterTypes();
		if(c.length > 0) {
			sb.append(c[0].getName());
			for(int i = 1; i < c.length; i++) {
				sb.append(',');
				sb.append(c[i].getName());
			}
		}
		return sb.toString();
	}
 
	/** Create a new instance of the object the constructor can construct.
	 ** The constructor will permit widening argument conversions, but not narrowing conversions.
	 ** @param args the arguments to the constructor.
	 ** @return the newly created object.
	 ** @exception InstantiationException		if the class is abstract.  <B>Never mind that there
	 **						should be no constructors in an abstract class.</B>
	 ** @exception IllegalAccessException		if the constructor could not normally be called
	 **						by the Java code (i.e. it is not public).
	 ** @exception IllegalArgumentException		if the number of arguments is incorrect, or if the
	 **						arguments cannot be converted to the actual argument
	 **						types, even with a widening conversion.
	 ** @exception InvocationTargetException	if the constructor throws an exception.
	 **/
	public Object newInstance(Object args[])
		throws InstantiationException,
		       IllegalAccessException,
		       IllegalArgumentException,
		       InvocationTargetException {
		return constructNative(args, declaringClass, modifiers, parameterTypes, args.length == parameterTypes.length ? args.length : -1);
	}

	/*
	 * STATIC NATIVE HELPERS
	 */

	private static native void initNativeState();

	/*
	 * NATIVE HELPERS
	 */
	protected void finalize() {
		finalizeNative();
	}

	private native void finalizeNative();

	private native Object constructNative(Object[] args, Class declaringClass,
					int modifiers, Class[] parameterTypes, int argLength);
}
