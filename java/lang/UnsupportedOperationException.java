/*************************************************************************
/* UnsupportedOperationException.java -- Exception thrown when an
/* unsupported operation is attempted on an object
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.lang;

/** 
 * This exception is thrown by an object when an operation is
 * requested of it that it does not support.
 *
 * @since JDK 1.2
 */
public class UnsupportedOperationException extends RuntimeException
{
  static final long serialVersionUID = 0L;  /* FIXME */

  /**
   * Create an exception without a message.
   */
  public UnsupportedOperationException() 
  {
    super();
  }

  /**
   * Create an exception with a message.
   */
  public UnsupportedOperationException( String s )
  {
    super(s);
  }
}
