/*************************************************************************
/* Fail.java -- Result code returned when test failed but was expected to 
/* pass.
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
 * Test failed but was expected to pass.
 */
public class Fail extends Result
{
  /**
   * Constructs a Fail result code with additional information.
   */
  public Fail(String msg) {
    super("FAIL", msg);
  }
  /**
   * Constructs a Fail result code.
   */
  public Fail() {
    this("");
  }
}
