/*
 * java.lang.reflect.Member: part of the Java Class Libraries project.
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

package java.lang.reflect;

/**
 ** Member is an interface that represents any member of a class; i.e. a field, a method or a constructor.
 ** You can get information about the declaring class, name or modifiers of the member with this interface.
 ** @author  John Keiser
 ** @version 1.1.0, 31 May 1998
 **/
public interface Member {
	/** Represents all members, whether public, private, protected or package-protected.
	 ** Used in java.lang.SecurityManager.checkMemberAccess() to determine the type of members
	 ** to access.
	 **/
	public static final int DECLARED = 0;

	/** Represents public members only.  Used in java.lang.SecurityManager.checkMemberAccess()
	 ** to determine the type of members to access.
	 **/
	public static final int PUBLIC = 1;

	/** Gets the class that declared this member.
	 ** <STRONG>It is unclear whether this returns the class that actually syntactically declared
	 ** the member, or the class where the <code>Member</code> object was gotten from.</STRONG>
	 ** @return the class that declared this member.
	 **/
	public abstract Class getDeclaringClass();

	/** Gets the modifiers this member uses.  Use the <code>Modifier</code>
	 ** class to interpret the values.
	 ** @see Modifier
	 ** @return an integer representing the modifiers to this Member.
	 **/
	public abstract int getModifiers();

	/** Gets the name of this member.
	 ** @return the name of this member.
	 **/
	public abstract String getName();
}