/*************************************************************************
/* Comparable.java -- Interface for comparaing objects to obtain an ordering
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
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
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.lang;

/** 
    Interface for objects that can be ordering among other
    objects. The ordering can be <EM>total</EM>, such that two objects
    only compare equal if they are equal by the equals method, or
    <EM>partial</EM> such that this is not necessarily true. For
    example, a case-sensitive dictionary order comparison of Strings
    is total, but if it is case-insensitive it is partial, because
    "abc" and "ABC" compare as equal even though "abc".equals("ABC")
    returns false.

    @see java.util.Comparator
*/
public interface Comparable
{
  /**
     @return a negative integer if this object is less than
     <code>o<code>, zero if this object is equal to <code>o</code>, or
     a positive integer if this object is greater than <code>o</code>
  */
  public int compareTo( Object o );
}
