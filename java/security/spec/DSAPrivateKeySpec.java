/* DSAPrivateKeySpec.java --- DSA Private Key Specificaton class
   
  Copyright (c) 1999 by Free Software Foundation, Inc.
  Written by Mark Benvenuto <ivymccough@worldnet.att.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU Library General Public License as published 
  by the Free Software Foundation, version 2. (see COPYING.LIB)

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation
  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307 USA. */

package java.security.spec;
import java.math.BigInteger;

/**
	DSA Private Key class Specification. Used to maintain the DSA
	Private Keys.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public class DSAPrivateKeySpec extends Object implements KeySpec 
{
private BigInteger x = null;
private BigInteger p = null;
private BigInteger q = null;
private BigInteger g = null;

/**
	Constructs a new DSAPrivateKeySpec with the specified x, p, q, and g.

	@param x the private key
	@param p the prime
	@param q the sub-prime
	@param g the base
*/
public DSAPrivateKeySpec(BigInteger x, BigInteger p, BigInteger q, BigInteger g) 
{
	this.x = x;
	this.p = p;
	this.q = q;
	this.g = g;
}

/**
	Returns private key x for the DSA algorithm.

	@return Returns the requested BigInteger
*/
public BigInteger getX() 
{
	return this.x;
}

/**
	Returns p for the DSA algorithm.

	@return Returns the requested BigInteger
*/
public BigInteger getP() 
{
	return this.q;
}

/**
	Returns p for the DSA algorithm.

	@return Returns the requested BigInteger
*/
public BigInteger getQ() 
{
	return this.q;
}

/**
	Returns g for the DSA algorithm.

	@return Returns the requested BigInteger
*/
public BigInteger getG() 
{
	return this.g;
}

}
