/*************************************************************************
/* Replaceable.java -- Replace an object with another object
/*
/* Copyright (c) 1998 by Aaron M. Renn (arenn@urbanophile.com)
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
/* You should have received a copy of the GNU Library General Public License
/* along with this program; if not, write to the Free Software Foundation
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

