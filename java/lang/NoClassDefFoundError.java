/*************************************************************************
/* NoClassDefFoundError.java 
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
 * A <code>NoClassDefFoundError</code> is thrown when a classloader or the
 * Java Virtual Machine tries to load a class and no definition of the class
 * can be found.  This could happen when using the <code>new</code> expression
 * or during a normal method call.  The reason this would occur at runtime is 
 * because the missing class definition existed when the currently executing 
 * class was compiled, but now that definition cannot be found.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class NoClassDefFoundError extends LinkageError
{
  static final long serialVersionUID = 9095859863287012458L;

  /**
   * Create an error without a message.
   */
  public NoClassDefFoundError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public NoClassDefFoundError(String s)
    {
      super(s);
    }
}
