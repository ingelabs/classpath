/*************************************************************************
/* FileNameMap.java -- Maps filenames to MIME types
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

package java.net;

/**
  * This interface has one method which, when passed a filename, returns
  * the MIME type associated with that filename.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public abstract interface FileNameMap
{
/**
  * This method is passed a filename and is responsible for determining
  * the appropriate MIME type for that file.
  *
  * @param filename The name of the file to generate a MIME type for.
  *
  * @return The MIME type for the filename passed in.
  */
public abstract String
getContentTypeFor(String filename);

} // interface FileNameMap
