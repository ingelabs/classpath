/*************************************************************************
/* InternalError.java 
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
 * An <code>InternalError</code> is thrown when a mystical error has
 * occurred in the Java Virtual Machine.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class InternalError extends VirtualMachineError
{
  static final long serialVersionUID = -9062593416125562365L;

  /**
   * Create an error without a message.
   */
  public InternalError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public InternalError(String s)
    {
      super(s);
    }
}
