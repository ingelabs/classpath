/*************************************************************************
/* Permission.java -- Information about an ACL permission
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

package java.security.acl;

/**
  * This interface provides information about a permission that can be
  * granted.  Note that this is <em>not</em> the same as the class
  * <code>java.security.Permission</code>.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Permission
{

/**
  * This method tests whether or not a specified <code>Permission</code>
  * (passed as an <code>Object</code>) is the same as this permission.
  *
  * @param perm The permission to check for equality
  *
  * @return <code>true</code> if the specified permission is the same as this one, <code>false</code> otherwise
  */
public abstract boolean
equals(Object perm);

/*************************************************************************/

/**
  * This method returns this <code>Permission</code> as a <code>String</code>.
  *
  * @return A <code>String</code> representing this permission.
  */
public String
toString();

} // interface Permission

