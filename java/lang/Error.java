/*************************************************************************
/* Error.java 
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
 * Applications should not try to catch errors since they indicate 
 * abnormal conditions.  An abnormal condition is something which should not
 * occur.  A few errors, like <code>ThreadDeath</code> error do normally
 * occur, but most applications should not catch it.  
 * <p>
 * A method is not required to declare any subclass of <code>Error</code> in 
 * its <code>throws</code> clause which might be thrown but not caught while
 * executing the method..
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class Error extends Throwable
{
  static final long serialVersionUID = 4980196508277280342L;

  /**
   * Create an error without a message.
   */
  public Error()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public Error(String s)
    {
      super(s);
    }
}
