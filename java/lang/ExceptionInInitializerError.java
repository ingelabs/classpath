/*************************************************************************
/* ExceptionInInitializerError.java 
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

import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

/**
 * An <code>ExceptionInInitializerError</code> is thrown when an 
 * unexpected exception has occurred in a static initializer or the
 * initializer for a static variable.
 *
 * @since JDK 1.1
 * 
 * @author Brian Jones
 */
public class ExceptionInInitializerError extends LinkageError
{
  static final long serialVersionUID = 1521711792217232256L;
  private Throwable t = null;

  /**
   * Create an error without a message.
   */
  public ExceptionInInitializerError()
    {
      super();
    }

  /**
   * Create an error with a message.
   */
  public ExceptionInInitializerError(String s)
    {
      super(s);
    }

  /**
   * Creates an error an saves a reference to the <code>Throwable</code>
   * object.
   *
   * @param t the exception thrown
   */
  public ExceptionInInitializerError(Throwable t)
    {
      super();
      this.t = t;
    }

  /**
   * Serialize the object in a manner binary compatible with the JDK 1.2
   */
  private void writeObject(java.io.ObjectOutputStream s) 
    throws IOException
    {
      ObjectOutputStream.PutField oFields;
      oFields = s.putFields();
      oFields.put("exception", this.t);
      s.writeFields();
    }

  /**
   * Deserialize the object in a manner binary compatible with the JDK 1.2
   */    
  private void readObject(java.io.ObjectInputStream s)
    throws IOException, ClassNotFoundException
    {
      ObjectInputStream.GetField oFields;
      oFields = s.readFields();
      this.t = oFields.get("exception", (Throwable)null);
    }
}
