/*
 * java.beans.beancontext.BeanContextProxy: part of the Java Class Libraries project.
 * Copyright (C) 1999 Free Software Foundation
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

package java.beans.beancontext;

/**
 * Beans that wish to have a <code>BeanContextChild</code> or <code>BeanContext</code> associated with them
 * but do not wish to implement those interfaces directly, can implement this interface.
 * <P>
 *
 * Don't shoot yourself in the foot: if you already implement
 * <code>BeanContextChild</code>, directly or indirectly, the whole
 * workings of this package will be unpredictable because it is
 * indeterminate as to whether the <code>BeanContextChild</code> is used
 * in preference to its proxy or vice versa.
 *
 * @author John Keiser
 * @since JDK1.2
 */

public interface BeanContextProxy {
	/**
	 * Return the <code>BeanContextChild</code> associated with this
	 * <code>Object</code>.
	 *
	 * @return the <code>BeanContextChild</code> associated with this
	 * <code>Object</code>.
	 */
	public BeanContextChild getBeanContextProxy();
}
