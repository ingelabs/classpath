/* AccessibleHypertext.java -- Java interface for aiding in accessibly rendering 
   other Java components
   Copyright (C) 2000 Free Software Foundation, Inc.

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

package javax.accessibility;

/**
 * Allows an application to determine a set of hyperlinks for a 
 * document and manipulate them.
 * <p>
 * The <code>AccessibleContext.getAccessibleText()</code> method
 * should return an object which extends <code>AccessibleHypertext</code> 
 * if it supports this interface.
 *.
 * @see AccessibleContext.getAccessibleText()
 */
public interface AccessibleHypertext extends AccessibleText {

    /**
     * Returns link object denoted by the number <code>i</code> in this
     * document.
     *
     * @param i the ith hyperlink of the document
     * @return link object denoted by <code>i</code>
     */
    public abstract AccessibleHyperlink getLink(int i);
    
    /**
     * Returns the number of links in the document if any exist.  When
     * no links exist, returns -1.
     * 
     * @return the number of links
     */
    public abstract int getLinkCount();
    
    /**
     * Returns the link index for this character index into the
     * document if a hyperlink is associated with the character
     * specified by the given index.  When no association exists,
     * returns -1.
     * 
     * @param c the character index
     * @return the link index
     */
    public abstract int getLinkIndex(int c);
}
