/*************************************************************************
/* Principal.java -- A security entity
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
  * This interface models an entity (such as a user or a certificate authority)
  * for the purposes of applying the Java security model.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Principal
{

/**
  * This method returns a <code>String</code> that names this 
  * <code>Principal</code>.
  *
  * @return The name of this <code>Principal</code>.
  */
public abstract String
getName();

/*************************************************************************/

/**
  * This method tests another <code>Principal</code> object for equality
  * with this one.
  * 
  * @param obj The <code>Object</code> (which is a <code>Principal</code>) to test for equality against.
  *
  * @return <code>true</code> if the specified <code>Principal</code> is equal to this one, <code>false</code> otherwise.
  */
public abstract boolean
equals(Object obj);

/*************************************************************************/

/**
  * This method returns a hash code value for this <code>Principal</code>.
  *
  * @return A hash value
  */
public abstract int
hashCode();

/*************************************************************************/

/**
  * This method returns a <code>String</code> representation of this
  * <code>Principal</code>.
  *
  * @return This <code>Principal</code> represented as a <code>String</code>.
  */
public abstract String
toString();

} // interface Principal

