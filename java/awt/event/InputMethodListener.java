/* InputMethodListener.java -- Listen for input method events
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


package java.awt.event;

/**
  * This interface is for classes that wish to receive events from an
  * input method.
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface InputMethodListener extends java.util.EventListener
{

/**
  * This method is called when the cursor position within the text is changed.
  *
  * @param event The <code>InputMethodEvent</code> indicating the 
  * position change.
  */
public abstract void
caretPositionChanged(InputMethodEvent event);

/*************************************************************************/

/**
  * This method is called when the text is changed.
  *
  * @param event The <code>InputMethodEvent</code> indicating the text change.
  */
public abstract void
inputMethodTextChanged(InputMethodEvent event);

} // interface InputMethodListener

