/*************************************************************************
/* Resolvable.java -- Returns an object to replace the one being de-serialized
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
  * This interface is implemented when an object wishes to return another
  * object to replace it during de-serialization.  It has one method that
  * returns the object that should be used to replace the original object.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Resolvable extends Serializable
{

/**
  * This method returns the object that should be used to replace the 
  * original object during de-serialization.
  *
  * @return The replacement object
  */
public abstract Object
readResolve();

} // interface Resolvable

