/*************************************************************************
/* RuntimeException.java -- all exceptions which are subclasses of this class
/* can be thrown at any time during the execution of a Java virtual machine.
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
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * All exceptions which are subclasses of <code>RuntimeException</code>
 * can be thrown at any time during the execution of a Java virtual machine.
 * Methods which throw these exceptions are not required to declare them
 * in their throws clause.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class RuntimeException extends Exception
{
  static final long serialVersionUID = -7034897190745766939L;

  /**
   * Create an exception without a message.
   */
  public RuntimeException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public RuntimeException(String s)
    {
      super(s);
    }
}
