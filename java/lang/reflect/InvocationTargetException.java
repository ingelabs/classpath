/*
 * java.lang.reflect.InvocationTargetException: part of the Java Class Libraries project.
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
 ** InvocationTargetException is sort of a way to "wrap" whatever exception comes up when a method or
 ** constructor is called via Reflection.
 **
 ** @author John Keiser
 ** @version 1.1.0, 31 May 1998
 ** @see Method#invoke(Object,Object[])
 ** @see Constructor#newInstance(Object[])
 **/

public class InvocationTargetException extends Exception {
	private Throwable targetException;

	/** <B>I am not sure if I'm supposed to copy the protected functions from the spec or not ... ?</B> **/
	protected InvocationTargetException() {
		super();
		this.targetException = null;
	}

	/** Create an <code>InvocationTargetException</code> using another exception.
	 ** @param targetException the exception to wrap
	 **/
	public InvocationTargetException(Throwable targetException) {
		super("Invoked method threw " + targetException.getClass().getName());
		this.targetException = targetException;
	}

	/** Create an <code>InvocationTargetException</code> using another exception and an error message.
	 ** @param targetException the exception to wrap
	 ** @param err an extra reason for the exception-throwing
	 **/
	public InvocationTargetException(Throwable targetException, String err) {
		super(err);
		this.targetException = targetException;
	}

	/** Get the wrapped (targeted) exception.
	 ** @return the targeted exception.
	 **/
	public Throwable getTarget() {
		return targetException;
	}
}
