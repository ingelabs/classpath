/*************************************************************************
/* RSAPrivateCrtKey.java -- An RSA private key in CRT format
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

package java.security.interfaces;

import java.math.BigInteger;

/**
  * This interface provides access to information about an RSA private
  * key in Chinese Remainder Theorem (CRT) format.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface RSAPrivateCrtKey extends RSAPrivateKey
{

/**
  * Returns the public exponent for this key
  *
  * @return The public exponent for this key
  */
public abstract BigInteger
getPublicExponent();

/*************************************************************************/

/**
  * Returns the primeP value
  *
  * @return The primeP value
  */
public abstract BigInteger
getPrimeP();

/*************************************************************************/

/**
  * Returns the primeQ value
  *
  * @return The primeQ value
  */
public abstract BigInteger
getPrimeQ();

/*************************************************************************/

/**
  * Returns the primeExponentP
  *
  * @return The primeExponentP
  */
public abstract BigInteger
getPrimeExponentP();

/*************************************************************************/

/**
  * Returns the primeExponentQ
  *
  * @return The primeExponentQ
  */
public abstract BigInteger
getPrimeExponentQ();

/*************************************************************************/

/**
  * Returns the CRT coefficient
  *
  * @return The CRT coefficient
  */
public abstract BigInteger
getCrtCoefficient();

} // interface RSAPrivateCrtKey

