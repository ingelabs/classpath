/*************************************************************************
/* IllegalAccessException.java -- exception thrown when trying to load a 
/* class that is not public and in another package.
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
 * Thrown in two cases.  The first is when try to load a class that is
 * not public and in another package using specific methods from 
 * <code>ClassLoader</code> and <code>Class</code>.  The second case is
 * when trying to create a new instance of a class to which you do not have
 * access to the zero argument constructor as in using the 
 * <code>newsInstance</code> method of class <code>Class</code>.
 *
 * @author Brian Jones
 */
public class IllegalAccessException extends Exception
{
  /**
   * Create an exception without a message.
   */
  public IllegalAccessException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IllegalAccessException(String s)
    {
      super(s);
    }
}
