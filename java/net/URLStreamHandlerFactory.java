/*************************************************************************
/* URLStreamHandlerFactory.java -- Maps protocols to URLStreamHandlers
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
  * This interface contains one method which maps the protocol portion of
  * a URL (eg, "http" in "http://www.urbanophile.com/arenn/") to a 
  * URLStreamHandler object.
  *
  * @version 0.5
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface URLStreamHandlerFactory
{
/**
  * This method maps the protocol portion of a URL to a URLStreamHandler
  * object.
  *
  * @param protocol The protocol name to map ("http", "ftp", etc).
  *
  * @return The URLStreamHandler for the specified protocol
  */
public abstract URLStreamHandler
createURLStreamHandler(String protocol);

} // interface URLStreamHandlerFactory
