/* Clipboard.java -- Class for transferring data via cut and paste.
   Copyright (C) 1999, 2001 Free Software Foundation, Inc.

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


package java.awt.datatransfer;

/**
  * This class allows data to be transferred using a cut and paste type
  * mechanism.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Clipboard
{

/*
 * Instance Variables
 */

/**
  * The data being transferred.
  */
protected Transferable contents;

/**
  * The owner of this clipboard.
  */
protected ClipboardOwner owner;

// The clipboard name
private String name;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * Initializes a new instance of <code>Clipboard</code> with the
  * specified name.
  *
  * @param name The clipboard name.
  */
public 
Clipboard(String name)
{
  this.name = name;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the name of the clipboard.
  */
public String
getName()
{
  return(name);
}

/*************************************************************************/

/**
  * Returns the contents of the clipboard.
  *
  * @param requestor The object requesting the contents.
  */
public synchronized Transferable
getContents(Object requestor)
{
  return(contents);
}

/*************************************************************************/

/**
  * Sets the content and owner of this clipboard.
  * If the given owner is different from the current owner
  * then lostOwnership is called on the current owner.
  * XXX - is this called with the old or new contents.
  *
  * @param contents The new clipboard contents.
  * @param owner The new clipboard owner
  */
public synchronized void
setContents(Transferable contents, ClipboardOwner owner)
{
  if (this.owner != owner)
    if (this.owner != null)
      this.owner.lostOwnership(this, contents);
 
  this.owner = owner;
  this.contents = contents;
}

} // class Clipboard

