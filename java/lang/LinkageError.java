/*************************************************************************
/* LinkageError.java 
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
 * Subclasses of <code>LinkageError</code> are thrown to indicate 
 * a class which is depended upon by another class has incompatibly 
 * changed after the compilation of the latter class.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class LinkageError extends Error
{
  static final long serialVersionUID = 3579600108157160122L;

  /**
   * Create an error without a message.
   */
  public LinkageError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public LinkageError(String s)
    {
      super(s);
    }
}
