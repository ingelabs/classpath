/*************************************************************************
/* PrivilegedAction.java -- Perform a privileged action
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
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

package java.security;

/**
  * This interface specifes a single <code>run</code> method that 
  * executes a privileged operation.  This method is called by 
  * <code>AccessController.doPrivileged()</code> after that method
  * activiates the required privileges.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface PrivilegedAction
{

/**
  * This method performs an operation that requires higher privileges to
  * perform.  It is called when a section of code invokes
  * <code>AccessController.doPrivileged()</code>.
  *
  * @return obj An implementation dependent return value
  */
public abstract Object
run();

} // interface PrivilegedAction

