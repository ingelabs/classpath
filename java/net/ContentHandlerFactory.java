/*************************************************************************
/* ContentHandlerFactory.java -- Interface for creating content handlers
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
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.net;

/**
  * This interface maps MIME types to ContentHandler objects.  It consists
  * of one method that, when passed a MIME type, returns a handler for that
  * type.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface ContentHandlerFactory
{
/**
  * This method is passed a MIME type as a string and is responsible for
  * returning the appropriate ContentType object.
  *
  * @param mime_type The MIME type to map to a ContentHandler
  *
  * @return The ContentHandler for the passed in MIME type
  */
public abstract ContentHandler
createContentHandler(String mime_type);

} // interface ContentHandlerFactory

