/* RSAPrivateCrtKeySpec.java --- RSA Private Certificate Key Specificaton class
   
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
	RSA Private Certificate Key class Specification. Used to 
	maintain the RSA Private Certificate Keys with the 
	<I>Chinese Remainder Theorem</I>(CRT) as specified by PKCS#1.

	@since JDK 1.2

	@author Mark Benvenuto
*/
public class RSAPrivateCrtKeySpec extends RSAPrivateKeySpec
{
private BigInteger publicExponent;
private BigInteger primeP;
private BigInteger primeQ;
private BigInteger primeExponentP;
private BigInteger primeExponentQ;
private BigInteger crtCoefficient;

/**
	Constructs a new RSAPrivateKeySpec with the specified
	variables.

	@param modulus the RSA modulus
	@param publicExponent the public key exponent
	@param privateExponent the private key exponent
	@param primeP the prime P
	@param primeQ the prime Q
	@param primeExponentP the prime exponent P
	@param primeExponentQ the prime exponent P
	@param crtCoefficient the CRT coefficient
*/
public RSAPrivateCrtKeySpec(BigInteger modulus,
                            BigInteger publicExponent,
                            BigInteger privateExponent,
                            BigInteger primeP,
                            BigInteger primeQ,
                            BigInteger primeExponentP,
                            BigInteger primeExponentQ,
                            BigInteger crtCoefficient)
{
	super( modulus, privateExponent);
	this.publicExponent = publicExponent;
	this.primeP = primeP;
	this.primeQ = primeQ;
	this.primeExponentP = primeExponentP;
	this.primeExponentQ = primeExponentQ;
	this.crtCoefficient = crtCoefficient;
}

/**
	Gets the RSA public exponent.

	@return the RSA public exponent
*/
public BigInteger getPublicExponent()
{
	return this.publicExponent;
}

/**
	Gets the RSA prime P.

	@return the RSA prime P
*/
public BigInteger getPrimeP()
{
	return this.primeP;
}

/**
	Gets the RSA prime Q.

	@return the RSA prime Q
*/
public BigInteger getPrimeQ()
{
	return this.primeQ;
}

/**
	Gets the RSA prime exponent P.

	@return the RSA prime exponent P
*/
public BigInteger getPrimeExponentP()
{
	return this.primeExponentP;
}

/**
	Gets the RSA prime exponent P.

	@return the RSA prime exponent Q
*/
public BigInteger getPrimeExponentQ()
{
	return this.primeExponentQ;
}

/**
	Gets the RSA CRT coefficient.

	@return the RSA CRT coefficient
*/
public BigInteger getCrtCoefficient()
{
	return this.crtCoefficient;
}

}
