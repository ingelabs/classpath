/* Book.java -- A mixed group of pages to print.
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

import java.util.Vector;

/**
  * This class allows documents to be created with different paper types,
  * page formatters, and painters.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class Book implements Pageable
{

/*
 * Instance Variables
 */

// Painter objects for the book
Vector printables = new Vector();

// Page formats for the book
Vector page_formats = new Vector();

/*************************************************************************/

/*
 * Constructors
 */

/** 
  * Initializes a new instance of <code>Book</code> that is empty.
  */
public
Book()
{
  ;
}

/*************************************************************************/

/**
  * Returns the number of pages in this book.
  *
  * @return The number of pages in this book.
  */
public int
getNumberOfPages()
{
  return(printables.size());
}

/*************************************************************************/

/**
  * This method returns the <code>PageFormat</code> object for the
  * specified page.
  *
  * @param page_numbers The number of the page to get information for, where
  * page numbers start at 0.
  *
  * @return The <code>PageFormat</code> object for the specified page.
  *
  * @exception IndexOutOfBoundsException If the page number is not valid.
  */
public PageFormat
getPageFormat(int page_number)
{
  return((PageFormat)page_formats.elementAt(page_number));
}

/*************************************************************************/

/**
  * This method returns the <code>Printable</code> object for the
  * specified page.
  *
  * @param page_numbers The number of the page to get information for, where
  * page numbers start at 0.
  *
  * @return The <code>Printable</code> object for the specified page.
  *
  * @exception IndexOutOfBoundsException If the page number is not valid.
  */
public Printable
getPrintable(int page_number)
{
  return((Printable)printables.elementAt(page_number));
}

/*************************************************************************/

/**
  * This method appends a page to the end of the book.
  *
  * @param printable The <code>Printable</code> for this page.
  * @param page_format The <code>PageFormat</code> for this page.
  *
  * @exception NullPointerException If either argument is <code>null</code>.
  */
public void
append(Printable printable, PageFormat page_format)
{
  append(printable, page_format, 1);
} 

/*************************************************************************/

/**
  * This method appends the specified number of pages to the end of the book.
  * Each one will be associated with the specified <code>Printable</code>
  * and <code>PageFormat</code>.
  *
  * @param printable The <code>Printable</code> for this page.
  * @param page_format The <code>PageFormat</code> for this page.
  * @param num_pages The number of pages to append.
  *
  * @exception NullPointerException If any argument is <code>null</code>.
  */
public void
append(Printable painter, PageFormat page_format, int num_pages)
{
  for (int i = 0; i < num_pages; i++)
    {
      printables.addElement(painter);
      page_formats.addElement(page_format);
    }
}

/*************************************************************************/

/**
  * This method changes the <code>Printable</code> and <code>PageFormat</code>
  * for the specified page.  The page must already exist or an exception
  * will be thrown.
  *
  * @param page_num The page number to alter.
  * @param printable The new <code>Printable</code> for the page.
  * @param page_format The new <code>PageFormat</code> for the page.
  *
  * @param IndexOutOfBoundsException If the specified page does not exist.
  */
public void
setPage(int page_num, Printable printable, PageFormat page_format)
{
  printables.setElementAt(printable, page_num);
  page_formats.setElementAt(page_format, page_num);
}

} // class Book

