/*
 * java.lang.reflect.Field: part of the Java Class Libraries project.
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
 ** The Field class represents a member variable of a class.
 ** It allows you to read and manipulate that variable.<P>
 **
 ** <B>Note:</B> This class returns and accepts types as Classes, even primitive types; there are Class
 ** types defined that represent each different primitive type.  They are <code>java.lang.Boolean.TYPE,
 ** java.lang.Byte.TYPE, </code>etc.  These are not to be confused with the classes
 ** <code>java.lang.Boolean, java.lang.Byte</code>, etc., which are real classes.<P>
 **
 ** Also note that this is not a serializable class.  It is entirely feasible to make it serializable
 ** using the Externalizable interface, but this is on Sun, not me.
 **
 ** @author John Keiser
 ** @version 1.1.0, 31 May 1998
 ** @see Member
 ** @see java.lang.Class#getField(String)
 ** @see java.lang.Class#getDeclaredField(String)
 ** @see java.lang.Class#getFields()
 ** @see java.lang.Class#getDeclaredFields()
 **/
public final class Field implements Member {
	private Class declaringClass;
	private String name;
	private Class type;
	private int modifiers;

	/* Native State Stuff */
	private final int native_state = System.identityHashCode(this);
	static {
		initNativeState();
	}

	/* This class is uninstantiable except natively. */
        private Field() {}

	/** Gets the class that declared this field.
	 ** <B>It is unclear whether this returns the class that actually syntactically declared
	 ** the member, or the class where the Field object was gotten from.</B>
	 ** @return the class that declared this member.
	 **/
	public Class getDeclaringClass() {
		return declaringClass;
	}

	/** Gets the modifiers this field uses.  Use the <code>Modifier</code>
	 ** class to interpret the values.  A field can only have the following
	 ** modifiers: public, private, protected, static, final, transient, and volatile. 
	 ** @see Modifier
	 ** @return an integer representing the modifiers to this Member.
	 **/
	public int getModifiers() {
		return modifiers;
	}

	/** Gets the name of this field.
	 ** @return the name of this field.
	 **/
	public String getName() {
		return name;
	}

	/** Gets the type of this field.
	 ** @return the type of this field.
	 **/
	public Class getType() {
		return type;
	}

