/*************************************************************************
/* StackOverflowError.java 
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
 * A <code>StackOverflowError</code> is thrown when a stack overflow occurs.
 * This can occur because an application recurses too deeply.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class StackOverflowError extends VirtualMachineError
{
  static final long serialVersionUID = 8609175038441759607L;

  /**
   * Create an error without a message.
   */
  public StackOverflowError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public StackOverflowError(String s)
    {
      super(s);
    }
}
