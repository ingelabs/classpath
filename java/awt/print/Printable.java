/* Printable.java -- Renders a page to the print device
   Copyright (C) 1999 Free Software Foundation, Inc.

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


package java.awt.print;

import java.awt.Graphics;

/**
  * This interface provides a mechanism for the actual printing of pages to the
  * printer.  The object implementing this interface performs the page
  * rendering.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Printable
{

/*
 * Static Variables
 */

/**
  * This value is returned by the <code>print()</code> method to indicate
  * that the requested page number does not exist.
  */
public static final int NO_SUCH_PAGE = 0;

/**
  * This value is returned by the <code>print()</code> method to indicate
  * that the requested page exists and has been printed.
  */
public static final int PAGE_EXISTS = 1;

/*************************************************************************/

/**
  * This method prints the specified page to the specified graphics
  * context in the specified format.  The pages are numbered starting
  * from zero.
  *
  * @param graphics The graphics context to render the pages on.
  * @param format The format in which to print the page.
  * @param page_number The page number to print, where numbers start at zero.
  *
  * @return <code>PAGE_EXISTS</code> if the requested page exists and was
  * successfully printed, <code>NO_SUCH_PAGE</code> otherwise.
  *
  * @exception PrinterException If an error occurs during printing.
  */
public abstract int
print(Graphics graphics, PageFormat format, int page_number) 
      throws PrinterException;

} // interface Printable

