/*
 * java.beans.beancontext.BeanContextContainerProxy: part of the Java Class Libraries project.
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

import java.awt.Container;

/**
 * Interface for <code>BeanContext</code>s which wish to associate an
 * AWT container with them.  The proxy is provided because the
 * <code>addPropertyChangeListener()</code> and <code>add()</code> methods
 * would conflict with <code>Component</code> and <code>Container</code>
 * if you tried to extend.
 *
 * @author John Keiser
 * @since JDK1.2
 */

public interface BeanContextContainerProxy {
	/**
	 * Get the <code>Container</code> associated with this
	 * <code>BeanContext</code>.
	 */
	public Container getContainer();
}
