/*************************************************************************
/* Unresolved.java -- Result code returned when test produced 
/* indeterminate results.
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
 * Test produced indeterminate results.
 */
public class Unresolved extends Result
{
  /**
   * Constructs an Unresolved result code with additional information.
   */
  public Unresolved(String msg) {
    super("UNRESOLVED", msg);
  }
  /**
   * Constructs an Unresolved result code.
   */
  public Unresolved() {
    this("");
  }
}
