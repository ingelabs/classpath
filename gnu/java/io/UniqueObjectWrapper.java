/*************************************************************************
/* UniqueObjectWrapper.java -- Wrapper class used to override equals()
/*                             and hashCode() to be as discriminating
/*			       as possible
/*
/* Copyright (c) 1998 by Geoffrey C. Berry (gcb@cs.duke.edu)
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

//TODO: comments
//      test suite

package gnu.java.io;

public class UniqueObjectWrapper
{
  public UniqueObjectWrapper( Object o )
  {
    object = o;
  }

  public int hashCode()
  {
    return System.identityHashCode( object );
  }

  public boolean equals( Object o )
  {
    return object == ((UniqueObjectWrapper)o).object;
  }

  public String toString()
  {
    return "UniqueObjectWrapper< " + object + " >";
  }

  public Object object;
}
