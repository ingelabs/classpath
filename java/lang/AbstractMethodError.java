/*************************************************************************
/* AbstractMethodError.java 
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
 * An <code>AbstractMethodError</code> is thrown when an application
 * attempts to access an abstract method.  Compilers typically detect
 * this error, but it can be thrown at run time if the definition of a 
 * class has changed since the application was last compiled.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class AbstractMethodError extends IncompatibleClassChangeError
{
  static final long serialVersionUID = -1654391082989018462L;

  /**
   * Create an error without a message.
   */
  public AbstractMethodError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public AbstractMethodError(String s)
    {
      super(s);
    }
}
