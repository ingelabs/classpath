/*************************************************************************
/* Replaceable.java -- Replace an object with another object
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

package java.io;

/**
  * This interface is used to indicate that an object may want to have
  * another object serialized instead of itself.  It contains one method
  * that is to be called when an object is to be serialized.  That method
  * is reponsible for returning the real object that should be serialized
  * instead of object being queried.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Replaceable extends Serializable
{

/**
  * This method returns the object that should be serialized instead of
  * this object
  *
  * @return The real object that should be serialized
  */
public abstract Object
writeReplace();

} // interface Replaceable

