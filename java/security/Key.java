/*************************************************************************
/* Key.java -- A abstract representation of a digital key
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

import java.io.Serializable;

/**
  * This interfaces models the base characteristics that all keys must
  * have.  These are:  a key algorithm, an encoded form, and a format used
  * to encode the key.  Specific key types inherit from this interface.
  * <p>
  * Note that since this interface extends <code>Serializable</code>, all
  * keys may be serialized.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface Key extends Serializable
{

/*************************************************************************/

/*
 * Interface Variables
 */

/**
  * The verion identifier used for serialization.
  */
public static final long serialVersionUID = 0L;

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the name of the algorithm for this key.  This is a
  * <code>String</code> such as "RSA".
  *
  * @return The name of the algorithm in use
  */
public abstract String
getAlgorithm();

/*************************************************************************/

/**
  * This method returns the name of the encoding format for this key.  This
  * is the name of the ASN.1 data format used for this key, such as
  * "X.509" or "PKCS#8".  This method returns <code>null</code> if this key
  * does not have an encoding format.
  *
  * @return The name of the encoding format for this key, or <code>null</code> if there is no such format.
  */
public abstract String
getFormat();

/*************************************************************************/

/**
  * This method returns the encoded form of the key.  If this key does not
  * support encoding, this method returns <code>null</code>
  *
  * @return The encoded form of the key, or <code>null</code> if no encoded form is available.
  */
public abstract byte[]
getEncoded();

} // interface Key

