/*************************************************************************
/* XPass.java -- Result code returned when test failed and was expected to
/* fail.
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
 * Test failed and was expected to fail.
 */
public class XFail extends Result
{
  /**
   * Constructs an XFail result code with additional information.
   */
  public XFail(String msg) {
    super("XFAIL", msg);
  }
  /**
   * Constructs an XFail result code.
   */
  public XFail() {
    this("");
  }
}