	/** Compare two objects to see if they are semantically equivalent.
	 ** Two Fields are semantically equivalent if they have the same declaring class and the
	 ** same name.
	 ** @param o the object to compare to.
	 ** @return <code>true<code> if they are equal; <code>false</code> if not.
	 **/
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		} else {
			return (o instanceof Field)
				&& getDeclaringClass().equals(((Field)o).getDeclaringClass())
				&& getName().equals(((Field)o).getName());
		}
	}

	/** Get the hash code for the Field.
	 ** The Field hash code is the hash code of its name XOR'd with the hash code of its class name.
	 ** @return the hash code for the object.
	 **/
	public int hashCode() {
		return getDeclaringClass().getName().hashCode() ^ getName().hashCode();
	}

	/** Get a String representation of the Field.
	 ** A Field's String representation is &lt;modifiers&gt; &lt;type&gt; &lt;class&gt;.&lt;fieldname&gt;.
	 ** Example: <code>public transient boolean gnu.parse.Parser.parseComplete</code>
	 ** @return the String representation of the Constructor.
	 **/
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Modifier.toString(getModifiers()));
		sb.append(' ');
		sb.append(getType().getName());
		sb.append(' ');
		sb.append(getDeclaringClass().getName());
		sb.append('.');
		sb.append(getName());
		return sb.toString();
	}
 
	/** Get the value of this Field.  If it is primitive, it will be wrapped in the
	 ** appropriate wrapper type (boolean = java.lang.Boolean)
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this field is not a field of <code>o</code>.
	 **/
	public Object get(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getNative(o,name,declaringClass,type,modifiers);
	}

	/** Get the value of this boolean Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a boolean field of <code>o</code>.
	 **/
	public boolean getBoolean(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getBooleanNative(o,name,declaringClass,type,modifiers);
	}

	/** Get the value of this byte Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a byte field of <code>o</code>.
	 **/
	public byte getByte(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getByteNative(o,name,declaringClass,type,modifiers);
	}

	/** Get the value of this Field as a short.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a field of <code>o</code> or is
	 **					a field of <code>o</code> but cannot be converted
	 **					via a widening conversion to a short.
	 **/
	public short getShort(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getShortNative(o,name,declaringClass,type,modifiers);
	}

	/** Get the value of this Field as a char.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a field of <code>o</code> or is
	 **					a field of <code>o</code> but cannot be converted
	 **					via a widening conversion to a char.
	 **/
	public char getChar(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getCharNative(o,name,declaringClass,type,modifiers);
	}

	/** Get the value of this Field as an int.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a field of <code>o</code> or is
	 **					a field of <code>o</code> but cannot be converted
	 **					via a widening conversion to an int.
	 **/
	public int getInt(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getIntNative(o,name,declaringClass,type,modifiers);
	}

	/** Get the value of this Field as a long.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a field of <code>o</code> or is
	 **					a field of <code>o</code> but cannot be converted
	 **					via a widening conversion to a long.
	 **/
	public long getLong(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getShortNative(o,name,declaringClass,type,modifiers);
	}

	/** Get the value of this Field as a float.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a field of <code>o</code> or is
	 **					a field of <code>o</code> but cannot be converted
	 **					via a widening conversion to a float.
	 **/
	public float getFloat(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getShortNative(o,name,declaringClass,type,modifiers);

	}

	/** Get the value of this Field as a double.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to get the value of this Field from.
	 ** @return the value of the Field.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this is not a field of <code>o</code> or is
	 **					a field of <code>o</code> but cannot be converted
	 **					via a widening conversion to a double.
	 **/
	public double getDouble(Object o)
		throws IllegalAccessException,
		       IllegalArgumentException {
		return getShortNative(o,name,declaringClass,type,modifiers);
	}

	/** Set this Field.  If it is a primitive field, the value passed must be wrapped in
	 ** the appropriate wrapped type (boolean = java.lang.Boolean)
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if <code>value</code> cannot be converted by a
	 **					widening conversion to the underlying type of
	 **					the Field.
	 **/
	public void set(Object o, Object value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this boolean Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this field is not a primitive boolean field.
	 **/
	public void setBoolean(Object o, boolean value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setBooleanNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this byte Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if a byte cannot be converted via a widening
	 **					conversion to the type of this field.
	 **/
	public void setByte(Object o, byte value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setByteNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this short Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if a byte cannot be converted via a widening
	 **					conversion to the type of this field.
	 **/
	public void setShort(Object o, short value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setShortNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this char Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if a char cannot be converted via a widening
	 **					conversion to the type of this field.
	 **/
	public void setChar(Object o, char value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setCharNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this int Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if an int cannot be converted via a widening
	 **					conversion to the type of this field.
	 **/
	public void setInt(Object o, int value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setIntNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this long Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if a long cannot be converted via a widening
	 **					conversion to the type of this field.
	 **/
	public void setLong(Object o, long value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setLongNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this float Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to.
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if a float cannot be converted via a widening
	 **					conversion to the type of this field.
	 **/
	public void setFloat(Object o, float value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setFloatNative(o,value,name,declaringClass,type,modifiers);
	}

	/** Set this double Field.
	 ** If the field is static, <code>o</code> will be ignored.
	 ** @param o the object to set this Field on.
	 ** @param value the value to set this Field to
	 ** @exception IllegalAccessException	if you could not normally access this field
	 **					(i.e. it is not public).
	 ** @exception IllegalArgumentException	if this field is not a primitive double field.
	 **/
	public void setDouble(Object o, double value)
		throws IllegalAccessException,
		        IllegalArgumentException {
		setDoubleNative(o,value,name,declaringClass,type,modifiers);
	}

	protected void finalize() {
		finalizeNative();
	}

	/*
	 * STATIC NATIVE HELPERS
	 */

	static native void initNativeState();

	/*
	 * NATIVE HELPERS
	 */
	private native void finalizeNative();

	private native Object getNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native boolean getBooleanNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native byte getByteNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native char getCharNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native short getShortNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native int getIntNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native long getLongNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native float getFloatNative(Object o,String name,Class declaringClass,Class type, int modifiers);
	private native double getDoubleNative(Object o,String name,Class declaringClass,Class type, int modifiers);

	private native void setNative(Object o,Object val,String name,Class declaringClass,Class type, int modifiers);
	private native void setBooleanNative(Object o,boolean val,String name,Class declaringClass,Class type, int modifiers);
	private native void setByteNative(Object o,byte val,String name,Class declaringClass,Class type, int modifiers);
	private native void setCharNative(Object o,char val,String name,Class declaringClass,Class type, int modifiers);
	private native void setShortNative(Object o,short val,String name,Class declaringClass,Class type, int modifiers);
	private native void setIntNative(Object o,int val,String name,Class declaringClass,Class type, int modifiers);
	private native void setLongNative(Object o,long val,String name,Class declaringClass,Class type, int modifiers);
	private native void setFloatNative(Object o,float val,String name,Class declaringClass,Class type, int modifiers);
	private native void setDoubleNative(Object o,double val,String name,Class declaringClass,Class type, int modifiers);
}
