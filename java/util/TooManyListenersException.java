/*
 * java.util.TooManyListenersException: part of the Java Class Libraries project.
 * Copyright (C) 1999 Jochen Hoenicke
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

package java.util;

/**
 * This exception is part of the java event model.  It is thrown if an
 * event listener is added via the addXyzEventListener method, but the
 * object doesn' support any more listeners, e.g. it only supports a
 * single event listener.
 *
 * @see EventListener
 * @see EventObject
 * @author Jochen Hoenicke 
 */

public class TooManyListenersException extends Exception
{
  private static final long serialVersionUID = 5074640544770687831L;

  /**
   * Constructs a TooManyListenersException with no detail message.
   */
  public TooManyListenersException() 
  {
  }

  /**
   * Constructs a TooManyListenersException with a detail message.
   * @param detail the detail message.
   */
  public TooManyListenersException(String detail)
  {
    super(detail);
  }
}
