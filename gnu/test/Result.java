/*************************************************************************
/* Result.java -- Abstract base class for all Result types.
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Paul Fisher (rao@gnu.org)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package gnu.test;

/**
 * Class which all usable Result objects extend.
 */
public abstract class Result
{
  String name, msg;

  /**
   * Create a result of a given type, with a given message.
   *
   * @param name name of type
   * @param msg message
   */
  public Result(String name, String msg) {
    this.name = name;
    this.msg = msg;
  }

  /**
   * Create a result of a given type.
   *
   * @param name name of type
   */
  public Result(String name) {
    this(name, "");
  }

  /**
   * Returns the name of the type.
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the message associated with this instance of Result, or
   * the empty string if no message exists.
   */
  public String getMsg() {
    return (msg != null) ? msg : "";
  }

  public String toString() {
    return getName() + ":" + getMsg();
  }
}
