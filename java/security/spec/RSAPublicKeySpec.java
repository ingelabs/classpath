/* RSAPublicKeySpec.java --- RSA Public Key Specificaton class
   
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
	RSA Public Key class Specification. Used to maintain the RSA
	Public Keys.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public class RSAPublicKeySpec implements KeySpec
{
private BigInteger modulus;
private BigInteger publicExponent;

/**
	Constructs a new RSAPublicKeySpec with the specified
	modulus and publicExponent.

	@param modulus the RSA modulus
	@param publicExponent the public key exponent
*/
public RSAPublicKeySpec(BigInteger modulus, BigInteger publicExponent)
{
	this.modulus = modulus;
	this.publicExponent = publicExponent;
}

/**
	Gets the RSA modulus.

	@return the RSA modulus
*/
public BigInteger getModulus()
{
	return this.modulus;
}

/**
	Gets the RSA public exponent.

	@return the RSA public exponent
*/
public BigInteger getPublicExponent()
{
	return this.publicExponent;
}

}
