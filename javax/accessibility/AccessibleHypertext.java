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

As a special exception, if you link this library with other files to
produce an executable, this library does not by itself cause the
resulting executable to be covered by the GNU General Public License.
This exception does not however invalidate any other reasons why the
executable file might be covered by the GNU General Public License. */

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
