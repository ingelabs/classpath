/*************************************************************************
/* FilenameFilter.java -- Filter a list of filenames
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
  * This interface has one method which is used for filtering filenames
  * returned in a directory listing.  It is currently used by the 
  * @code{File.list()} method and by the filename dialog in AWT.
  *
  * The method in this interface determines if a particular file should
  * or should not be included in the file listing.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface FilenameFilter
{

/**
  * This method determines whether or not a given file should be included
  * in a directory listing.
  *
  * @param dir The @code{File} instance for the directory being read
  * @param name The name of the file to test
  *
  * @return @code{true} if the file should be included in the list, @code{false} otherwise.
  */
public abstract boolean
accept(File dir, String name);

} // interface FilenameFilter

