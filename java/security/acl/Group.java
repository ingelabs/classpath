/*************************************************************************
/* Group.java -- Represents a group of Principals
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

import java.security.Principal;
import java.util.Enumeration;

/**
  * This interface represents a group of <code>Principals</code>.  Note that
  * since this interface extends <code>Principal</code>, a <code>Group</code>
  * can be used where ever a <code>Principal</code> is requested.  This
  * includes arguments to the methods in this interface.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Group
{

/**
  * This method adds a new <code>Principal</code> to this group.
  *
  * @param user The new <code>Principal</code> to add
  *
  * @return <code>true</code> if the user was successfully added or <code>false</code> if the user is already a member
  */
public abstract boolean 
addMember(Principal user);

/*************************************************************************/

/**
  * This method deletes a member from the group.
  *
  * @param user The <code>Principal</code> to delete
  *
  * @return <code>true</code> if the user was successfully deleted or <code>false</code> if the user is not a member of the group
  */
public abstract boolean
removeMember(Principal user);

/*************************************************************************/

/**
  * This method tests whether or not a given <code>Principal</code> is a
  * member of this group.
  *
  * @param user The <code>Principal</code> to test for membership
  *
  * @return <code>true</code> if the user is member, <code>false</code> otherwise
  */
public abstract boolean
isMember();

/*************************************************************************/

/**
  * This method returns a list of all members of the group as an 
  * <code>Enumeration</code>.
  *
  * @return The list of all members of the group
  */
public abstract Enumeration
members();

} // interface Group

