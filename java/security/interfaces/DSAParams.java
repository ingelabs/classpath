/*************************************************************************
/* DSAParams.java -- Digital Signature Algorithm parameter access
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
  * This interface allows the Digital Signature Algorithm (DSA) parameters
  * to be queried.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public interface DSAParams
{

/**
  * Returns the base, or 'g' value
  *
  * @return The DSA base value
  */
public abstract BigInteger
getG();

/*************************************************************************/

/**
  * Returns the prime, or 'p' value
  *
  * @return The DSA prime value
  */
public abstract BigInteger
getP();

/*************************************************************************/

/**
  * Returns the subprime, or 'q' value
  *
  * @return The DSA subprime value
  */
public abstract BigInteger
getQ();

} // interface DSAParams

