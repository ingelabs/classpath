/*************************************************************************
/* SignatureException.java -- Generic error in signature
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
  * This exception is thrown when a problem is encountered with a
  * digital signature.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class SignatureException extends GeneralSecurityException
{

/**
  * This method initializes a new instance of <code>SignatureException</code>
  * with no descriptive error message.
  */
public
SignatureException()
{
  super();
}

/*************************************************************************/

/**
  * This method initializes a new instance of <code>SignatureException</code>
  * with a descriptive error message.
  */
public
SignatureException(String msg)
{
  super(msg);
}

} // class SignatureException

