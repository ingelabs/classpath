/*************************************************************************
/* OutOfMemoryError.java 
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
 * When the Java Virtual Machine is unable to allocate an object because it 
 * is out of memory and no more memory could be made available by the 
 * garbage collector an <code>OutOfMemoryError</code> is thrown.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class OutOfMemoryError extends VirtualMachineError
{
  static final long serialVersionUID = 8228564086184010517L;

  /**
   * Create an error without a message.
   */
  public OutOfMemoryError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public OutOfMemoryError(String s)
    {
      super(s);
    }
}
